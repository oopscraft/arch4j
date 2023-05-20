pipeline {
    agent any
    parameters {
        string(name: 'MAVEN_URL', defaultValue: params.MAVEN_URL, description: 'maven url')
        credentials(credentialType:'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'MAVEN_CREDENTIALS',
                defaultValue: params.MAVEN_CREDENTIALS ?: '___',
                description: 'maven credentials')
        string(name:'PUBLISHING_MAVEN_URL', defaultValue: params.PUBLISHING_MAVEN_URL, description:'publishing maven url')
        credentials(credentialType:'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'PUBLISHING_MAVEN_CREDENTIALS',
                defaultValue: params.PUBLISHING_MAVEN_CREDENTIALS,
                descripton: 'publishing maven credentials')
        string(name: 'JIB_FROM_IMAGE', defaultValue: params.JIB_FROM_IMAGE, description: 'container base image')
        credentials(credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'JIB_FROM_CREDENTIALS',
                defaultValue: params.JIB_FROM_CREDENTIALS,
                description: 'base image repository credentials')
        string(name: 'JIB_TO_IMAGE', defaultValue: params.JIB_TO_IMAGE, description: 'target image')
        credentials(credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'JIB_TO_CREDENTIALS',
                defaultValue: params.JIB_TO_CREDENTIALS,
                description: 'target image repository credentials')
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage("publish") {
            environment {
                MAVEN_CREDENTIALS = credentials('MAVEN_CREDENTIALS')
                PUBLISHING_MAVEN_CREDENTIALS = credentials('PUBLISHING_MAVEN_CREDENTIALS')
            }
            steps {
                cleanWs()
                checkout scm
                sh '''
                ./gradlew :arch4j-web:publish -x test --refresh-dependencies \
                -PmavenUrl=${MAVEN_URL} \
                -PmavenUsername=${MAVEN_CREDENTIALS_USR} \
                -PmavenPassword=${MAVEN_CREDENTIALS_PWD} \
                -PpublishingMavenUrl=${PUBLISHING_MAVEN_URL} \
                -PpublishingMavenUsername=${PUBLISHING_MAVEN_CREDENTIALS_USR} \
                -PpublishingMavenPassword=${PUBLISHING_MAVEN_CREDENTIALS_PSW} \
                -x test --refresh-dependencies --stacktrace 
                '''.stripIndent()
            }
        }
        stage("jib") {
            environment {
                IMAGE_CREDENTIALS = credentials('IMAGE_CREDENTIALS')
            }
            steps {
                cleanWs()
                checkout scm
                sh '''
                ./gradlew :arch4j-web:jib -x test --refresh-dependencies \
                -DincludeSubmodule=true \
                -PimagePublishHost=${IMAGE_PUBLISH_HOST} \
                -PimageUsername=${IMAGE_CREDENTIALS_USR} \
                -PimagePassword=${IMAGE_CREDENTIALS_PSW} \
                -PimageTag=${IMAGE_TAG} \
                ${GRADLE_EXTRA_OPTION}
                '''.stripIndent()
            }
        }
        stage("rollout") {
            steps {
                sh("kubectl rollout restart deployment/apps-web")
                sh("kubectl rollout status deployment/apps-web")
            }
        }
    }
}

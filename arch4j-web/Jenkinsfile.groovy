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
                description: 'publishing maven credentials')
        string(name: 'JIB_FROM_IMAGE', defaultValue: params.JIB_FROM_IMAGE, description: 'container base image')
        credentials(credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'JIB_FROM_AUTH_CREDENTIALS',
                defaultValue: params.JIB_FROM_AUTH_CREDENTIALS,
                description: 'base image repository credentials')
        string(name: 'JIB_TO_IMAGE', defaultValue: params.JIB_TO_IMAGE, description: 'target image')
        string(name: 'JIB_TO_TAGS', defaultValue: params.JIB_TO_TAGS, description: 'target image tags')
        credentials(credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'JIB_TO_AUTH_CREDENTIALS',
                defaultValue: params.JIB_TO_AUTH_CREDENTIALS,
                description: 'target image repository credentials')
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage("prepare") {
            steps {
                cleanWs()
                checkout scm
            }
        }
        stage("publish") {
            environment {
                MAVEN_CREDENTIALS = credentials('MAVEN_CREDENTIALS')
                PUBLISHING_MAVEN_CREDENTIALS = credentials('PUBLISHING_MAVEN_CREDENTIALS')
            }
            steps {
                sh '''
                ./gradlew :arch4j-web:publish -x test --refresh-dependencies --stacktrace \
                -PmavenUrl=${MAVEN_URL} \
                -PmavenUsername=${MAVEN_CREDENTIALS_USR} \
                -PmavenPassword=${MAVEN_CREDENTIALS_PWD} \
                -PpublishingMavenUrl=${PUBLISHING_MAVEN_URL} \
                -PpublishingMavenUsername=${PUBLISHING_MAVEN_CREDENTIALS_USR} \
                -PpublishingMavenPassword=${PUBLISHING_MAVEN_CREDENTIALS_PSW} \
                '''.stripIndent()
            }
        }
        stage("jib") {
            environment {
                JIB_FROM_AUTH_CREDENTIALS = credentials('JIB_FROM_AUTH_CREDENTIALS')
                JIB_TO_AUTH_CREDENTIALS = credentials('JIB_TO_AUTH_CREDENTIALS')
            }
            steps {
                sh '''
                ./gradlew :arch4j-web:jib -x test --refresh-dependencies --stacktrace \
                -PjibFromImage=${JIB_FROM_IMAGE} \
                -PjibFromAuthUsername=${JIB_FROM_AUTH_CREDENTIALS_USR} \
                -PjibFromAuthPassword=${JIB_FROM_AUTH_CREDENTIALS_PSW} \
                -PjibToImage=${JIB_TO_IMAGE} \
                -PjibToTags=${JIB_TO_TAGS} \
                -PjibToAuthUsername=${JIB_TO_AUTH_CREDENTIALS_USR} \
                -PjibToAuthPassword=${JIB_TO_AUTH_CREDENTIALS_PSW} \
                '''.stripIndent()
            }
        }
        stage("rollout") {
            steps {
                sh("kubectl rollout restart deployment/arch4j-web")
                sh("kubectl rollout status deployment/arch4j-web")
            }
        }
    }
}

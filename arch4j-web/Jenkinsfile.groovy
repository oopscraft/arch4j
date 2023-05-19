pipeline {
    agent any
    parameters {
        string(name: 'IMAGE_PUBLISH_HOST', defaultValue: params.IMAGE_PUBLISH_HOST ?: 'nexus.oopscraft.org:9997', description: 'Image publish host')
        credentials(credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                name: 'IMAGE_CREDENTIALS',
                defaultValue: params.IMAGE_CREDENTIALS ?: '___',
                description: 'Image Publish credentials')
        string(name: 'IMAGE_TAG', defaultValue: params.IMAGE_TAG ?: 'latest', description: 'Image tag')
        string(name: 'GRADLE_BUILD_OPTION', defaultValue: params.GRADLE_BUILD_OPTION ?: '--stacktrace', description: 'gradle build option')
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage("build") {
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
        stage("deploy") {
            steps {
                sh("kubectl rollout restart deployment/apps-web")
                sh("kubectl rollout status deployment/apps-web")
            }
        }
    }
}

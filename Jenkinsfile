pipeline {
    agent any

    triggers {
        pollSCM('*/5 * * * *')
    }

    stages {

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test -Dspring.profiles.active=test'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying application...'

                dir('src/main/java/com/example/demo') {
                    sh 'ansible-playbook -i hosts deploy.yml'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            mail to: 'srengty@gmail.com',
                 subject: "Build Failed: ${env.JOB_NAME}",
                 body: """
Build failed.

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
URL: ${env.BUILD_URL}
"""
        }
    }
}
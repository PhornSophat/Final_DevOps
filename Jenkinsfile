pipeline {
    agent any
    triggers {
        pollSCM('*/5 * * * *') // Poll Git every 5 minutes
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
            echo 'Deploying...'
            // Navigate to the directory where deploy.yml is located
            dir('src/main/java/com/example/demo') {
                // Ensure your 'hosts' file is also moved to this directory, 
                // or provide the correct path to it.
                sh 'ansible-playbook -i hosts deploy.yml'
            }
        }
    }
    }
    post {
        failure {
            mail to: 'srengty@gmail.com',
                 subject: "Build Failed: ${env.JOB_NAME}",
                 body: "Build failed at ${env.BUILD_URL}. Please check the logs."
        }
    }
}
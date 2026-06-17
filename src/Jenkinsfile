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
                // This will find where your deploy.yml is actually located
                sh 'find . -name "deploy.yml"' 
                
                // Use the path you find from the command above
                // If it is in the root, remove the dir('src') block:
                sh 'ansible-playbook -i hosts deploy.yml'
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
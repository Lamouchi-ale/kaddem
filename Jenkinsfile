pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Lamouchi-ale/kaddem.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("kaddem-app:${env.BUILD_ID}")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'b068dfbe-48f1-4914-9540-ccc68b451ac5') {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Clean Up') {
            steps {
                sh 'docker rmi kaddem-app:${env.BUILD_ID}'
            }
        }
    }
}

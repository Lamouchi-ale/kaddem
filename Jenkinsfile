pipeline {
    agent any
    stages {
	stage('Check Docker Version') {
    steps {
        sh 'docker --version'
    }
}
        stage('Checkout') {
            steps {
                // Ajoutez le nom de la branche à cloner
                git branch: 'azizbranch', url: 'https://github.com/Lamouchi-ale/kaddem.git', credentialsId: 'b068dfbe-48f1-4914-9540-ccc68b451ac5'
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
                sh "docker rmi kaddem-app:${env.BUILD_ID}"
            }
        }
    }
}

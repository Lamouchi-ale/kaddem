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
                // Checkout the code from the 'azizbranch' branch
                git branch: 'azizbranch', url: 'https://github.com/Lamouchi-ale/kaddem.git', credentialsId: 'b068dfbe-48f1-4914-9540-ccc68b451ac5'
            }
        }

        stage('Build JAR') {
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Ensure the correct Dockerfile and context are used
                    dockerImage = docker.build("azizaydi/kaddem-app:${env.BUILD_ID}", '.')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push the Docker image to the registry
                    docker.withRegistry('https://index.docker.io/v1/', 'b068dfbe-48f1-4914-9540-ccc68b451ac5') {
                        dockerImage.push()
                    }
                }
            }
        }

        // Stage des tests unitaires
        stage('Run Unit Tests') {
            steps {
                script {
                   sh 'chmod +x mvnw'
                    sh './mvnw test'
                }
            }
        }

       stage('Clean Up') {
    steps {
        script {
            // Suppression de l'image Docker après le déploiement
            sh "docker rmi azizaydi/kaddem-app:${env.BUILD_ID}"

            // Suppression des conteneurs arrêtés
            sh 'docker container prune -f'

            // Suppression des volumes non utilisés
            sh 'docker volume prune -f'

            // Suppression des réseaux non utilisés
            sh 'docker network prune -f'
        }
    }
}


        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // Ensure any existing containers are stopped and removed before deployment
                    dir('kaddem') {
                        sh 'docker-compose down -v' // Shut down existing containers and remove volumes
                        sh 'docker-compose up -d'  // Start the containers in detached mode
                    }
                }
            }
        }
    }
}

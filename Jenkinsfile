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

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run the SonarQube analysis and send results to the SonarQube server
                    sh 'mvn sonar:sonar -Dsonar.projectKey=kaddem -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_5aa3a90c18655b36d409d5c114e16c200a3527f4'
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
                    // Remove the Docker image after deployment
                    sh "docker rmi azizaydi/kaddem-app:${env.BUILD_ID}"

                    // Remove stopped containers, unused volumes, and networks
                    sh 'docker container prune -f'
                    sh 'docker volume prune -f'
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

pipeline {
    agent any
    environment {
        // Define environment variables
        SONARQUBE_TOKEN = credentials('squ_f1e321a5358280c009c2cb33691990fb0c51d6a5') // Use credentials for SonarQube
        DOCKER_CREDENTIALS_ID = 'a0e8eee8-ff55-43b2-b265-b5fe9d7fd5ec' // Define Docker credentials ID
        DOCKER_IMAGE = "houss12/kaddem-app:${env.BUILD_ID}"
    }
    stages {
        
        stage('Checkout') {
            steps {
                // Clone the repository using the specified credentials
                git branch: 'houssem', 
                    url: 'https://github.com/Lamouchi-ale/kaddem.git', 
                    credentialsId: DOCKER_CREDENTIALS_ID
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run the SonarQube analysis and send results to the SonarQube server using credentials
                    withCredentials([string(credentialsId: 'sonar-token-id', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                                -Dsonar.projectKey=kaddem \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=$SONAR_TOKEN
                        """
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                script {
                    // Clean and package the Maven project
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    // Deploy the JAR file to the Nexus repository
                    sh 'mvn deploy -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in the current directory
                    dockerImage = docker.build(DOCKER_IMAGE)
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push the Docker image to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        dockerImage.push()
                    }
                    // Optionally push the `latest` tag
                    dockerImage.push('latest')
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment were successful!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}

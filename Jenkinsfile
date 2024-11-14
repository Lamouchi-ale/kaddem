pipeline {
    agent any
    environment {
        // Define environment variables
        
        DOCKER_CREDENTIALS_ID = '3e1a5f58-c5cc-4416-80c1-9c88c72540d1' // Define Docker credentials ID
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
            // Using the provided token directly (not recommended for production)
            sh """
                mvn clean compile sonar:sonar \
                -Dsonar.projectKey=kaddem \
                -Dsonar.host.url=http://localhost:9000 \
                -Dsonar.token=squ_f1e321a5358280c009c2cb33691990fb0c51d6a5 \
                -Dsonar.java.binaries=target/classes
            """
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

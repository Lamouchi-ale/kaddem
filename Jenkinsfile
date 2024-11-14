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
                checkout([$class: 'GitSCM', 
                          branches: [[name: '*/houssem']], 
                          userRemoteConfigs: [[url: 'https://github.com/Lamouchi-ale/kaddem.git', credentialsId: 'a0e8eee8-ff55-43b2-b265-b5fe9d7fd5ec']]])
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
                -Dsonar.token=squ_dcff9b1c5edfacce8ebf2c81912fe5c9f40b210f \
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
                    sh 'mvn deploy -DskipTests  --settings /usr/share/maven/conf/settings.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in the current directory
                    //dockerImage = docker.build(DOCKER_IMAGE)
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh 'docker login -u houss12 -p dckr_pat_OWY5P09g6zo8ACbu1u8NjcjUNNo'
                    sh 'docker push ${DOCKER_IMAGE}'
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

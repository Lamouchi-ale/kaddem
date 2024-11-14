pipeline {
    agent any
    environment {
        // Environment variable for the generated token
        SONARQUBE_TOKEN = ''
        NEXUS_REPO_URL = 'http://localhost:8081/repository/maven-public/'  // Nexus repo URL
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'NidhalDalhoumi-5SE4-G8', url: 'https://github.com/Lamouchi-ale/kaddem.git', credentialsId: 'NidhalSecret'
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
             stage('SonarQube Analysis') {
                            steps {
                                script {
                                    // Run the SonarQube analysis and send results to the SonarQube server
                                    sh 'mvn sonar:sonar -Dsonar.projectKey=kaddem -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqa_5c0f4183fc9a65a6c75174e866f25bbcebd7a989'
                                }
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
                    dockerImage = docker.build("nidhaldalhoumi/5se4-g8-kaddem:${env.BUILD_ID}", '.')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {

                    docker.withRegistry('https://index.docker.io/v1/', 'NidhalSecret') {
                        dockerImage.push()
                    }
                }
            }
        }
    }
} 

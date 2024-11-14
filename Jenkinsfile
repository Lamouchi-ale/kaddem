pipeline {
    agent any
    environment {
        SONARQUBE_TOKEN = ''
        NEXUS_REPO_URL = 'http://localhost:8081/repository/maven-public/'  
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
                    dockerImage = docker.build("nidhaldalhoumi/5se4-g8-kaddem:${env.BUILD_ID}", '.')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {

                    docker.withRegistry('https://index.docker.io/v1/', 'b8d466bb-196a-42b0-b4c3-fad1b7bf6990') {
                        dockerImage.push()
                    }
                }
            }
        }
            stage('Deploy with Docker Compose') {
            steps {
                script {
                    dir('kaddem') {
                        sh 'docker-compose down --volumes || true'
                        sh 'docker-compose up -d'  
                    }
                }
            }
        }
    }
} 

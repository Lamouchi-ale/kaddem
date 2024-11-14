pipeline {
    agent any
    stages {

        stage('Checkout') {
            steps {

                git branch: 'houssem', url: 'https://github.com/Lamouchi-ale/kaddem.git', credentialsId: 'a0e8eee8-ff55-43b2-b265-b5fe9d7fd5ec'
            }
        }



        stage('SonarQube Analysis') {
                                    steps {
                                        script {
                                            // Run the SonarQube analysis and send results to the SonarQube server
                                            sh 'mvn sonar:sonar -Dsonar.projectKey=kaddem -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_f1e321a5358280c009c2cb33691990fb0c51d6a5'
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




                                        stage('Deploy to Nexus') {
                                                    steps {
                                                        script {

                                                            sh 'mvn deploy -DskipTests'
                                                        }
                                                    }
                                                }




                                                 stage('Build Docker Image') {
                                                            steps {
                                                                script {
                                                                    // Ensure the correct Dockerfile and context are used
                                                                    dockerImage = docker.build("houss12/kaddem-app:${env.BUILD_ID}", '.')
                                                                }
                                                            }
                                                        }

                                                        stage('Push Docker Image') {
                                                            steps {
                                                                script {

                                                                    docker.withRegistry('https://index.docker.io/v1/', 'a0e8eee8-ff55-43b2-b265-b5fe9d7fd5ec') {
                                                                        dockerImage.push()
                                                                    }
                                                                }
                                                            }
                                                        }
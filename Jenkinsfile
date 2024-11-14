pipeline {
    agent any
    environment {
        // Définir une variable d'environnement pour l'image Docker
        DOCKER_IMAGE = 'rahma-bousrih-kaddem' // Utilisez cette variable pour l'image Docker
        DOCKERHUB_CREDENTIALS_ID = 'docker'  // ID des credentials DockerHub dans Jenkins
    }
    stages {
        stage('Git checkout') {
            steps {
                git branch: 'bousrihrahma_5SE4_G8', url: 'https://github.com/Lamouchi-ale/kaddem.git'
            }
        }
        stage('Compile code') {
            steps {
                script {
                    echo "Starting Maven build..."
                    sh 'mvn clean package'
                    echo "Maven build completed!"
                }
            }
        }
        stage('SonarQube') {
            steps {
                script {
                    // Remplacer l'adresse et le token d'authentification
                    sh 'mvn sonar:sonar -Dsonar.projectKey=kaddem -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.login=squ_26cb70c0ca54fd40b7fe225766adba73962ece19'
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
                    echo "Deploying to Nexus..."
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: 'http://192.168.33.10:8081/',
                        groupId: 'tn.esprit.spring',
                        artifactId: 'kaddem',
                        version: '0.0.3-SNAPSHOT',
                        repository: 'kaddem',
                        credentialsId: 'nexus', // Assurez-vous que cette ID est correcte dans Jenkins
                        artifacts: [
                            [artifactId: 'kaddem',
                             classifier: '',
                             file: 'target/kaddem-0.0.3-SNAPSHOT.jar',
                             type: 'jar']
                        ]
                    )
                    echo "Deployment to Nexus completed!"
                }
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    echo 'Building Docker image...'
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh '''
                             docker build -t ${DOCKER_IMAGE}:latest .
                             echo ${DOCKERHUB_PASSWORD} | docker login -u ${DOCKERHUB_USERNAME} --password-stdin
                             docker tag ${DOCKER_IMAGE}:latest rahma473/${DOCKER_IMAGE}:latest
                             docker push rahma473/${DOCKER_IMAGE}:latest
                        '''
                    }
                    echo 'Docker image built and pushed successfully!'
                }
            }
        }
        stage('Docker Compose Up') {
            steps {
                script {
                    echo 'Starting services with Docker Compose...'
                    sh '''
                         docker-compose up -d
                    '''
                    echo 'Services started successfully!'
                }
            }
        }
    }
}

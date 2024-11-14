pipeline {
    agent any
    environment {
        // Définir une variable d'environnement pour l'image Docker
        DOCKER_IMAGE = 'your-docker-image-name' // Remplacer par le nom de votre image Docker
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
                        nexusUrl: 'http://192.168.33.10:8081',
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
        stage('Push Docker Image') {
            steps {
                script {
                    // Connexion à Docker Hub
                    sh 'docker login -u houss12 -p dckr_pat_OWY5P09g6zo8ACbu1u8NjcjUNNo'

                    // Pousser l'image Docker
                    sh 'docker push ${DOCKER_IMAGE}'
                }
            }
        }
    }
}

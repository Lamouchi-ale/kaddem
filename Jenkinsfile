pipeline {
    agent any
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
        stage('Nexus') {
            steps {
                script {
                    sh 'mvn deploy -DskipTests'
                }
            }
         stage('Push Docker Image') {
            steps {
                script {
                    sh 'docker login -u houss12 -p dckr_pat_OWY5P09g6zo8ACbu1u8NjcjUNNo'
                    sh 'docker push ${DOCKER_IMAGE}'
                }
            }
        }   }
    }
}

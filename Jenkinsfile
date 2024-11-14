pipeline {
    agent any
    stages {
        stage('Checkout') {
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
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Remplacer l'adresse et le token d'authentification
                    sh 'mvn sonar:sonar -Dsonar.projectKey=kaddem -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.login=squ_26cb70c0ca54fd40b7fe225766adba73962ece19'
                }
            }
     stage('Deploy to Nexus') {
            steps {
                script {

                    sh 'mvn deploy -DskipTests'
                }
            }
        }   } 
    }
}

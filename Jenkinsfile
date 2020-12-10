pipeline {
    agent any

    tools {
        jdk 'java11-openjdk'
    }

    stages {
        stage('Cloning Git repository') {
            steps {
                git 'https://github.com/eipc16/kubernetes-exercise'
            }
        }

        stage('Maven clean install') {
            steps {
                sh './mvnw clean install -DskipTests'
            }
        }

        stage('Maven run tests') {
            steps {
                sh './mvnw test'
            }
        }
    }
}
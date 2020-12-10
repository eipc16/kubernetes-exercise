pipeline {
    agent any

    tools {
        jdk 'openjdk_11_0_1'
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
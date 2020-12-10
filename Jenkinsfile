pipeline {
    agent any

    tools {
        jdk 'openjdk-11'
    }

    environment {
        JAVA_HOME = "${tool 'openjdk-11'}"
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }

    stages {
        stage('Cloning Git repository') {
            steps {
                git 'https://github.com/eipc16/kubernetes-exercise'
            }
        }

        stage('Display Java Version') {
            steps {
                sh '''
                echo $JAVA_HOME
                java -version
                '''
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
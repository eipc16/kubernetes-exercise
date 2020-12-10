pipeline {
    agent any

    tools {
        jdk 'openjdk-11'
    }

    env.JAVA_HOME="${tool 'openjdk-11'}"
    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

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
pipeline {
    agent any
    environment {
        DOCKER_HUB_USERNAME = credentials('forDockerhub').USR
        DOCKER_HUB_PASSWORD = credentials('forDockerhub').PSW
    }
    stages {
        stage("Permission") {
            steps {
                sh "chmod +x ./gradlew"
            }
        }

        stage("Compile") {
            steps {
                sh "./gradlew compileJava"
            }
        }

        stage("Unit Test") {
            steps {
                sh "./gradlew test"
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage("Code Coverage") {
            steps {
                sh "./gradlew jacocoTestCoverageVerification"
                sh "./gradlew jacocoTestReport"
                publishHTML(target: [
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'Jacoco Report'
                ])
            }
        }

        stage("Static Code Analysis") {
            steps {
                sh "./gradlew checkstyleMain"
                publishHTML(target: [
                    reportDir: 'build/reports/checkstyle/',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }

        stage("dockerhub deploy") {
            steps {
                echo "Set Variables"

                // Docker Hub login, build, push, and logout
                sh '''
                    docker login -u $DOCKER_HUB_USERNAME -p $DOCKER_HUB_PASSWORD
                    docker build -t $DOCKER_HUB_USERNAME/cicd_practice:latest .
                    docker push $DOCKER_HUB_USERNAME/cicd_practice:latest
                    docker logout
                '''
            }
        }
    }
}
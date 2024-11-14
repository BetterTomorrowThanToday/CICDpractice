pipeline {
    agent any
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
                    docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD
                    docker build -t $DOCKERHUB_USERNAME/cicd_practice:latest .
                    docker push $DOCKERHUB_USERNAME/cicd_practice:latest
                    docker logout
                '''
            }
        }
    }
}
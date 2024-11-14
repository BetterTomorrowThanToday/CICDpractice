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

        stage("Set Variables") {
            steps {
                echo "Set Variables"

                script {
                    withCredentials([usernamePassword(credentialsId: forDockerhub,
                                                               passwordVariable: 'DOCKER_HUB_PASSWORD',
                                                               usernameVariable: 'DOCKER_HUB_USERNAME')]) {

                                    // Docker Hub 로그인
                                    sh "docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}"

                                    // Docker 이미지 빌드
                                    sh "docker build -t ${DOCKER_HUB_USERNAME}/cicd_practice:latest ."

                                    // Docker Hub에 이미지 푸시
                                    sh "docker push ${DOCKER_HUB_USERNAME}/cicd_practice:latest"

                                    // Docker 로그아웃 (보안을 위해)
                                    sh "docker logout"
                                }
                }
            }
        }
    }
}
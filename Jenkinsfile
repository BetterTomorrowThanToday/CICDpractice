pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('forDockerhub') // Docker Hub 자격 증명
        DOCKERHUB_REPO = "hikiki/cicd_practice" // Docker Hub 저장소
        IMAGE_TAG = "latest" // Docker 이미지 태그 (예: "latest" 또는 "${env.BUILD_NUMBER}")
    }
    stages {
        stage("Permission") {
            steps {
                // Gradle Wrapper 실행 권한 부여
                sh "chmod +x ./gradlew"
            }
        }

        stage("Compile") {
            steps {
                // Java 소스 코드 컴파일
                sh "./gradlew compileJava"
            }
        }
        stage('Build') {
            steps {
                // Gradle 빌드를 실행
                sh './gradlew build'
            }
        }

        stage("Unit Test") {
            steps {
                // 유닛 테스트 실행
                sh "./gradlew test"
            }
            post {
                always {
                    // 테스트 결과 보고서 생성
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage("Code Coverage") {
            steps {
                // JaCoCo 테스트 커버리지 검증
                sh "./gradlew jacocoTestCoverageVerification"
                // JaCoCo 테스트 보고서 생성
                sh "./gradlew jacocoTestReport"
                // HTML 형식의 JaCoCo 커버리지 리포트 게시
                publishHTML(target: [
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'Jacoco Report'
                ])
            }
        }

        stage("Static Code Analysis") {
            steps {
                // Checkstyle 코드 스타일 검증
                sh "./gradlew checkstyleMain"
                // HTML 형식의 Checkstyle 리포트 게시
                publishHTML(target: [
                    reportDir: 'build/reports/checkstyle/',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }

        stage("Build Docker Image") {
            steps {
                // Docker 이미지 빌드
                echo 'Building Docker image...'
                sh """
                docker build -t ${DOCKERHUB_REPO}:${IMAGE_TAG} .
                """
            }
        }

        stage("Push Docker Image to Docker Hub") {
            steps {
                // Docker Hub에 로그인하고 이미지 푸시
                echo 'Pushing Docker image to Docker Hub...'
                sh """
                    echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                    docker push ${DOCKERHUB_REPO}:${IMAGE_TAG}
                """
            }
        }
    }
}
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'java-api-boilerplate'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        ECR_REPO = 'your-ecr-repo/java-api-boilerplate'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }

        stage('Push to ECR') {
            steps {
                sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${ECR_REPO}"
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${ECR_REPO}:${DOCKER_TAG}"
                sh "docker push ${ECR_REPO}:${DOCKER_TAG}"
            }
        }

        stage('Deploy to Dev') {
            when {
                branch 'develop'
            }
            steps {
                sh "helm upgrade --install java-api-boilerplate-dev ./helm/dev --set image.tag=${DOCKER_TAG} --namespace dev"
            }
        }

        stage('Deploy to HML') {
            when {
                branch 'release'
            }
            steps {
                sh "helm upgrade --install java-api-boilerplate-hml ./helm/hml --set image.tag=${DOCKER_TAG} --namespace hml"
            }
        }

        stage('Deploy to Prod') {
            when {
                branch 'master'
            }
            steps {
                sh "helm upgrade --install java-api-boilerplate-prod ./helm/prod --set image.tag=${DOCKER_TAG} --namespace prod"
            }
        }
    }

    post {
        always {
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

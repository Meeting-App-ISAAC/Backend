pipeline {
  agent any
  options {
    disableConcurrentBuilds()
    timeout(time: 10, unit: 'MINUTES')
  }
  triggers {
    //cron('H 7-12 * * *')
    gitlab(
      triggerOnPush: true,
      triggerOnMergeRequest: true    
    )
  }
  stages {
    stage('Verify Tools') {
      steps {
        parallel (
          java: {
            sh 'java -version'
            sh 'which java'
          },
          maven: {
            sh 'mvn -version'
            sh 'which mvn'
          },
          docker: {
            sh 'docker --version'
            sh 'which docker'
          }
        )
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean install -B -DskipTests'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }
    stage('Deploy dockerbranch') {
           when {
             branch 'dockerbranch'
           }
           steps {
             sh 'docker build -t isaak-backend .'
             sh 'docker rm -f isaak-backend-prod || true'
             sh 'docker run -d -p 8092:8092 -e --name isaak-backend-prod isaak-backend --restart always '
             sh 'docker image prune -f'
           }
         }
  }
  post {
    always {
      cleanWs()
    }
  }
}

pipeline {

  agent any

  environment {
    // Point Gradle to a project-local cache directory
    GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
  }

  options {
    // Keep only the last 10 builds for storage hygiene
    buildDiscarder(logRotator(numToKeepStr: '10'))
    // Give each stage a timeout so hung steps can’t block the queue
    timeout(time: 30, unit: 'MINUTES')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Run API Tests') {
      steps {
        sh './gradlew testApi'
      }
    }

    stage('Run Web UI Tests') {
      steps {
        sh './gradlew testWeb'
      }
    }

    stage('Run General Tests') {
      steps {
        sh './gradlew test'
      }
    }
  }

  post {
    always {
      // Archive your Cucumber and JUnit report artifacts
      archiveArtifacts artifacts: 'build/reports/cucumber/testweb/cucumber-web.html', fingerprint: true
      archiveArtifacts artifacts: 'build/reports/cucumber/testapi/cucumber-api.html', fingerprint: true
      archiveArtifacts artifacts: 'build/reports/tests/**', fingerprint: true

      // You can also publish JUnit results so they show up in the Jenkins UI
      junit 'build/test-results/**/*.xml'
    }
  }
}

pipeline {
    agent any

    tools {
        jdk 'JDK-11'
        maven 'Maven-3.5'
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        retry(3)
    }

    stages {
        stage('Prepare') {
            steps {
                // Get code from GitHub repository
                git branch: 'develop', url: 'https://github.com/afbustamante/y-a-foot'
            }
        }
        stage('Build') {
            steps {
                // Run the maven build
                sh 'mvn -DskipTests=false -Dmaven.test.failure.ignore clean install -P jenkins --batch-mode --errors --fail-at-end'
            }
        }
        stage('Analyze') {
            steps {
                // Run the Sonar analysis
                sh 'mvn sonar:sonar -P jenkins -Dsonar.organization=afbustamante-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=f141b07519c6a6eb8ac0e400c56cfdabb1775cdc'
            }
        }
        stage('Report') {
            steps {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }
}
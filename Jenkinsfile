pipeline {
    agent any

    tools {
        jdk 'JDK-14'
        maven 'Maven-3.8'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Prepare') {
            steps {
                // Get code from GitHub repository
                echo 'Pulling branch ' + env.GIT_BRANCH
            }
        }
        stage('Build') {
            steps {
                // Run the maven build without tests
                sh 'mvn clean install -P jenkins -DskipTests=true'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -P jenkins --batch-mode --errors --fail-at-end'
            }
        }
        stage('Analyze') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        // Run the Sonar analysis
                        sh 'mvn sonar:sonar -P jenkins -Dsonar.organization=afbustamante-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=f141b07519c6a6eb8ac0e400c56cfdabb1775cdc'
                    } else {
                        echo 'Skipped Sonar analysis'
                    }
                }
            }
        }
        stage('Report') {
            steps {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }
}
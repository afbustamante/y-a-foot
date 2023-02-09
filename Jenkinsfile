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
                // Run the maven build with tests
                sh 'mvn clean install -P jenkins --batch-mode --errors --fail-at-end'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'

                    withChecks('Integration Tests') {
                        junit '**/target/failsafe-reports/TEST-*.xml'
                    }
                }
            }
        }
        stage('Analyze') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        // Run the Sonar analysis
                        configFileProvider([configFile(fileId: '8d47e8c5-f619-4f36-a1dc-590dca78adb1', variable: 'SONAR_CONFIG')]) {
                            // some block
                            def props = readProperties file: "${SONAR_CONFIG}"
                            sh "mvn sonar:sonar -P jenkins -Dsonar.login=${props['sonar.login']}"
                        }
                    } else {
                        echo 'Skipped Sonar analysis'
                    }
                }
            }
        }
        stage('Publish') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        // Generate the updated site for the project
                        sh 'mvn site:site site:deploy -P jenkins -pl !y-a-foot-commons-tools'
                    } else {
                        echo 'No site files generated for this branch'
                    }
                }
            }
        }
    }
}
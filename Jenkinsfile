pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9'
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
                sh 'mvn clean'
            }
        }

        stage('Validate') {
            steps {
                sh 'mvn clean install -P jenkins -pl y-a-foot-commons-tools'
                sh 'mvn validate -P jenkins'
            }
        }

        stage('Build') {
            steps {
                // Run the maven build without tests
                sh 'mvn compile -P jenkins'
            }
        }

        stage('Test') {
            steps {
                // Run the maven build with tests
                sh 'mvn test -P jenkins'
                sh 'mvn verify -P jenkins'
            }
            post {
                always {
                    junit '**/target/*-reports/TEST-*.xml'
                }
            }
        }

        stage('Report') {
            steps {
                script {
                    sh 'mvn site -P jenkins -pl !y-a-foot-commons-tools'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/site/', fingerprint: true
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
                            sh "mvn sonar:sonar -P jenkins -Dsonar.host.url=${props['sonar.host.url']} -Dsonar.login=${props['sonar.login']} -Dsonar.organization=${props['sonar.organization']}"
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
                        // Publish the updated site for the project
                        sh 'mvn site:deploy -P jenkins -pl !y-a-foot-commons-tools'
                    } else {
                        echo 'No site files published for this branch'
                    }
                }
            }
        }
    }
}
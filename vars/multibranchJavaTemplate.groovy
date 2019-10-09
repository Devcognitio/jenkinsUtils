def call(body) {

    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()


    pipeline {
        agent any
        triggers { pollSCM('* * * * *') }
        environment {
            ARTIFACTORY_SERVER = 'DEVOPS-ARTIFACTORY'
            
            ARTIFACT_NAME = "${pipelineParams.artifact.name}"
        }

        stages {

            stage('build') {
                steps {
                    echo "CommitHash: ${env.GIT_COMMIT}"
                    sh "git branch tmp-promotion-${POM_VERSION} ${env.GIT_COMMIT}"
                    sh './gradlew clean package -DskipTests=true'
                }
                post{
                    success {
                        archiveArtifacts artifacts: "${pipelineParams.artifact.path}/*.${pipelineParams.artifact.name}", fingerprint: true
                    }
                }
            }

            stage ('test') {
                parallel {
                    stage('unit tests'){
                        steps{
                            sh './gradlew test'
                        }
                        post {
                            always {
                                junit pipelineParams.jUnitTestReport
                            }
                        }
                    }
                    stage('static analysis') {
                        steps{
                            withSonarQubeEnv('SonarQubeServer') {
                                script{
                                    sh "./gradlew sonar:sonar"

                                    timeout(time: 1, unit: 'HOURS') {
                                        def qg = waitForQualityGate()
                                        if (qg.status != 'OK') {
                                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                                        }
                                    }
                                }
                            }

                        }
                    }
                }


            }

            stage('deploy'){
                when{
                    anyOf { branch 'develop'; branch 'release'; branch 'master' }
                }
                environment {
                    ENVIRONMENT = "${env.BRANCH_NAME == "develop" ? "DEVELOPMENT" : "QUALITY"}"                    
                }
                steps {
                    echo "DEPLOY en: ${env.ENVIRONMENT} para ${env.ARTIFACT_NAME}"
                }
            }

            stage('post deploy test'){
                when{
                    anyOf { branch 'develop'; branch 'release'; branch 'master' }
                }
                environment {
                    EXECUTE_INTEGRATION_TEST = "${env.BRANCH_NAME == "develop" ? pipelineParams.integrationTest.executeDllo : pipelineParams..integrationTest.executeQa}"
                    EXECUTE_E2E_TEST = "${env.BRANCH_NAME == "develop" ? pipelineParams.e2eTest.executeDllo : pipelineParams..e2eTest.executeQa}"
                }
                parallel{
                    stage('postman test') {
                        when{
                            expression{
                                EXECUTE_INTEGRATION_TEST == 'true'
                            }
                        }
                        steps{
                            echo "POST DEPLOY TEST en: ${env.ENVIRONMENT} para ${env.ARTIFACT_NAME}"                            
                        }
                    }
                    stage('e2e test') {
                        when{
                            expression{
                                EXECUTE_E2E_TEST == 'true'
                            }
                        }
                        steps{
                            dir ('e2e') {
                                echo "PRUEBAS E2E en: ${env.ENVIRONMENT} para ${env.ARTIFACT_NAME}"
                            }
                        }
                    }
                }
            }

            stage('publish artifact'){
                when{
                    anyOf { branch 'develop'; branch 'release'; branch 'master' }
                }
                steps {
                    script{
                        String artifactoryPath= artifactory.getStage(env.BRANCH_NAME, pipelineParams.artifact.repo_name)
                        artifactory.push(env.ARTIFACTORY_SERVER, pipelineParams.artifactPath, artifactoryPath)
                    }
                }
            }

            stage('input') {
                agent none
                options {
                    timeout(time: 1, unit: 'HOURS')
                }
                steps{
                    input 'Desea realizar la promoción al siguiente ambiente?'
                }
            }

            stage('git promotion'){
                when{
                    anyOf { branch 'develop'; branch 'release'; branch 'master' }
                }
                steps{
                    script{
                        if (env.BRANCH_NAME == 'develop'){
                            echo 'Promotion de develop'
                            sh 'git reset --hard'
                            sh 'git status'
                            sh 'git fetch'
                            sh 'git checkout origin/release'
                            sh "git merge tmp-promotion-${POM_VERSION}"
                            withCredentials([usernamePassword(credentialsId: 'GitHub-Devco-Repos', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                                sh("git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${env.GIT_URL.substring(8)} HEAD:release")
                            }
                        }else if (env.BRANCH_NAME == 'release'){
                            echo 'Promotion de release'
                            sh 'git reset --hard'
                            sh 'git status'
                            sh 'git fetch'
                            sh 'git checkout origin/master'
                            sh "git merge tmp-promotion-${POM_VERSION}"
                            withCredentials([usernamePassword(credentialsId: 'GitHub-Devco-Repos', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                                sh("git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${env.GIT_URL.substring(8)} HEAD:master")
                            }
                        }
                    }


                }
            }

        }
        post {
            failure {
                echo "ALGO FALLÓ EN EL PIPELINE"
                //mail to: pipelineParams.supportEmail,
                //        subject: "Pipeline failed: ${currentBuild.fullDisplayName}",
                //        body: "Something is wrong with ${env.BUILD_URL}"
            }
            always{
                echo "Borrando branch temporal"
                sh "git branch -d tmp-promotion-${POM_VERSION}"
            }
        }
    }
}
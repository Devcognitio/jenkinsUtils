def call(int parallelStagesAmount, String parallelStageName, String agentName, Closure functionToRun, Closure functionToRunFinally){
    def testsGen = [:]
    for (int i = 1; i <= parallelStagesAmount; i++) {
        int item = i
        testsGen["${parallelStageName} ${item}"] = {
            node(agentName) {
                try {
                    stage("${parallelStageName}  ${item}") {
                        dir("${parallelStageName}${item}")	{
                                                                
                            bat "echo ${item}"
                            bat "echo NODE_NAME = ${env.NODE_NAME}"
                                                
                            functionToRun(item)				                    
                                                                        
                        }                               		
                    }
                } finally {
                    functionToRunFinally(item)										    
                }

            }
        }
    }
    return testsGen
}
def mavenbuild() {
    sh """
        mvn clean package -Dv=${BUILD_NUMBER}
    """
}

def login() {
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'hubUsername', passwordVariable: 'hubPassword')]) {
        sh """
            docker login --username="${hubUsername}" --password="${hubPassword}"
        """
    }
}

def buildimage(String tag) {
    sh """
        docker build -t k2r2t2/demosharedlib .
    """
}

def pushimage(String tag) {
    sh """
        docker push k2r2t2/demosharedlib
    """
}

def deploy() {
    withKubeCredentials(kubectlCredentials: [[caCertificate: '', clusterName: '', contextName: '', credentialsId: 'K8S', namespace: '', serverUrl: '']]) {
                   sh """
                        kubectl apply -f deployment.yml
		        kubectl rollout restart deployment.v1.apps/demosharedlib-deployment
                    """
    }
}  

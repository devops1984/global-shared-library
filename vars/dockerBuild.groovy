def login() {
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'hubUsername', passwordVariable: 'hubPassword')]) {
        sh """
            docker login --username="${hubUsername}" --password="${hubPassword}"
        """
    }
}

def build(String tag) {
    sh """
        docker build -t "${tag}" .
    """
}

def push(String tag) {
    sh """
        docker push "${tag}"
    """
}

def deploy() {
    withKubeCredentials(kubectlCredentials: [[caCertificate: '', clusterName: '', contextName: '', credentialsId: 'K8S', namespace: '', serverUrl: '']]) {
                   sh """
                        kubectl apply -f deployment.yml
				        kubectl rollout restart deployment.v1.apps/demosharedlib-deployment
                    """
    }
        

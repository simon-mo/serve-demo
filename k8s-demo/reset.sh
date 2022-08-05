set -x

kubectl scale deploy kuberay-operator --replicas 0 -n ray-system
kubectl delete rayservice.ray.io/rayservice-sample
kubectl scale deploy kuberay-operator --replicas 1 -n ray-system

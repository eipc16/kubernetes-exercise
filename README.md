# Laboratorium 6. Docker (Technologie wspierające wytwarzanie oprogramowania)

## Opis zadania

-- opis zadania

## Środowisko

-- opis środowiska


### Ogólna architektura

```bash
# kubectl get all
NAME                            READY   STATUS    RESTARTS   AGE
pod/cta-core-6d77b645cf-9mj6v   1/1     Running   0          2m48s
pod/cta-core-6d77b645cf-jx2ct   1/1     Running   1          2m53s
pod/cta-db-6999448466-5lmxj     1/1     Running   0          2m53s

NAME                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
service/cta-core     NodePort    10.111.201.242   <none>        8081:31316/TCP   23m
service/cta-db       ClusterIP   10.97.16.95      <none>        3306/TCP         23m
service/kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP          24m

NAME                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/cta-core   2/2     2            2           23m
deployment.apps/cta-db     1/1     1            1           23m

NAME                                  DESIRED   CURRENT   READY   AGE
replicaset.apps/cta-core-6d77b645cf   2         2         2       2m54s
replicaset.apps/cta-core-7c7cb6cf4b   0         0         0       23m
replicaset.apps/cta-db-6999448466     1         1         1       2m54s
replicaset.apps/cta-db-6cc5c4b4c      0         0         0       23m
```

### Pody

W klastrze znajduja się 3 pody, dwa pody dla backendu i jeden dla bazy danych

#### cta-core - 1

```bash
Name:         cta-core-6d77b645cf-9mj6v
Namespace:    default
Priority:     0
Node:         minikube/192.168.49.2
Start Time:   Thu, 03 Dec 2020 20:21:02 +0000
Labels:       app=cta-core
              pod-template-hash=6d77b645cf
              tier=backend
              track=stable
Annotations:  <none>
Status:       Running
IP:           172.17.0.8
IPs:
  IP:           172.17.0.8
Controlled By:  ReplicaSet/cta-core-6d77b645cf
Containers:
  cta-core:
    Container ID:   docker://e673d913899afd92cb705748c323baebeb896248e5efd40eec7ea1375c762907
    Image:          cta_core:1.1
    Image ID:       docker://sha256:04aa88066a826882f95fa41b2dfd6d63745f2f206ed6ec6fb82ef4e454d0240c
    Port:           8081/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 03 Dec 2020 20:21:05 +0000
    Ready:          True
    Restart Count:  0
    Liveness:       tcp-socket :8081 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:
      CTA_DATABASE_HOST:  cta-db
      CTA_DATABASE_PORT:  3306
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-6xqkn (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  default-token-6xqkn:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  default-token-6xqkn
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                 node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  5m30s  default-scheduler  Successfully assigned default/cta-core-6d77b645cf-9mj6v to minikube
  Normal  Pulled     5m29s  kubelet            Container image "cta_core:1.1" already present on machine
  Normal  Created    5m28s  kubelet            Created container cta-core
  Normal  Started    5m28s  kubelet            Started container cta-core
```

#### cta-core - 2

```bash
Name:         cta-core-6d77b645cf-jx2ct
Namespace:    default
Priority:     0
Node:         minikube/192.168.49.2
Start Time:   Thu, 03 Dec 2020 20:20:58 +0000
Labels:       app=cta-core
              pod-template-hash=6d77b645cf
              tier=backend
              track=stable
Annotations:  <none>
Status:       Running
IP:           172.17.0.7
IPs:
  IP:           172.17.0.7
Controlled By:  ReplicaSet/cta-core-6d77b645cf
Containers:
  cta-core:
    Container ID:   docker://614387f57940f2fb1778f35fc7badf09d963c5eae041d1aaefc7f35b6af6a44f
    Image:          cta_core:1.1
    Image ID:       docker://sha256:04aa88066a826882f95fa41b2dfd6d63745f2f206ed6ec6fb82ef4e454d0240c
    Port:           8081/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 03 Dec 2020 20:21:41 +0000
    Last State:     Terminated
      Reason:       Error
      Exit Code:    1
      Started:      Thu, 03 Dec 2020 20:21:01 +0000
      Finished:     Thu, 03 Dec 2020 20:21:40 +0000
    Ready:          True
    Restart Count:  1
    Liveness:       tcp-socket :8081 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:
      CTA_DATABASE_HOST:  cta-db
      CTA_DATABASE_PORT:  3306
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-6xqkn (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  default-token-6xqkn:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  default-token-6xqkn
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                 node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age                    From               Message
  ----    ------     ----                   ----               -------
  Normal  Scheduled  6m56s                  default-scheduler  Successfully assigned default/cta-core-6d77b645cf-jx2ct to minikube
  Normal  Pulled     6m14s (x2 over 6m54s)  kubelet            Container image "cta_core:1.1" already present on machine
  Normal  Created    6m14s (x2 over 6m54s)  kubelet            Created container cta-core
  Normal  Started    6m13s (x2 over 6m54s)  kubelet            Started container cta-core
```

#### cta-db

```bash
Name:         cta-db-6999448466-5lmxj
Namespace:    default
Priority:     0
Node:         minikube/192.168.49.2
Start Time:   Thu, 03 Dec 2020 20:20:58 +0000
Labels:       app=cta-db
              pod-template-hash=6999448466
              tier=database
              track=stable
Annotations:  <none>
Status:       Running
IP:           172.17.0.6
IPs:
  IP:           172.17.0.6
Controlled By:  ReplicaSet/cta-db-6999448466
Containers:
  cta-db:
    Container ID:   docker://f864f965d72a0ee52798fec0c45a75ad836308edc41f01b881d1f7abaaa835e9
    Image:          cta_db:1.1
    Image ID:       docker://sha256:8724eefd92347c5eca5d322d48790a74977b21dfac600232ff5b06779aeff4fe
    Port:           3306/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 03 Dec 2020 20:21:02 +0000
    Ready:          True
    Restart Count:  0
    Liveness:       tcp-socket :3306 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-6xqkn (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  default-token-6xqkn:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  default-token-6xqkn
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                 node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  7m40s  default-scheduler  Successfully assigned default/cta-db-6999448466-5lmxj to minikube
  Normal  Pulled     7m38s  kubelet            Container image "cta_db:1.1" already present on machine
  Normal  Created    7m38s  kubelet            Created container cta-db
  Normal  Started    7m37s  kubelet            Started container cta-db
```


### Serwisy

W klastrze znajdują się 3 seriwsy: serwis dla cta-core, serwis dla cta-db oraz główny serwis kubernetesowy

#### cta-core

```bash
Name:                     cta-core
Namespace:                default
Labels:                   <none>
Annotations:              <none>
Selector:                 app=cta-core
Type:                     NodePort
IP:                       10.111.201.242
Port:                     <unset>  8081/TCP
TargetPort:               8081/TCP
NodePort:                 <unset>  31316/TCP
Endpoints:                172.17.0.7:8081,172.17.0.8:8081
Session Affinity:         None
External Traffic Policy:  Cluster
Events:
  Type     Reason                        Age   From                       Message
  ----     ------                        ----  ----                       -------
  Warning  FailedToUpdateEndpointSlices  21m   endpoint-slice-controller  Error updating Endpoint Slices for Service default/cta-core: failed to update cta-core-dz54w EndpointSlice for Service default/cta-core: Operation cannot be fulfilled on endpointslices.discovery.k8s.io "cta-core-dz54w": the object has been modified; please apply your changes to the latest version and try again
```

#### cta-db

```bash
Name:              cta-db
Namespace:         default
Labels:            <none>
Annotations:       <none>
Selector:          app=cta-db,tier=database
Type:              ClusterIP
IP:                10.97.16.95
Port:              <unset>  3306/TCP
TargetPort:        http/TCP
Endpoints:         172.17.0.6:3306
Session Affinity:  None
Events:            <none>
```

#### kubernetes

```bash
Name:              kubernetes
Namespace:         default
Labels:            component=apiserver
                   provider=kubernetes
Annotations:       <none>
Selector:          <none>
Type:              ClusterIP
IP:                10.96.0.1
Port:              https  443/TCP
TargetPort:        8443/TCP
Endpoints:         192.168.49.2:8443
Session Affinity:  None
Events:            <none>
```

### Deploymenty

Zdefiniowano dwa deploymenty: cta-core dla backendu i cta-db dla bazy danych

#### cta-core

```bash
Name:                   cta-core
Namespace:              default
CreationTimestamp:      Thu, 03 Dec 2020 20:00:30 +0000
Labels:                 <none>
Annotations:            deployment.kubernetes.io/revision: 2
Selector:               app=cta-core,tier=backend,track=stable
Replicas:               2 desired | 2 updated | 2 total | 2 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  app=cta-core
           tier=backend
           track=stable
  Containers:
   cta-core:
    Image:      cta_core:1.1
    Port:       8081/TCP
    Host Port:  0/TCP
    Liveness:   tcp-socket :8081 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:
      CTA_DATABASE_HOST:  cta-db
      CTA_DATABASE_PORT:  3306
    Mounts:               <none>
  Volumes:                <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Progressing    True    NewReplicaSetAvailable
  Available      True    MinimumReplicasAvailable
OldReplicaSets:  <none>
NewReplicaSet:   cta-core-6d77b645cf (2/2 replicas created)
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  31m   deployment-controller  Scaled up replica set cta-core-7c7cb6cf4b to 2
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled up replica set cta-core-6d77b645cf to 1
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled down replica set cta-core-7c7cb6cf4b to 1
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled up replica set cta-core-6d77b645cf to 2
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled down replica set cta-core-7c7cb6cf4b to 0

```

#### cta-db

```bash
Name:                   cta-db
Namespace:              default
CreationTimestamp:      Thu, 03 Dec 2020 20:00:30 +0000
Labels:                 <none>
Annotations:            deployment.kubernetes.io/revision: 2
Selector:               app=cta-db,tier=database,track=stable
Replicas:               1 desired | 1 updated | 1 total | 1 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  app=cta-db
           tier=database
           track=stable
  Containers:
   cta-db:
    Image:        cta_db:1.1
    Port:         3306/TCP
    Host Port:    0/TCP
    Liveness:     tcp-socket :3306 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
  Progressing    True    NewReplicaSetAvailable
OldReplicaSets:  <none>
NewReplicaSet:   cta-db-6999448466 (1/1 replicas created)
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  32m   deployment-controller  Scaled up replica set cta-db-6cc5c4b4c to 1
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled up replica set cta-db-6999448466 to 1
  Normal  ScalingReplicaSet  11m   deployment-controller  Scaled down replica set cta-db-6cc5c4b4c to 0
```

### ReplicaSety

Komenda `kubectl get all` informuje, że istnieją 4 obiekty typu `ReplicaSet`, dwa z nich są jednak powiązane z podami, które zostały wcześniej zaterminowane i zastąpione obecnie uruchomionymi

#### cta-core

```bash
Name:           cta-core-6d77b645cf
Namespace:      default
Selector:       app=cta-core,pod-template-hash=6d77b645cf,tier=backend,track=stable
Labels:         app=cta-core
                pod-template-hash=6d77b645cf
                tier=backend
                track=stable
Annotations:    deployment.kubernetes.io/desired-replicas: 2
                deployment.kubernetes.io/max-replicas: 3
                deployment.kubernetes.io/revision: 2
Controlled By:  Deployment/cta-core
Replicas:       2 current / 2 desired
Pods Status:    2 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=cta-core
           pod-template-hash=6d77b645cf
           tier=backend
           track=stable
  Containers:
   cta-core:
    Image:      cta_core:1.1
    Port:       8081/TCP
    Host Port:  0/TCP
    Liveness:   tcp-socket :8081 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:
      CTA_DATABASE_HOST:  cta-db
      CTA_DATABASE_PORT:  3306
    Mounts:               <none>
  Volumes:                <none>
Events:
  Type    Reason            Age   From                   Message
  ----    ------            ----  ----                   -------
  Normal  SuccessfulCreate  13m   replicaset-controller  Created pod: cta-core-6d77b645cf-jx2ct
  Normal  SuccessfulCreate  13m   replicaset-controller  Created pod: cta-core-6d77b645cf-9mj6v
```

#### cta-db

```bash
Name:           cta-db-6999448466
Namespace:      default
Selector:       app=cta-db,pod-template-hash=6999448466,tier=database,track=stable
Labels:         app=cta-db
                pod-template-hash=6999448466
                tier=database
                track=stable
Annotations:    deployment.kubernetes.io/desired-replicas: 1
                deployment.kubernetes.io/max-replicas: 2
                deployment.kubernetes.io/revision: 2
Controlled By:  Deployment/cta-db
Replicas:       1 current / 1 desired
Pods Status:    1 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=cta-db
           pod-template-hash=6999448466
           tier=database
           track=stable
  Containers:
   cta-db:
    Image:        cta_db:1.1
    Port:         3306/TCP
    Host Port:    0/TCP
    Liveness:     tcp-socket :3306 delay=600s timeout=1s period=10s #success=1 #failure=3
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Events:
  Type    Reason            Age   From                   Message
  ----    ------            ----  ----                   -------
  Normal  SuccessfulCreate  14m   replicaset-controller  Created pod: cta-db-6999448466-5lmxj
```

## Podsumowanie

-- podsumowanie

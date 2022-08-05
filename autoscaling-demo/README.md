here’s a simple way to play with autoscaling.

- run a model that time.sleep(1)
- use any load testing tool  to generate constant throughput. i used brew install vegeta: echo "GET http://localhost:8000/A" | vegeta attack -rate=3/s | vegeta encode
- observe in the ray dashboard the replica as about the same number of replicas as the throughput. and you can change the rate in load generator.


```
echo "GET http://localhost:8000/A" | vegeta attack -rate=3/s | vegeta encode
```

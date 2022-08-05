from ray import serve
import time

@serve.deployment
class A:
    def __init__(self):
        print("new replica")

    def __call__(self):
        time.sleep(1)
        return "hi"


config = {
    "min_replicas": 0,
    "max_replicas": 10,
    "target_num_ongoing_requests_per_replica": 1,
    
    # Speeding up for demo purpose.
    "downscale_delay_s": 8,
    "upscale_delay_s": 8,
    "look_back_period_s": 6,
    "metrics_interval_s": 2
}

a = A.options(autoscaling_config=config).bind()

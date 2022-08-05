from locust import HttpUser, task

class ServeUser(HttpUser):
    @task
    def test_request(self):
        self.client.post("/", json=["MANGO", 2])

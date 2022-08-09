import json

class SumModel:
    def __call__(self, inp_json: str) -> str:
        data = json.loads(inp_json)
        summed = {k: sum(v) for k, v in data.items()}
        return json.dumps(summed)
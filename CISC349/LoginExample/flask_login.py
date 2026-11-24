from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/', methods=["POST"])
def index():
    request_data = request.get_json()
    print(request_data)
    return "Successful"     



if __name__ == '__main__':
    app.run("0.0.0.0")

# curl -X POST http://localhost:5000/
#    -H "Content-Type: application/x-www-form-urlencoded"
#    -d "param1=value1&param2=value2"
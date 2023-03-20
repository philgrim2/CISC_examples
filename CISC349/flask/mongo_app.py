import json
from pymongo import MongoClient  
from flask import Flask
from flask import request
from flask.json import jsonify

app = Flask(__name__)

client = MongoClient('mongodb+srv://pgrim:<password>@cisc349.aa5oxv8.mongodb.net/?retryWrites=true&w=majority')
db = client["CISC349"]
 

# A welcome message to test our server
@app.route('/')
def index():
    return "<h1>Welcome to our server!!</h1>"


# Add user
@app.route('/add', methods=['POST'])
def add():
    collection = db["customers"]
    request_data = request.get_json()
    name = request_data['name']
    address = request_data['address']
    print(f'Name: {name}, Address: {address}')
    data = { "name": name, "address": address }
    _id = collection.insert_one(data) 
    return json.dumps({'id' : str(_id.inserted_id)})

# Select All users

@app.route('/all', methods=['POST'])
def all():
    collection = db["customers"] 
    customers = list(collection.find())
    # we need to convert _id to str.
    return json.dumps(customers, default=str)



if __name__ == "__main__":
    app.run(port=5000)
    
    
#
# curl --header 'Content-Type: application/json' --data "{ \"name\": \"Phil Grim\", \"address\": \"214 Pine Woods Rd, Wellsville PA 17365\"}" http://localhost:5000/add
#
#curl -X POST localhost:5000/add -H "Content-Type: application/json" -d "{ \"name\"": \"Phil Grim\", \"address\": \"214 Pine Woods Rd, Wellsville PA 17365\"}"
#
#curl -X POST -d '{ "name": "Phil Grim", "address": "214 Pine Woods Rd, Wellsville PA 17365"}' -H 'Content-Type: application/json' localhost:5000/add

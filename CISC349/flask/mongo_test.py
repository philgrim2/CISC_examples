import json
from pymongo import MongoClient  
import certifi


client = MongoClient('mongodb+srv://pgrim:<password>@cisc349.aa5oxv8.mongodb.net/?retryWrites=true&w=majority',tlsCAFile=certifi.where())
db = client["CISC349"]
 

def add(name, address):
    collection = db["customers"]
    print(f'Name: {name}, Address: {address}')
    data = { "name": name, "address": address }
    _id = collection.insert_one(data) 
    return json.dumps({'id' : str(_id.inserted_id)})

# Select All users

def all():
    collection = db["customers"] 
    customers = list(collection.find())
    print(customers)



if __name__ == "__main__":
    add("Phil Grim","214 Pine Woods Rd, Wellsville, PA  17365")
    all()

    
#
# curl --header 'Content-Type: application/json' --data "{ \"name\": \"Phil Grim\", \"address\": \"214 Pine Woods Rd, Wellsville PA 17365\"}" http://localhost:5000/add
#
#curl -X POST localhost:5000/add -H "Content-Type: application/json" -d "{ \"name\"": \"Phil Grim\", \"address\": \"214 Pine Woods Rd, Wellsville PA 17365\"}"
#
#curl -X POST -d '{ "name": "Phil Grim", "address": "214 Pine Woods Rd, Wellsville PA 17365"}' -H 'Content-Type: application/json' localhost:5000/add

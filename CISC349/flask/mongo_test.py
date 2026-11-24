import json
from pymongo import MongoClient  
import certifi


client = MongoClient('mongodb+srv://pgrim:plmnkoij@cisc349.aa5oxv8.mongodb.net/?retryWrites=true&w=majority',tlsCAFile=certifi.where())
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
    add("Virginia Grim","214 Pine Woods Rd, Wellsville, PA  17365")
    all()

    

from flask import Flask, request
import json

app = Flask("Login")

@app.route('/login', methods=['POST'])
def login():
   request_data = request.get_json()
   username = request_data['username']
   password = request_data['password']

   print(f'Username: {username}, Password: {password}')

   success = (username == 'pgrim') and (password == 'p@$$w0rd')

   return json.dumps({'login': success})

if __name__ == '__main__':
   app.run('0.0.0.0',5000,True)
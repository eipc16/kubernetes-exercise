#!/bin/python
import json
import requests

url = 'http://10.104.78.104:31316/cinema-tickets-app/api/genres'
response_json = requests.get(url).json()

print('Found genres:')

for genre in response_json:
    id = genre['id']
    name = genre['name']
    print(f"Id: {id}, Name: {name}")
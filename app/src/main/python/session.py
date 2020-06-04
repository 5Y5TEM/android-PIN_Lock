from telethon.sync import TelegramClient
from telethon.sessions import StringSession
import pandas as pd
import json

login_data = pd.read_csv('login.csv', sep=';')
session = []
for i in range(len(login_data)):
    api_id = login_data['api_id'][i]
    api_hash = login_data['api_hash'][i]
    with TelegramClient(StringSession(), api_id, api_hash) as client:
        s = client.session.save()
        data = {'api_id':int(api_id), "api_hash":api_hash,"session_string":s}
        session.append(data)

with open("StringSession.json", 'w') as ct:
    json.dump(session, ct)
    print("Done")

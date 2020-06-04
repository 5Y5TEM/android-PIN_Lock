# Import Module Section.
from telethon.sync import TelegramClient
import pandas as pd
import time
from telethon.errors import (UsernameInvalidError, UsernameNotModifiedError, UsernameOccupiedError,
                             HashInvalidError, AuthKeyError)
from telethon.tl.functions.account import (UpdateUsernameRequest, GetAuthorizationsRequest,
                                           ResetAuthorizationRequest)
from telethon.sessions import StringSession


# Funtion Defination Section.

# login to telegram session.
def is_logged_in(id, hash, session_string):
    try:
        print(f"{id}, {hash}")
        client = TelegramClient(StringSession(session_string), id, hash)
        client.connect()

        if not client.is_user_authorized():
            print("String Session is not Authorized.")
            raise
        print('Logged in successfully.')
        return client
    except:
        print(f"Error occured while login to {id}. Please check out String Session.")
        return None

# Get login credentials from csv.
try:
    login_data = pd.read_json("StringSession.json")
except:
    print("Error in StringSession.json file. Check it out.")

# Logging to session
for i in range(len(login_data)):
    client = is_logged_in(login_data['api_id'][i], login_data['api_hash'][i], login_data['session_string'][i])
    username = client.get_me().username
    res = client(GetAuthorizationsRequest())


    for j in res.authorizations:
        try:
            print(j.hash)
            result = client(ResetAuthorizationRequest(hash=j.hash))
            print(result)
        except:
            pass
    print(f"All session terminated of \"{username}\" user.")
    time.sleep(1)


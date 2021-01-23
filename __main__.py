import random
from lib import *
from tkinter import *
import tkinter as tk
import threading


# Gonna scramble from unicode 32-1032

#TODO:add GUI

win=tk.Tk()

win.title("Encoder")
win.minsize(400,350)


datafile=open("data.txt","r+")
data=datafile.readlines()

try:# checking if this is first time usage, in which case this will throw error as file is empty
    public_key=int(data[0])
    private_key=int(data[1])
    print('Loaded previous session')
except:
    name = input('Email: ')
    password = input('Password: ')

    public_key=intify(superEncode(name))
    private_key=intify(superEncode(name+password))
    datafile.writelines(str(public_key)+'\n'+str(private_key))

print(public_key)
print(private_key)


end=False


def encodeInput():
    person=input('Input email of the person you want to talk to')
    msgBox= takeInput('Input your message:',win, person)

def encodeMessage(message, person):
    print('sending '+message+' to '+person)
    completing_command.set()

completing_command=threading.Event()

while not(end):#todo gui stuff once ive done the rest
    command=input("Input:")
    if command.lower()=='end':
        end=True
    elif command.lower()=='encode':
        thread = threading.Thread(target=encodeInput)
        thread.start()

        completing_command.wait()

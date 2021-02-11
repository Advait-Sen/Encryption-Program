import random
from lib import *
import tkinter as tk
from huffman_compression import huffman_encode, huffman_decode
from vinegere_encoding import *

# Gonna make a bunch of encoding methods: vinegere, huffman compression (not encoding strictly, but kinda is)

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
    return takeInput('Input your message:',win, person)

def encodeMessage(message = '', tk = takeInput):
    message = message.removesuffix('\n') # Cos tkinter adds a newline at the end for some reason and it breaks everything
    [huffman_message, huffman_key] = huffman_encode(message)
    
    encoded_message = huffman_key+ '\n\n' + vinegere_encode(huffman_message, huffman_key, tk.recipient)
    print('Encoded message: \n'+encoded_message)
    
    #decoding stuff, todo move to separate function
    
    decoded_list = encoded_message.split('\n')
    
    decoding_key = decoded_list[0]
    decoded_message = ''
    for i in range(len(decoded_list)-2):
        decoded_message+=decoded_list[i+2]
    
    print('Decoded message: ' + huffman_decode(vinegere_decode(decoded_message, decoding_key, tk.recipient), decoding_key))
    
    tk.frame.destroy()

while not(end):#todo gui stuff once ive done the rest
    command=input("Input:")
    if command.lower()=='end':
        win.destroy()
        end=True
    elif command.lower()=='encode':
        task = encodeInput()
        task.frame.wait_window()


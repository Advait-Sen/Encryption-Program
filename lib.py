import hashlib
from tkinter import Frame, Label, Text, Button
import __main__ as main

def intify(str=""):
    word=[c for c in str]
    if len(word)==0:
        return 0
    ret=ord(word[0])
    for i in range(len(word)-1):
        ret*=10
        ret+=ord(word[i+1])
    return ret

def dec_to_base(num,base =10):  #Maximum base - 36
    if base==10:
        return base
    base_num = ""
    while num>0:
        dig = int(num%base)
        if dig<10:
            base_num += str(dig)
        else:
            base_num += chr(ord('A')+dig-10)  #Using uppercase letters
        num //= base
    base_num = base_num[::-1]  #To reverse the string
    return base_num

def sha512(string=""):
    hs = hashlib.sha512()
    hs.update(string.encode())
    
    return str(int(hs.hexdigest(),16))

def sha256(string=""):
    hs = hashlib.sha256()
    hs.update(string.encode())
    
    return str(int(hs.hexdigest(),16))

def superEncode(string=""):
    return sha512(sha256(str(intify(sha512(string)))))

class takeInput(object):
    
    def __init__(self,requestMessage, tk, person):
        self.root = tk
        self.request=requestMessage
        self.recipient=person
        self.frame = Frame(self.root)
        self.frame.pack()        
        self.acceptInput()

    def acceptInput(self):
        r = self.frame
        self.waiting=True
        k = Label(r,text=self.request)
        k.pack(side='left')
        self.e = Text(r)
        self.e.pack(side='left')
        self.e.focus_set()
        b = Button(r,text='enter',command=self.getText)
        b.pack(side='right')

    def getText(self):
        main.encodeMessage(self.e.get('1.0','end'),self)
        self.frame.destroy()


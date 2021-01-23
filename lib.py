import hashlib
from tkinter import *
import __main__ as main

g=282755483533707287054752184321121345766861480697448703443857012153264407439766013042402571
n=370332600450952648802345609908335058273399487356359263038584017827194636172568988257769601*14759984361802021245410475928101669395348791811705709117374129427051861355011151

def intify(str=""):
    word=[c for c in str]
    ret=0
    ret+=ord(word[0])
    for i in range(len(word)-1):
        ret*=10
        ret+=ord(word[i+1])
    return ret

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
        self.e = Entry(r,text='Name')
        self.e.pack(side='left')
        self.e.focus_set()
        b = Button(r,text='enter',command=self.getText)
        b.pack(side='right')

    def getText(self):
        main.encodeMessage(self.e.get(),self.recipient)
        self.frame.destroy()


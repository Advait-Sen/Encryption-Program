from random import randrange, seed
from lib import intify

def vinegere_encode(message, key="secret", key2 = "secret2"):
    message = [c for c in message]
    keylist = [c for c in str(key)]
    key2list = [c for c in str(key2)]
    
    encoded_message=''
    
    for i in range(len(message)):
        seed((ord(keylist[i%len(keylist)])*i**(ord(key2list[i%len(key2list)])*i).bit_length()))
        encoded_message+=chr((ord(message[i])+randrange(0, 1000)+(intify(key+key2)*(intify(key)+intify(key2))%1000))%1000)
    
    return encoded_message

def vinegere_decode(message, key = "secret", key2 = "secret2"):
    message = [c for c in message]
    keylist = [c for c in str(key)]
    key2list = [c for c in str(key2)]
    
    decoded_message=''
    
    for i in range(len(message)):
        seed((ord(keylist[i%len(keylist)])*i**(ord(key2list[i%len(key2list)])*i).bit_length()))
        decoded_message+=chr((ord(message[i])-randrange(0, 1000) - (intify(key+key2)*(intify(key)+intify(key2))%1000))%1000)

    return decoded_message

message = 'Hello, how are you doing?'

code = vinegere_encode(vinegere_encode(message,'idk'), 'idk')
print(code)
print(vinegere_decode(vinegere_decode(code,'idk'), 'idk'))

from random import randrange, seed
from lib import intify


def vinegere_encode(message, key="secret", key2="my secret message"):
    message = [c for c in message]
    key1list = [c for c in str(key)]
    key2list = [c for c in str(key2)]

    encoded_message = ''

    for i in range(len(message)):
        seed((ord(key1list[i % len(key1list)]) * i ** (ord(key2list[i % len(key2list)]) * i).bit_length()) + (
                    intify(key + key2) * (intify(key) + intify(key2)) % 1009))
        encoded_message += chr(ord(message[i]) + randrange(0, 1009))

    return encoded_message


def vinegere_decode(message, key="secret", key2=""):
    message = [c for c in message]
    key1list = [c for c in str(key)]
    key2list = [c for c in str(key2)]

    decoded_message = ''

    for i in range(len(message)):
        seed((ord(key1list[i % len(key1list)]) * i ** (ord(key2list[i % len(key2list)]) * i).bit_length()) + (
                    intify(key + key2) * (intify(key) + intify(key2)) % 1009))
        decoded_message += chr(ord(message[i]) - randrange(0, 1009))

    return decoded_message

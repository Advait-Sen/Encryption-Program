import utils

from encoders import Huffman


def main():
    print("Encryption Program Running")

    message_input = ""

    while message_input.lower() != "end":
        message_input = utils.get_input("What do you want to do?").lower()

        if message_input == "encrypt":
            encoding = utils.get_input("Please input what encryption method you want to use").lower()

            if encoding == "vignere":
                pass
            elif encoding == "huffman":
                message = utils.get_input("Please input the message you want to encrypt:")
                huff = Huffman()
                print(huff.encode(message))
                pass
            elif encoding == "master":
                pass
            else:
                print(f"Unknown encryption method '{encoding}'\n")

        elif message_input == "decrypt":
            decoding = utils.get_input("Please input what encryption method you want to use").lower()

            if decoding == "vignere":
                pass
            elif decoding == "huffman":
                pass
            elif decoding == "master":
                pass
            else:
                print(f"Unknown encryption method '{decoding}'\n")
                pass
            pass
        elif message_input != "end":
            print(f"Unknown action '{message_input}'")
        pass

    pass


if __name__ == "__main__":
    main()

from abc import ABC, abstractmethod

from utils import CharTree


class Encoder(ABC):

    @abstractmethod
    def encode(self, message=""):
        pass

    @abstractmethod
    def decode(self, encoded_message=""):
        pass

    pass


class Huffman(Encoder):

    def encode(self, message=""):
        char_list = list(message)
        char_frequency = {}

        for message_char in char_list:
            char_frequency.setdefault(message_char, 0)
            char_frequency[message_char] += 1

        sorted_characters = list(char_frequency.keys())
        sorted_characters.sort(key=char_frequency.get, reverse=True)
        sorted_characters = list(map(lambda x: CharTree(x), sorted_characters))

        child_buffer = []

        while len(sorted_characters) > 9:
            for i in range(10):
                child_buffer.append(sorted_characters.pop())
            sorted_characters.append(CharTree(child_buffer))
            child_buffer.clear()
            sorted_characters.sort(key=CharTree.get_frequency, reverse=True)
            pass

        huffman_tree = CharTree(sorted_characters)

        encoded_string = str(huffman_tree).replace("\n", "\\n") + '\n\n'

        for message_char in char_list:
            encoded_string += huffman_tree.char_path(message_char)

        return encoded_string

    def decode(self, encoded_message=""):
        pass

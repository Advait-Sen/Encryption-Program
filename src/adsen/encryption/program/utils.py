import re


def get_input(prompt=""):
    if prompt:
        print(prompt)
    return input("")


class CharTree:
    children = []
    char_value = ""
    char_cache = {}
    frequency = 1

    def __init__(self, child):

        if type(child) is CharTree:
            self.__init_char_tree__(child)
        elif type(child) is list:
            self.__init_char_tree_list__(child)
        else:
            self.__init_char__(str(child))

        pass

    def __init_char__(self, char):
        if len(char) != 1:
            raise ValueError(f"Parameter 'char' must be a character (a string of length 1), not '{char}'")
        self.char_value = char
        pass

    def __init_char_tree__(self, char_tree):
        self.char_value = char_tree.char_value
        self.children = char_tree.children
        self.frequency = char_tree.frequency
        self.char_cache = char_tree.char_cache
        pass

    def __init_char_tree_list__(self, children):
        if len(children) > 1:
            self.char_value = ""
            self.children = children
            self.frequency = 0
            for i in range(len(children)):
                child = children[i]
                self.frequency += child.frequency
                for key in child.char_cache.keys():
                    self.char_cache = {}
                    self.char_cache.setdefault(key, str(i) + child.char_cache[key])
                    pass
            pass
        elif len(children) == 1:
            self.__init_char_tree__(children[0])
            pass
        else:
            raise ValueError("Cannot input an empty list to initialise a CharTree!")
        pass

    def has_children(self):
        raise self.has_child()

    def has_child(self, index=0):
        return index < len(self.children)

    def get_frequency(self):
        return self.frequency

    def get_child(self, index=0):
        return self.children[index]

    def get_char(self, path=""):
        if path == "":
            return self.char_value
        for key in self.char_cache.keys():
            if self.char_cache[key] == path:
                return key
            pass

        return self.children[int(path[:1])].get_char(path[1:])

    def has_char(self, char):
        if char == self.char_value:
            return True
        for child in self.children:
            if child.has_char(char):
                return True
        return False

    def char_path(self, char):
        if char in self.char_cache:
            return self.char_cache[char]

        for i in range(len(self.children)):
            child = self.children[i]
            if child.has_char(char):
                self.char_cache.setdefault(char, "")
                self.char_cache[char] = str(i) + child.char_path(char)
                return self.char_cache.get(char)

        if char == self.char_value:
            self.char_cache.setdefault(char, "")

        return ""

    def __len__(self):
        return len(self.children)

    def is_leaf(self):
        return self.char_value != ""

    def __str__(self):
        if self.char_value != "":
            return f"'{self.char_value}'"

        string = "["

        for i in range(len(self.children) - 1):
            string += str(self.children[i]) + ","
        string += str(self.children[len(self.children) - 1]) + ']'

        return string

    def __hash__(self):
        if self.char_value != 0:
            return ord(self.char_value)

        hash_code = 0
        for child in self.children:
            hash_code ^= hash(child)

        return hash_code

    @staticmethod
    def parse_string(tree_string):
        if tree_string == "'\n'":
            return CharTree('\n')
        if re.match("'.'", tree_string):
            return CharTree(tree_string[1:2])

        child_args = []
        child = ""
        tree_chars = list(tree_string)
        paren_counter = 1
        is_in_quote = False

        for i in range(1, len(tree_chars)):
            current_char = tree_chars[i]
            if current_char == '[':
                paren_counter += 1

            if current_char == ']':
                paren_counter -= 1

            if current_char == "'":
                is_in_quote = not is_in_quote

            if paren_counter > 0:
                child += current_char
                if paren_counter == 1 and (current_char == ']' or current_char == "'") and not is_in_quote:
                    child_args.append(child)
                    child = ""
                    i += 1

        child_char_trees = []
        for child in child_args:
            child_char_trees.append(CharTree.parse_string(child))

        return CharTree(child_char_trees)

    pass

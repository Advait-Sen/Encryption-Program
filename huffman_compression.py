encryption_base = 3  # gonna implement as function argument later


def __sort_chars(message):
    char_count = {}

    for line in message:
        for char in [c for c in line]:
            char_count.setdefault(repr(char), 0)
            char_count[repr(char)] += 1

    return char_count


def __sort_char_map(char_map):
    return [sorted(char_map.keys(), key=char_map.get, reverse=True), char_map]


def __get_huffman_tree(sorted_chars):
    reverse_sorted_chars = list(reversed(sorted_chars[0]))

    # if less than encryption_base, just return the string, no need to encrypt tbh
    if len(reverse_sorted_chars) <= encryption_base:
        return [eval(val) for val in reverse_sorted_chars]

    while len(reverse_sorted_chars) > 1:
        popped_chars = []
        for i in range(min(encryption_base, len(reverse_sorted_chars))):
            char = reverse_sorted_chars.pop(0)
            popped_chars.append([eval(char), sorted_chars[1].pop(char)])

        pair = [[], 0]
        for c in popped_chars:
            pair[0].append(c[0])
            pair[1] += c[1]
        sorted_chars[1].setdefault(repr(pair[0]), pair[1])
        sorted_chars = __sort_char_map(sorted_chars[1])
        reverse_sorted_chars = list(reversed(sorted_chars[0]))

    return eval(reverse_sorted_chars[0])


def huffman_tree(message):
    return __get_huffman_tree(__sort_char_map(__sort_chars(message)))


def __evaluate_item(item, index=''):
    ret_key = ''
    if type(item) is str:
        return index + repr(item)
    for i in range(len(item)):
        if type(item[i]) is str:
            if item[i] == '\n':  # Special handling cos it gets parsed weirdly, can't rly do much about ' tho,
                ret_key += index + str(i) + '\'\n\''
            elif item[i] == "'":
                # todo fix this
                exit(print('Cannot use "\'" in messages, as it unfortunately cannot be compiled by the processor.'))
            else:
                ret_key += index + str(i) + repr(item[i])
        else:
            ret_key += __evaluate_item(item[i], index + str(i))
    return ret_key


def huffman_encode(message=''):
    encoding_key = __evaluate_item(huffman_tree(message))
    char_list = encoding_key.split('\'')
    encoding_map = {}

    for i in range(int((len(char_list) - 1) / 2)):
        key = i * 2 + 1
        val = i * 2
        encoding_map.setdefault(char_list[key], char_list[val])
    encoded_message = ''
    for char in message:
        encoded_message += encoding_map[char]

    return [encoded_message, encoding_key]


def huffman_decode(message, encoding_key):
    decoded_message = ''

    char_list = encoding_key.split('\'')

    encoding_map = {}

    for i in range(int((len(char_list) - 1) / 2)):
        key = i * 2 + 1
        val = i * 2
        encoding_map.setdefault(char_list[val], char_list[key])

    char_buffer = ''

    for char in message:
        char_buffer += char
        if encoding_map.get(char_buffer, '') != '':
            decoded_message += encoding_map[char_buffer]
            char_buffer = ''

    return decoded_message

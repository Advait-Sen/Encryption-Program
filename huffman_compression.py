message_file=open('message.txt','r+')

message=message_file.readlines()

huffman_tree={}

char_count={}

for line in message:
    for char in [c for c in line]:
        char_count.setdefault(char,0)
        char_count[char]+=1
        
print(char_count)

for char in char_count:
    
    pass

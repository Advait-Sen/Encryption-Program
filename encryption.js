encryptionType = "";

currentPressedButton = "";

//Using separate modulo function because javascript % doesn't work with negative numbers
//https://stackoverflow.com/questions/4467539/javascript-modulo-gives-a-negative-result-for-negative-numbers#4467559
function mod(n, m) {
    return ((n % m) + m) % m;
}

function onClickEncryption(type) {
    if (type != currentPressedButton) {
        if (currentPressedButton != "") document.getElementById(currentPressedButton).className = "btn-primary";
        document.getElementById(type).className = "btn-danger";
        currentPressedButton = type;
    } else {
        if (currentPressedButton == "") {
            document.getElementById(type).className = "btn-danger";
            currentPressedButton = type
        } else {
            document.getElementById(type).className = "btn-primary";
            currentPressedButton = "";
            encryptionType = "";
            return
        }
    }
    encryptionType = type;
}

function onClickEncrypt() {
    output = "Encrypted text";
    message = document.getElementById("messageinput").value;
    switch (encryptionType) {
        case "classicvignere": { //Classic vignere, only encrypting letters
            key = prompt("Please input your key:");
            if (key == "") key = "A";
            output = "";
            for (let i in message) {
                c = message[i];
                isLetter = (c >= "A" && c <= "Z") || (c >= "a" && c < "z");
                if (isLetter) {
                    k = key[i % key.length];
                    charA = c <= "Z" ? 65 : 97;
                    keyA = k <= "Z" ? 65 : 97;
                    numC = c.charCodeAt(0);
                    numK = k.charCodeAt(0);
                    output += String.fromCharCode(mod(numC + numK - charA - keyA, 26) + charA);
                } else { //if not a letter, 
                    output += c
                }
            };
            break;
        }
        default:
            alert("No encryption type selected!")
    }
    document.getElementById("output").innerText = output;
}

function onClickDecrypt() {
    output = "Decrypted text";
    message = document.getElementById("messageinput").value;
    switch (encryptionType) {
        case "classicvignere": {
            key = prompt("Please input your key:");
            if (key == "") key = "A";
            output = "";
            for (let i in message) {
                c = message[i];
                isLetter = (c >= "A" && c <= "Z") || (c >= "a" && c < "z");
                if (isLetter) {
                    k = key[i % key.length];
                    charA = c <= "Z" ? 65 : 97;
                    keyA = k <= "Z" ? 65 : 97;
                    numC = c.charCodeAt(0);
                    numK = k.charCodeAt(0);
                    output += String.fromCharCode(mod(numC - numK - charA + keyA, 26) + charA);
                } else { //if not a letter, 
                    output += c
                }
            };
            break;
        }
        default:
            alert("No decryption type selected!")
    }
    document.getElementById("output").innerText = output;
}
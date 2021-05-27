package adsen.encryption.program.utils;

import java.util.Scanner;

public class Utils {

    public static String getMessageInput(Scanner scanner, boolean encrypt) {
        StringBuilder message = new StringBuilder();
        String input;
        System.out.println(
                "Please input the message you want to " + (encrypt ? "encrypt" : "decrypt") +
                        " line by line, and when you're done enter 'END' (case-sensitive)"
        );

        while (true) {
            input = scanner.nextLine();
            if (input.equals("END"))
                break;
            message.append(input);
            message.append('\n');
        }

        message = new StringBuilder(message.toString().strip());//removing initial and final whitespaces

        return message.toString();
    }
}


package assignmentchatapp;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Message {
    private static int numMessagesSent = 0;
    private static JSONArray storedMessages = new JSONArray();

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    Scanner scan = new Scanner(System.in);

    // ==============================================
    // MAIN MENU
    // ==============================================
    public void mainMenu() {
        while (true) {
            System.out.println("\nWelcome to QuickChat");
            System.out.println("1) Send Message");
            System.out.println("2) Show Recently Sent Messages (Coming Soon)");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            int choice = scan.nextInt();
            scan.nextLine(); // consume newline

            switch (choice) {
                case 1 -> sendMessage();
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    // ==============================================
    // MESSAGE VALIDATION METHODS
    // ==============================================
    public boolean checkMessageID(String id) {
        return id.length() <= 10;
    }

    public boolean checkRecipientCell(String cell) {
        return Pattern.matches("^\\+27\\d{9}$", cell);
    }

    // ==============================================
    // MESSAGE CREATION AND SENDING
    // ==============================================
    public String createMessageHash(String id, String messageText) {
        String[] words = messageText.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;

        return id.substring(0, 2) + ":" + numMessagesSent + ":" + firstWord.toUpperCase() + lastWord.toUpperCase();
    }

    public void sendMessage() {
        System.out.print("How many messages do you want to send? ");
        int numToSend = scan.nextInt();
        scan.nextLine();

        for (int i = 0; i < numToSend; i++) {
            messageID = String.valueOf((long) (Math.random() * 1_000_000_0000L));
            while (!checkMessageID(messageID)) {
                messageID = String.valueOf((long) (Math.random() * 1_000_000_0000L));
            }

            System.out.println("Enter recipient cell number (+27XXXXXXXXX):");
            recipient = scan.nextLine();
            while (!checkRecipientCell(recipient)) {
                System.out.println("Invalid cell number. Try again:");
                recipient = scan.nextLine();
            }

            System.out.println("Enter your message (max 250 characters):");
            messageText = scan.nextLine();
            while (messageText.length() > 250) {
                System.out.println("Please enter a message less than 250 characters.");
                messageText = scan.nextLine();
            }

            messageHash = createMessageHash(messageID, messageText);

            System.out.println("\nChoose what to do:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int option = scan.nextInt();
            scan.nextLine();

            if (option == 1) {
                numMessagesSent++;
                System.out.println("Message sent successfully!");
                storeMessage();
                printMessageDetails();
            } else if (option == 2) {
                System.out.println("Message disregarded.");
            } else if (option == 3) {
                storeMessage();
                System.out.println("Message stored for later.");
            }
        }

        System.out.println("\nTotal messages sent: " + numMessagesSent);
    }

    // ==============================================
    // DISPLAY & STORAGE
    // ==============================================
    public void printMessageDetails() {
        JOptionPane.showMessageDialog(null,
                "Message ID: " + messageID +
                        "\nMessage Hash: " + messageHash +
                        "\nRecipient: " + recipient +
                        "\nMessage: " + messageText,
                "Message Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void storeMessage() {
        JSONObject msgObj = new JSONObject();
        msgObj.put("MessageID", messageID);
        msgObj.put("MessageHash", messageHash);
        msgObj.put("Recipient", recipient);
        msgObj.put("Message", messageText);

        storedMessages.add(msgObj);

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(storedMessages.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}

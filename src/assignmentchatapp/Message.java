package assignmentchatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * QuickChat Message System with full GUI interaction using JOptionPane.
 * Handles sending, storing, and validating messages in an interactive pop-up environment.
 *
 * @author Tracey_Obi
 * @improved GPT-5 (2025)
 */
public class Message {

    // === Shared State ===
    private static int numMessagesSent = 0;
    private static JSONArray storedMessages = new JSONArray();

    // === Message Fields ===
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    // ============================================================
    // MAIN MENU
    // ============================================================
    public void mainMenu() {
        while (true) {
            String[] options = {"Send Message", "Show recently sent messages(Coming Soon)", "Quit"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Welcome to QuickChat! What would you like to do?",
                    "QuickChat Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0 -> sendMessage();
                case 1 -> showStoredMessages();
                case 2, JOptionPane.CLOSED_OPTION -> {
                    JOptionPane.showMessageDialog(null, "Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ============================================================
    // VALIDATION METHODS
    // ============================================================

    public boolean checkMessageID(String id) {
        return id.length() <= 10;
    }

    public boolean checkRecipientCell(String cell) {
        return Pattern.matches("^\\+27\\d{9}$", cell);
    }

    // ============================================================
    // MESSAGE CREATION AND HANDLING
    // ============================================================

    public String createMessageHash(String id, String messageText) {
        String[] words = messageText.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return id.substring(0, 2).toUpperCase() + ":" + numMessagesSent + ":" +
                firstWord.toUpperCase() + lastWord.toUpperCase();
    }

    public void sendMessage() {
        int numToSend = 0;

        // Ask for number of messages
        while (numToSend <= 0) {
            try {
                String input = JOptionPane.showInputDialog(null,
                        "How many messages do you want to send?",
                        "Number of Messages",
                        JOptionPane.QUESTION_MESSAGE);
                if (input == null) return; // user canceled
                numToSend = Integer.parseInt(input);
                if (numToSend <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Send multiple messages
        for (int i = 0; i < numToSend; i++) {
            messageID = String.valueOf((long) (Math.random() * 1_000_000_0000L));
            while (!checkMessageID(messageID)) {
                messageID = String.valueOf((long) (Math.random() * 1_000_000_0000L));
            }

            // Recipient input
            recipient = JOptionPane.showInputDialog(null,
                    "Enter recipient cell number (+27XXXXXXXXX):",
                    "Recipient Input",
                    JOptionPane.QUESTION_MESSAGE);
            if (recipient == null) return;
            while (!checkRecipientCell(recipient)) {
                recipient = JOptionPane.showInputDialog(null,
                        "Invalid format.\nCell number must start with +27 and contain 11 digits.\nTry again:",
                        "Invalid Cell Number",
                        JOptionPane.ERROR_MESSAGE);
                if (recipient == null) return;
            }

            // Message input
            messageText = JOptionPane.showInputDialog(null,
                    "Enter your message (max 250 characters):",
                    "Message Input",
                    JOptionPane.PLAIN_MESSAGE);
            if (messageText == null) return;
            while (messageText.length() > 250) {
                messageText = JOptionPane.showInputDialog(null,
                        "Message too long. Please enter under 250 characters:",
                        "Invalid Message Length",
                        JOptionPane.ERROR_MESSAGE);
                if (messageText == null) return;
            }

            messageHash = createMessageHash(messageID, messageText);

            // Choose action
            String[] actions = {"Send Message", "Store for Later", "Disregard"};
            int choice = JOptionPane.showOptionDialog(null,
                    "What would you like to do with this message?",
                    "Message Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]);

            switch (choice) {
                case 0 -> {
                    numMessagesSent++;
                    storeMessage();
                    printMessageDetails();
                    JOptionPane.showMessageDialog(null, "Message sent successfully!");
                }
                case 1 -> {
                    storeMessage();
                    JOptionPane.showMessageDialog(null, "Message stored for later.");
                }
                case 2 -> JOptionPane.showMessageDialog(null, "Message disregarded.");
                default -> JOptionPane.showMessageDialog(null, "No action selected.");
            }
        }

        JOptionPane.showMessageDialog(null, "📨 Total messages sent: " + numMessagesSent,
                "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    // ============================================================
    // DISPLAY & STORAGE
    // ============================================================

    public void printMessageDetails() {
        String details = "Message ID: " + messageID +
                "\nMessage Hash: " + messageHash +
                "\nRecipient: " + recipient +
                "\nMessage: " + messageText;
        JOptionPane.showMessageDialog(null, details, "Message Details", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null,
                    "Error storing message: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showStoredMessages() {
        if (storedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No stored messages yet.",
                    "Stored Messages",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder msgList = new StringBuilder("=== Stored Messages ===\n");
        for (Object obj : storedMessages) {
            JSONObject msg = (JSONObject) obj;
            msgList.append("ID: ").append(msg.get("MessageID"))
                    .append("\nRecipient: ").append(msg.get("Recipient"))
                    .append("\nMessage: ").append(msg.get("Message"))
                    .append("\n------------------------\n");
        }

        JOptionPane.showMessageDialog(null, msgList.toString(), "Stored Messages", JOptionPane.INFORMATION_MESSAGE);
    }
}

package assignmentchatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Message
 * - QuickChat core: send/store/discard messages
 * - validation helpers for unit tests
 * - persistent message storage to messages.json
 *
 * Methods are designed to be unit-testable (return values rather than only printing).
 */
public class Message {

    // counters and storage (shared across instances)
    private static int numMessagesSent = 0;
    private static JSONArray storedMessages = new JSONArray();

    // instance fields (current message)
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    private final Scanner scanner = new Scanner(System.in);

    // ---------------------------
    // Public helpers for testing
    // ---------------------------
    public void resetForTests() {
        numMessagesSent = 0;
        storedMessages = new JSONArray();
    }

    public int getNumMessagesSent() {
        return numMessagesSent;
    }

    public int getStoredMessagesCount() {
        return storedMessages.size();
    }

    // ---------------------------
    // Validation helpers
    // ---------------------------
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10 && id.matches("\\d+");
    }

    // SA cell regex +27XXXXXXXXX
    public boolean checkRecipientCell(String cell) {
        if (cell == null) return false;
        return Pattern.matches("^\\+27\\d{9}$", cell);
    }

    public boolean isMessageLengthValid(String msg) {
        if (msg == null) return false;
        return msg.length() <= 250;
    }

    public int getExceededBy(String msg) {
        if (msg == null) return 0;
        int over = msg.length() - 250;
        return (over > 0) ? over : 0;
    }

    // ---------------------------
    // ID and Hash creation
    // ---------------------------
    /**
     * Generates a 10-digit numeric ID (string). Deterministic enough for tests.
     */
    public String generateMessageID() {
        long r = Math.abs(new Random().nextLong()) % 1_000_000_0000L;
        String id = String.format("%010d", r);
        this.messageID = id;
        return id;
    }

    /**
     * Format for assignment tests:
     * firstTwoCharsOfID : messageCount : FIRSTWORD+LASTWORD (uppercase, concatenated)
     */
    public String createMessageHash(String id, String messageText, int count) {
        if (id == null) id = "00";
        String firstTwo = id.length() >= 2 ? id.substring(0, 2) : String.format("%-2s", id).replace(' ', '0');
        String trimmed = (messageText == null) ? "" : messageText.trim();
        if (trimmed.isEmpty()) trimmed = "";
        String[] words = trimmed.split("\\s+");
        String first = (words.length > 0) ? words[0] : "";
        String last = (words.length > 1) ? words[words.length - 1] : first;
        String combined = (first + last).toUpperCase();
        return firstTwo + ":" + count + ":" + combined;
    }

    // ---------------------------
    // Interactive menu
    // ---------------------------
    public void mainMenu() {
        while (true) {
            System.out.println("\nWelcome to QuickChat");
            System.out.println("1) Send Message");
            System.out.println("2) Show Recently Sent Messages (Coming Soon)");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number from the menu.");
                continue;
            }

            switch (choice) {
                case 1 -> interactiveSendMessage();
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void interactiveSendMessage() {
        System.out.print("How many messages do you want to send? ");
        String countLine = scanner.nextLine().trim();
        int num;
        try {
            num = Integer.parseInt(countLine);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a numeric value.");
            return;
        }

        for (int i = 0; i < num; i++) {
            String id = generateMessageID();
            while (!checkMessageID(id)) {
                id = generateMessageID();
            }

            System.out.print("Enter recipient cell number (+27XXXXXXXXX): ");
            String rec = scanner.nextLine().trim();
            while (!checkRecipientCell(rec)) {
                System.out.print("Invalid cell number. Try again: ");
                rec = scanner.nextLine().trim();
            }

            System.out.print("Enter your message (max 250 characters): ");
            String text = scanner.nextLine();
            while (!isMessageLengthValid(text)) {
                System.out.println("Please enter a message less than or equal to 250 characters.");
                text = scanner.nextLine();
            }

            String hash = createMessageHash(id, text, numMessagesSent);

            System.out.println("\nChoose what to do:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            String actionLine = scanner.nextLine().trim();
            int option;
            try {
                option = Integer.parseInt(actionLine);
            } catch (NumberFormatException e) {
                option = 2;
            }

            String result = sendAction(option, id, rec, text, hash);
            System.out.println(result);

            if (option == 1) {
                // only show popup after a send
                this.messageID = id;
                this.recipient = rec;
                this.messageText = text;
                this.messageHash = hash;
                printMessageDetails();
            }
        }

        System.out.println("\nTotal messages sent: " + numMessagesSent);
    }

    /**
     * sendAction is unit-test-friendly: it validates inputs, performs the action,
     * persists stored messages to messages.json, and returns a String describing result.
     *
     * option: 1=send, 2=discard, 3=store
     */
    public String sendAction(int option, String id, String rec, String text, String hash) {
        if (id == null || !checkMessageID(id)) {
            return "Message ID is invalid.";
        }
        if (!checkRecipientCell(rec)) {
            return "Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
        if (!isMessageLengthValid(text)) {
            return "Message exceeds 250 characters by " + getExceededBy(text) + ", please reduce size.";
        }

        switch (option) {
            case 1 -> {
                // send
                numMessagesSent++;
                persistMessageRecord(id, rec, text, hash, "SENT");
                return "Message successfully sent.";
            }
            case 2 -> {
                // discard
                return "Press 0 to delete message.";
            }
            case 3 -> {
                // store
                persistMessageRecord(id, rec, text, hash, "STORED");
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid action selected.";
            }
        }
    }

    // Persist a single message record to messages.json
    private void persistMessageRecord(String id, String rec, String text, String hash, String status) {
        JSONObject msg = new JSONObject();
        msg.put("MessageID", id);
        msg.put("MessageHash", hash);
        msg.put("Recipient", rec);
        msg.put("Message", text);
        msg.put("Status", status);
        storedMessages.add(msg);

        try (FileWriter fw = new FileWriter("messages.json")) {
            fw.write(storedMessages.toJSONString());
            fw.flush();
        } catch (IOException e) {
            // best-effort persistence: ignore for tests but log for interactive use
            System.out.println("Warning: could not persist messages to file (" + e.getMessage() + ").");
        }
    }

    public void printMessageDetails() {
        JOptionPane.showMessageDialog(null,
                "Message ID: " + messageID +
                        "\nMessage Hash: " + messageHash +
                        "\nRecipient: " + recipient +
                        "\nMessage: " + messageText,
                "Message Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

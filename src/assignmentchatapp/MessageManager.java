package assignmentchatapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;

public class MessageManager {

    private JSONArray loadMessages() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("messages.json")) {
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            return new JSONArray(); // empty list if file missing
        }
    }

    public void displayLongestMessage() {
        JSONArray messages = loadMessages();

        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No messages stored yet.",
                    "Longest Message",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JSONObject longestMsg = (JSONObject) messages.get(0);

        for (Object o : messages) {
            JSONObject msg = (JSONObject) o;
            if (msg.get("Message").toString().length() >
                longestMsg.get("Message").toString().length()) {
                longestMsg = msg;
            }
        }

        JOptionPane.showMessageDialog(null,
                "📌 Longest Message Found:\n\n"
                        + "ID: " + longestMsg.get("MessageID") + "\n"
                        + "Recipient: " + longestMsg.get("Recipient") + "\n"
                        + "Message: " + longestMsg.get("Message") + "\n"
                        + "Hash: " + longestMsg.get("MessageHash") + "\n"
                        + "Length: " + longestMsg.get("Message").toString().length(),
                "Longest Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void searchMessagesByRecipient() {
        JSONArray messages = loadMessages();

        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No messages stored yet.",
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String query = JOptionPane.showInputDialog("Enter a recipient number to search (+27...):");
        if (query == null) return;

        boolean found = false;
        StringBuilder result = new StringBuilder("Results:\n");

        for (Object o : messages) {
            JSONObject msg = (JSONObject) o;

            if (msg.get("Recipient").equals(query)) {
                found = true;
                result.append("\nID: ").append(msg.get("MessageID"))
                        .append("\nMessage: ").append(msg.get("Message"))
                        .append("\nHash: ").append(msg.get("MessageHash"))
                        .append("\n----------------");
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null,
                    "No messages found for recipient: " + query);
        } else {
            JOptionPane.showMessageDialog(null,
                    result.toString(),
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

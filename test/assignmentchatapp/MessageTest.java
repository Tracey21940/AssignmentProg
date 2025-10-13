package assignmentchatapp;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Automated TestNG tests for Message (non-interactive).
 * These tests assume Message.resetForTests() exists and that sendAction
 * does not throw exceptions for valid inputs.
 */
public class MessageTest {

    private Message message;

    @BeforeMethod
    public void setup() {
        message = new Message();
        message.resetForTests();
    }

    @Test
    public void recipientValid() {
        assertTrue(message.checkRecipientCell("+27718693002"), "Expected valid SA number to pass.");
    }

    @Test
    public void recipientInvalid() {
        assertFalse(message.checkRecipientCell("0712345678"), "Expected missing +27 to fail.");
    }

    @Test
    public void messageLengthValid() {
        assertTrue(message.isMessageLengthValid("Hello"), "Short message should be valid.");
    }

    @Test
    public void messageLengthTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) sb.append("a");
        String longMsg = sb.toString();
        assertFalse(message.isMessageLengthValid(longMsg), "Message >250 should be invalid.");
        assertEquals(message.getExceededBy(longMsg), 10, "Exceeded-by should be 10.");
    }

    @Test
    public void messageIDFormat() {
        String id = message.generateMessageID();
        assertNotNull(id, "ID should not be null.");
        assertTrue(id.length() <= 10, "ID length should be <= 10.");
        assertTrue(id.matches("\\d+"), "ID should be numeric.");
        assertTrue(message.checkMessageID(id), "checkMessageID should accept generated id.");
    }

    @Test
    public void createHashDeterministic() {
        String id = "0012345678";
        String hash = message.createMessageHash(id, "Hi tonight", 0);
        assertEquals(hash, "00:0:HITONIGHT", "Hash must be deterministic and exact.");
    }

    @Test
    public void sendActionSendWorks() {
        String id = "0000000001";
        String rec = "+27718693002";
        String text = "Dinner tonight";
        String hash = message.createMessageHash(id, text, message.getNumMessagesSent());

        String result = message.sendAction(1, id, rec, text, hash);
        assertEquals(result, "Message successfully sent.", "Expected send success message.");
        assertEquals(message.getNumMessagesSent(), 1, "Num messages sent should be incremented.");
        assertEquals(message.getStoredMessagesCount(), 1, "Stored messages count should be 1 (sent stored).");
    }

    @Test
    public void sendActionStoreWorks() {
        String id = "0000000002";
        String rec = "+27718693002";
        String text = "Store this msg";
        String hash = message.createMessageHash(id, text, message.getNumMessagesSent());

        String result = message.sendAction(3, id, rec, text, hash);
        assertEquals(result, "Message successfully stored.", "Expected store success message.");
        assertEquals(message.getNumMessagesSent(), 0, "Stored-only should not increment sent counter.");
        assertEquals(message.getStoredMessagesCount(), 1, "Stored messages count should be 1.");
    }

    @Test
    public void sendActionInvalidRecipient() {
        String id = "0000000003";
        String rec = "0823456789";
        String text = "Hello";
        String hash = message.createMessageHash(id, text, 0);

        String result = message.sendAction(1, id, rec, text, hash);
        assertTrue(result.startsWith("Cell phone number is incorrectly formatted"),
                   "Expected error message about invalid cellphone format.");
    }
}

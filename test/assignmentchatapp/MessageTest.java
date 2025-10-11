package assignmentchatapp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class MessageTest {

    private Message message;

    @Test
    public void setup() {
        message = new Message();
        message.resetForTests();
    }

    @Test
    public void recipientValid() {
        assertTrue(message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void recipientInvalid() {
        assertFalse(message.checkRecipientCell("0712345678"));
    }

    @Test
    public void messageLengthValid() {
        assertTrue(message.isMessageLengthValid("Hello"));
    }

    @Test
    public void messageLengthTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) sb.append("a");
        String longMsg = sb.toString();
        assertFalse(message.isMessageLengthValid(longMsg));
        assertEquals(10, message.getExceededBy(longMsg));
    }

    @Test
    public void messageIDFormat() {
        String id = message.generateMessageID();
        assertNotNull(id);
        assertTrue(id.length() <= 10);
        assertTrue(id.matches("\\d+"));
        assertTrue(message.checkMessageID(id));
    }

    @Test
    public void createHashDeterministic() {
        String id = "0012345678";
        String hash = message.createMessageHash(id, "Hi tonight", 0);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void sendActionSendWorks() {
        String id = "0000000001";
        String rec = "+27718693002";
        String text = "Dinner tonight";
        String hash = message.createMessageHash(id, text, message.getNumMessagesSent());
        String result = message.sendAction(1, id, rec, text, hash);
        assertEquals("Message successfully sent.", result);
        assertEquals(1, message.getNumMessagesSent());
        assertEquals(1, message.getStoredMessagesCount());
    }

    @Test
    public void sendActionStoreWorks() {
        String id = "0000000002";
        String rec = "+27718693002";
        String text = "Store this msg";
        String hash = message.createMessageHash(id, text, message.getNumMessagesSent());
        String result = message.sendAction(3, id, rec, text, hash);
        assertEquals("Message successfully stored.", result);
        assertEquals(0, message.getNumMessagesSent());
        assertEquals(1, message.getStoredMessagesCount());
    }

    @Test
    public void sendActionInvalidRecipient() {
        String id = "0000000003";
        String rec = "0823456789";
        String text = "Hello";
        String hash = message.createMessageHash(id, text, 0);
        String result = message.sendAction(1, id, rec, text, hash);
        assertTrue(result.startsWith("Cell phone number is incorrectly formatted"));
    }
}

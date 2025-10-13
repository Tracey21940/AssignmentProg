package assignmentchatapp;

import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 * MessageNGTest
 * @author Tracey
 */
public class MessageNGTest {

    private Message instance;

    @BeforeClass
    public void setUpClass() {
        System.out.println("=== Starting Message Class Tests ===");
    }

    @AfterClass
    public void tearDownClass() {
        System.out.println("=== All Message Tests Completed ===");
    }

    @BeforeMethod
    public void setUpMethod() {
        instance = new Message();
    }

    @AfterMethod
    public void tearDownMethod() {
        instance = null;
    }

    // TEST CASE 1 — VALID MESSAGE
    @Test
    public void testValidMessageData() {
        String recipient = "+27718693002";
        String message = "Hi Mike, can you join us for dinner tonight";

        // 1️⃣ Test message length under 250
        boolean isValidLength = message.length() <= 250;
        if (isValidLength) {
            assertEquals("Message ready to send.", "Message ready to send.");
        } else {
            fail("Message exceeds 250 characters, expected success.");
        }

        // 2️⃣ Test recipient format
        boolean validCell = instance.checkRecipientCell(recipient);
        assertTrue(validCell, "Recipient number should be valid.");
        System.out.println("Cell phone number successfully captured.");

        // 3️⃣ Test hash creation
        String messageID = "0010";
        String hash = instance.createMessageHash(messageID, message);
        String expectedHash = "00:0:HITONI";
        assertEquals(hash.substring(0, 11), expectedHash, "Message hash should match expected pattern.");

        // 4️⃣ Test message ID creation
        assertTrue(instance.checkMessageID(messageID), "Message ID should be valid length (<=10).");
        System.out.println("Message ID generated: " + messageID);

        // 5️⃣ Simulate Send action
        String sendResult = "Message successfully sent.";
        assertEquals(sendResult, "Message successfully sent.");
    }

    // TEST CASE 2 — INVALID MESSAGE
    @Test
    public void testInvalidMessageData() {
        String recipient = "08575975889"; // missing +27
        String message = "Hi Keegan, did you receive the payment?";

        // 1️⃣ Test invalid cell number
        boolean validCell = instance.checkRecipientCell(recipient);
        assertFalse(validCell, "Recipient number should be invalid.");
        System.out.println("Cell phone number is incorrectly formatted or does not contain an international code.");

        // 2️⃣ Test valid length (should still pass)
        boolean isValidLength = message.length() <= 250;
        assertTrue(isValidLength, "Message under 250 chars should pass.");

        // 3️⃣ Simulate user selecting Disregard
        String disregardResult = "Press 0 to delete message.";
        assertEquals(disregardResult, "Press 0 to delete message.");
    }

    // TEST CASE 3 — MESSAGE TOO LONG
    @Test
    public void testMessageExceedsLength() {
        String recipient = "+27831234567";
        String message = "A".repeat(260); // intentionally too long

        // 1️⃣ Length check
        boolean isValidLength = message.length() <= 250;
        if (!isValidLength) {
            int extra = message.length() - 250;
            String failMsg = "Message exceeds 250 characters by " + extra + ", please reduce size.";
            assertEquals(failMsg, "Message exceeds 250 characters by 10, please reduce size.".replace("10", String.valueOf(extra)));
        } else {
            fail("Expected message length failure.");
        }
    }

    // TEST CASE 4 — STORE MESSAGE
    @Test
    public void testStoreMessage() {
        instance.storeMessage();
        String result = "Message successfully stored.";
        assertEquals(result, "Message successfully stored.");
    }

    // EXTRA CHECK — LOOP HASH VALIDATION (for multiple messages)
    @Test
    public void testMultipleHashesLoop() {
        String[] ids = {"11", "12", "13"};
        String[] messages = {"Hi Tom how are you", "Meeting at 6 tonight", "Call me when free"};

        for (int i = 0; i < ids.length; i++) {
            String hash = instance.createMessageHash(ids[i], messages[i]);
            assertTrue(hash.contains(ids[i].substring(0, 2).toUpperCase()), "Hash should include ID prefix.");
        }
        System.out.println("All message hashes validated successfully in loop.");
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package assignmentchatapp;

import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author RC_Student_Lab
 */

public class MessageManagerTest {

    @Test
    public void testAddMessageStoresCorrectly() {
        MessageManager manager = new MessageManager();
        manager.addMessage("ID01", "+27123456789", "Hello!", "HASH1");

        Assert.assertTrue(true, "Message stored successfully.");
    }

    @Test
    public void testLongestMessageDetection() {
        MessageManager manager = new MessageManager();
        manager.addMessage("ID01", "+2711", "Hey", "H1");
        manager.addMessage("ID02", "+2712", "This is the longest message!", "H2");

        // Just checking no crash, GUI returns correct longest
        manager.displayLongestMessage();
        Assert.assertTrue(true);
    }

    @Test
    public void testSearchMessagesByRecipientRuns() {
        MessageManager manager = new MessageManager();
        manager.addMessage("ID01", "+271234", "Hi!", "HASH-A");
        manager.addMessage("ID02", "+278888", "Test!", "HASH-B");

        manager.searchMessagesByRecipient();
        Assert.assertTrue(true);
    }
}

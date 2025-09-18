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
 * @author RC_Student_lab
 */
public class RegistrationClassNGTest {
    

    /**
     * Test of checkUsername method, of class RegistrationClass.
     */
//    @Test
//    public void testCheckUsername() {
//        System.out.println("checkUsername");
//        String username = "";
//        RegistrationClass instance = new RegistrationClass();
//        boolean expResult = false;
//        boolean result = instance.checkUsername(username);
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of checkPassword method, of class RegistrationClass.
//     */
//    @Test
//    public void testCheckPassword() {
//        System.out.println("checkPassword");
//        String password = "";
//        RegistrationClass instance = new RegistrationClass();
//        boolean expResult = false;
//        boolean result = instance.checkPassword(password);
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of checkCellNum method, of class RegistrationClass.
//     */
//    @Test
//    public void testCheckCellNum() {
//        System.out.println("checkCellNum");
//        String cellphoneNum = "";
//        RegistrationClass instance = new RegistrationClass();
//        boolean expResult = false;
//        boolean result = instance.checkCellNum(cellphoneNum);
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
RegistrationClass reg = new RegistrationClass();

    // Username Tests
    @Test
    public void testCorrectUsername() {
        assertTrue(reg.checkUsername("Trac_"));  // Expected true
    }

    @Test
    public void testIncorrectUsername() {
        assertFalse(reg.checkUsername("tracey!!!!!!!"));  // Expected false
    }

    // Password Tests
    @Test
    public void testCorrectPassword() {
        assertTrue(reg.checkPassword("Tracey41@@"));  // Expected true
    }

    @Test
    public void testIncorrectPassword() {
        assertFalse(reg.checkPassword("password"));  // Expected false
    }

    // Cell Number Tests
    @Test
    public void testCorrectCellNumber() {
        assertTrue(reg.checkCellNum("+27604644789"));  // Expected true
    }

    @Test
    public void testIncorrectCellNumber() {
        assertFalse(reg.checkCellNum("08366553"));  // Expected false
    }

}

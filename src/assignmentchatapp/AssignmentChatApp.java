/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignmentchatapp;

/**
 *
 * @author Tracey_Obi
 */
public class AssignmentChatApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("===  Registration ===");
        RegistrationClass register = new RegistrationClass();
        register.userInput(); // register and (on success) redirect to login & quickchat
    }
    
}

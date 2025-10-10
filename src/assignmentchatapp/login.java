package assignmentchatapp;

import java.util.Scanner;

public class login {
    private RegistrationClass register;
    
    public login(RegistrationClass register) {
        this.register = register;
    }
    
    // === Interactive login with Scanner (for real use) ===
    public void log() {
        Scanner scan = new Scanner(System.in);
        String loginUsername;
        String loginPassword;
        boolean loggedIn = false;
        
        while (!loggedIn) {
            System.out.println("Enter username:");
            loginUsername = scan.nextLine();
            
            if (!loginUsername.equals(register.username)) {
                System.out.println("Username is incorrect. Try again.\n");
                continue;
            }
            
            System.out.println("Enter password:");
            loginPassword = scan.nextLine();
            
            if (!loginPassword.equals(register.password)) {
                System.out.println("Password is incorrect. Try again.\n");
                continue;
            }
            
            System.out.println("Login successful! Welcome " 
                    + register.name + " " + register.surname +", its great to see you again.");
            loggedIn = true;
            System.out.println("Welcome to QuickChat, " + register.name + "!");
                Message messageApp = new Message();
                messageApp.mainMenu();
        }
    }
    
    // === Non-interactive method (for testing) ===
    public boolean checkUserDetails(String username, String password) {
        return username.equals(register.username) && password.equals(register.password);
    }
}

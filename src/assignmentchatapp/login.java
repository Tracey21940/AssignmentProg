package assignmentchatapp;

import java.util.Scanner;

/**
 *
 * @author 
 */
public class login {
    RegistrationClass register;
    
    public login(RegistrationClass register){
        this.register = register;
    }
    
    public void log(){
        Scanner scan = new Scanner(System.in);
        String loginUsername;
        String loginPassword;
        boolean loggedIn = false;
        
        // Keep asking until correct details are entered
        while (!loggedIn) {
            System.out.println("Enter username:");
            loginUsername = scan.nextLine();
            
            if (!loginUsername.equals(register.username)) {
                System.out.println("Username is incorrect. Try again.\n");
                continue; // go back to start of loop
            }
            
            System.out.println("Enter password:");
            loginPassword = scan.nextLine();
            
            if (!loginPassword.equals(register.password)) {
                System.out.println("Password is incorrect. Try again.\n");
                continue; // ask again
            }
            
            // If both correct:
            System.out.println("Login successful! Welcome " + register.name + " " + register.surname);
            loggedIn = true;
        }
    }//End of log method
}//End of the login class

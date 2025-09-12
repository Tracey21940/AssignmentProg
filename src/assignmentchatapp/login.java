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
        
        System.out.println("Enter username");
        loginUsername = scan.nextLine();
        
        System.out.println("Enter password");
        loginPassword = scan.nextLine();
        
        // Call checkUserDetails to validate login
        if (checkUserDetails(loginUsername, loginPassword)) {
            System.out.println("Login successful! Welcome " + register.name + " " + register.surname);
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }//End of log method
    
    public boolean checkUserDetails(String username, String password){
        if(username.equals(register.username) && password.equals(register.password)){
            return true;
        } else {
            return false;
        }
    }
}//End of the login class

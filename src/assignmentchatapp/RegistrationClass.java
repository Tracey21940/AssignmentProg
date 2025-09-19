/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignmentchatapp;
import java.util.Scanner;
/**
 *
 * @author Tracey_Obi
 */
class RegistrationClass {
    
    String name;
        String surname;
        String username;
        String password;
        String cellphoneNum;
    public void userInput(){
        Scanner scan = new Scanner(System.in);
        
        //prompt the user for details
        System.out.println("Please enter your name:");
        name = scan.nextLine();
        
        System.out.println("Please enter your surname:");
        surname = scan.nextLine();
        
        //Prompt user username
        do{
        System.out.println("Please enter your username:");
        username = scan.nextLine();
        }while(!checkUsername(username));
        
        //Prompt user password
        do{
        System.out.println("Please enter your password:");
        password = scan.nextLine();
        }while(!checkPassword(password));
        
        //Prompt user cellphone number
        do{
        System.out.println("Please enter your cellphone number:");
        cellphoneNum = scan.nextLine();
        }while(!checkCellNum(cellphoneNum));
        
        System.out.println("\n===  Login ===");
        login logged = new login(this);
        logged.log();
        
        
    }//End of userInput
    
    public boolean checkUsername(String username){
        //username must have a undecore(_) and be no longer than five characers
        if(username.contains("_") && username.length() <= 5){
            System.out.println("Username is successfully captured.");
            return true;
        }else{
            System.out.println("Username is not correctly formatted,\nplease ensure that your username contains an underscore\nand is no more than five characters in length.");
            return false;
        }
    }//End of checkUsename method
    
    public boolean checkPassword(String password){
        //check password length
        boolean hasMinLength = password.length() >= 8;
        
        //check uppercase
        boolean hasUppercase = password.matches(".*[A-Z].*");
        
        //check if password has number
        boolean hasNum = password.matches(".*\\d.*");
        
        //check if password has a special character
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_-].*");
        
        if(hasMinLength && hasUppercase && hasNum && hasSpecialChar){
            System.out.println("Username is successfully captured.");
            return true;
        }else{
            System.out.println("Password is not correctly formatted;\nplease ensure that your password contains at least eight characters,\na capital letter, a number, and a special character.");
            
            return false;
        }
    }// End of checkPassword
    
    //It should have the SA code
    public boolean checkCellNum(String cellphoneNum) {
    // Regex: must start with +27 and followed by 9 digits
    boolean isValid = cellphoneNum.matches("^\\+27\\d{9}$");    //OpenAI. 2025. ChatGPT (GPT-5) [AI chatbot]. Available at: https://chat.openai.com
                                                                //(Accessed: 19 September 2025).

    if(isValid) {
        System.out.println("Cell number successfully captured.");
        return true;
    } else {
        System.out.println("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.");
        return false;
    }
}
   }//End of registration class

package assignmentchatapp;

import java.util.Scanner;

/**
 * Login
 * - interactive login with retry attempts
 * - on successful login redirects to QuickChat (Message.mainMenu())
 * - non-interactive checkUserDetails(username,password) for unit tests
 */
public class login {

    private final RegistrationClass register;

    public login(RegistrationClass register) {
        this.register = register;
    }

    /**
     * Interactive login. Allows 3 attempts. On success redirects to QuickChat.
     */
    public void log() {
        Scanner scan = new Scanner(System.in);
        int attemptsLeft = 3;

        while (attemptsLeft > 0) {
            System.out.print("Enter username: ");
            String loginUsername = scan.nextLine().trim();

            System.out.print("Enter password: ");
            String loginPassword = scan.nextLine();

            if (checkUserDetails(loginUsername, loginPassword)) {
                System.out.println("\nLogin successful! Welcome " + register.name + " " + register.surname + ", its great to see you again.");
                System.out.println("Welcome to QuickChat, " + register.name + "!");
                // Redirect to quick chat (only once)
                Message messageApp = new Message();
                messageApp.mainMenu();
                return;
            } else {
                attemptsLeft--;
                System.out.println("Incorrect login details. Attempts left: " + attemptsLeft);
                if (attemptsLeft == 0) {
                    System.out.println("You have been locked out. Please restart the program.");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Non-interactive check used by unit tests
     */
    public boolean checkUserDetails(String username, String password) {
        if (username == null || password == null) return false;
        return username.equals(register.username) && password.equals(register.password);
    }
}

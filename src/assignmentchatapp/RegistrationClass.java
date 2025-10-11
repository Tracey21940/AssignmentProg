package assignmentchatapp;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * RegistrationClass
 * - collects user details
 * - validates username, password, cellphone (SA format)
 * - saves last registered user to users.json (simple persistence)
 *
 * The cellphone regex pattern used was suggested via ChatGPT:
 * OpenAI. 2025. ChatGPT (GPT-5) [AI chatbot]. Available at: https://chat.openai.com (Accessed: 19 September 2025).
 */
public class RegistrationClass {

    public String name;
    public String surname;
    public String username;
    public String password;
    public String cellphoneNum;

    private static final String USERS_FILE = "users.json";

    public RegistrationClass() {
        // attempt to load existing user (optional convenience)
        loadLastUser();
    }

    public void userInput() {
        Scanner scan = new Scanner(System.in);

        System.out.print("Please enter your name: ");
        name = scan.nextLine().trim();

        System.out.print("Please enter your surname: ");
        surname = scan.nextLine().trim();

        // username
        do {
            System.out.print("Please enter your username: ");
            username = scan.nextLine().trim();
        } while (!checkUsername(username));

        // password
        do {
            System.out.print("Please enter your password: ");
            password = scan.nextLine();
        } while (!checkPassword(password));

        // cellphone
        do {
            System.out.print("Please enter your cellphone number (format +27XXXXXXXXX): ");
            cellphoneNum = scan.nextLine().trim();
        } while (!checkCellNum(cellphoneNum));

        // Save user to JSON for convenience (overwrites last user)
        saveUser();

        System.out.println("\n=== Login ===");
        login login = new login(this);
        login.log(); // interactive login — redirects to QuickChat on success
    }

    /**
     * Username must contain '_' and be <= 5 characters.
     * Returns true on valid username and prints success/failure message exactly per assignment.
     */
    public boolean checkUsername(String username) {
        if (username != null && username.contains("_") && username.length() <= 5) {
            System.out.println("Username is successfully captured.");
            return true;
        } else {
            System.out.println("Username is not correctly formatted,\nplease ensure that your username contains an underscore\nand is no more than five characters in length.");
            return false;
        }
    }

    /**
     * Password rules:
     * - at least 8 chars
     * - at least one uppercase
     * - at least one digit
     * - at least one special char from ! @ # $ % ^ & * ( ) _ -
     *
     * Prints the exact assignment-style messages.
     */
    public boolean checkPassword(String password) {
        if (password == null) {
            System.out.println("Password is not correctly formatted;\nplease ensure that your password contains at least eight characters,\na capital letter, a number, and a special character.");
            return false;
        }

        boolean hasMinLength = password.length() >= 8;
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasNum = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_-].*");

        if (hasMinLength && hasUppercase && hasNum && hasSpecialChar) {
            System.out.println("Password successfully captured.");
            return true;
        } else {
            System.out.println("Password is not correctly formatted;\nplease ensure that your password contains at least eight characters,\na capital letter, a number, and a special character.");
            return false;
        }
    }

    /**
     * Cellphone check via regex: ^\+27\d{9}$ (e.g. +27831234567)
     * The regex approach ensures exact match for SA international format.
     */
    public boolean checkCellNum(String cellphoneNum) {
        if (cellphoneNum == null) {
            System.out.println("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.");
            return false;
        }
        // Regex: must start with +27 and followed by 9 digits
        String regex = "^\\+27\\d{9}$"; // suggested via ChatGPT (see file header)
        boolean isValid = Pattern.matches(regex, cellphoneNum);

        if (isValid) {
            System.out.println("Cell number successfully captured.");
            return true;
        } else {
            System.out.println("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.");
            return false;
        }
    }

    // ---------------------
    // Simple persistence: save user to users.json as last registered
    // ---------------------
    @SuppressWarnings("unchecked")
    private void saveUser() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("surname", surname);
        obj.put("username", username);
        obj.put("password", password);
        obj.put("cellphoneNum", cellphoneNum);

        try (FileWriter fw = new FileWriter(USERS_FILE)) {
            fw.write(obj.toJSONString());
            fw.flush();
        } catch (IOException e) {
            System.out.println("Warning: Could not save user to file (" + e.getMessage() + ").");
        }
    }

    // Attempt to load last user (non-fatal)
    private void loadLastUser() {
        JSONParser parser = new JSONParser();
        try (FileReader fr = new FileReader(USERS_FILE)) {
            Object parsed = parser.parse(fr);
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                this.name = (String) obj.getOrDefault("name", "");
                this.surname = (String) obj.getOrDefault("surname", "");
                this.username = (String) obj.getOrDefault("username", "");
                this.password = (String) obj.getOrDefault("password", "");
                this.cellphoneNum = (String) obj.getOrDefault("cellphoneNum", "");
            }
        } catch (IOException | ParseException e) {
            // ignore — file may not exist at first run
        }
    }
}

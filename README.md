user.json and message.json appear in the project folder in the netbeans project.
QuickChat Messaging System
Overview

QuickChat is a Java-based messaging application that allows users to compose, validate, send, and store messages. It automatically generates message IDs and message hashes, validates phone numbers, and checks message length. Messages are saved to a JSON file for persistence. The project uses JOptionPane for user input and TestNG for automated testing.

Features

Validates South African phone numbers starting with +27.

Checks message length (maximum 250 characters).

Generates unique message IDs and message hashes.

Stores messages in a local messages.json file.

Provides three actions: Send, Store, and Discard.

Fully tested using TestNG.

Requirements

Java JDK 8 or later

TestNG library

json-simple-1.1.1.jar

How to Run

Add json-simple-1.1.1.jar to your project libraries.

Open Message.java and run the program to simulate message sending via JOptionPane dialogs.

Run MessageNGTest.java with TestNG to execute automated tests.

Expected Output

All tests should pass with:

Tests run: 6, Failures: 0, Skips: 0
BUILD SUCCESS

Example JSON Output

Messages are stored in messages.json in this format:

[
  {
    "MessageID": "1234567890",
    "MessageHash": "00:0:HITONIGHT",
    "Recipient": "+27718693002",
    "Message": "Hi Mike, can you join us for dinner tonight"
  }
]

Author

Developed by Tracey Obi
QuickChat Messaging Assignment — 2025

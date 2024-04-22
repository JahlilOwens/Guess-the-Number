package Project3;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Scanner infromuser = new Scanner(System.in)) { // Allows input for the user
            try {
                System.out.println("Enter the server IP address: ");
                String ipaddress = infromuser.nextLine();// stores the input from the user into the avriable as the ipaddress
                try (Socket clientSocket = new Socket(ipaddress, 15001)) {
                    System.out.println("Server Connected");
                    Scanner infromServer = new Scanner(clientSocket.getInputStream()); // Read input from the server
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // sends the data to the server
                    while (true) {
                        System.out.println("Enter a Number Between 1-1000: ");
                        String guess = infromuser.next();
                        outToServer.writeBytes(guess + "*");// sends the messeage and number to the server as a string
                        outToServer.flush(); // makes sure it reaches the server
                        String numbercheck = infromServer.nextLine(); // gest the message from the server
                        System.out.println("Server says: " + numbercheck);// prints out message
                        if (numbercheck.contains("Correct")) { //starts the process of reading the message
                            System.out.println("Play again? Yes or No: ");
                            String playagain = infromuser.next();
                            if (playagain.equalsIgnoreCase("Yes")) {
                                System.out.println("New game has started.");
                                outToServer.writeBytes(playagain+"*"); // sends the message to the server and then restarts the game
                                outToServer.flush();
                            }
                            else if (playagain.equalsIgnoreCase("No")) {
                                System.out.println("Thank You for Playing");
                                outToServer.writeBytes(playagain+"*");
                                break;// ends the game
                            }
                            else {
                                System.out.println("Invalid Input"); //checks for invalid guesses
                                continue;
                            }
                        }
                        else if (numbercheck.isEmpty()) {
                            System.out.println("invalid input"); // checks for invalid guesses
                        }
                        else {
                            continue;
                        }
                    }
                    clientSocket.close();
                    infromuser.close();
                    infromServer.close();
                }
            }
            catch (NumberFormatException e) {
                System.out.println("There was a number error"); //prints out error message
            }
            catch (Exception e) {
                System.out.println("There was an error"); // prints out error message
            }
        }
    }
}
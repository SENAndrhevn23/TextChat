package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LoginUI {

    public void start() {

        Scanner scanner = new Scanner(System.in);

        try {

            Socket socket =
                    new Socket("localhost", 5555);

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            PrintWriter out =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            System.out.println("=== TextChat ===");

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print(
                    "Login or Register? (L/R): ");

            String choice =
                    scanner.nextLine();

            if(choice.equalsIgnoreCase("L")) {

                out.println("LOGIN:"
                        + username
                        + ":"
                        + password);

            } else {

                out.println("REGISTER:"
                        + username
                        + ":"
                        + password);
            }

            String response = in.readLine();

            if(response.equals("SUCCESS")) {

                System.out.println(
                        "Logged in!");

                ChatUI chatUI =
                        new ChatUI(
                                socket,
                                in,
                                out,
                                username);

                chatUI.start();

            } else {

                System.out.println(
                        "Login failed.");

                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

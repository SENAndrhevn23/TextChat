package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatUI {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String username;

    public ChatUI(
            Socket socket,
            BufferedReader in,
            PrintWriter out,
            String username) {

        this.socket = socket;
        this.in = in;
        this.out = out;
        this.username = username;
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        Thread readerThread =
                new Thread(() -> {

                    try {

                        String message;

                        while((message =
                                in.readLine()) != null) {

                            System.out.println(message);
                        }

                    } catch (IOException e) {

                        System.out.println(
                                "Disconnected.");
                    }
                });

        readerThread.start();

        while(true) {

            String message =
                    scanner.nextLine();

            out.println(message);
        }
    }
}

package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            String request = in.readLine();

            String[] parts = request.split(":");

            String type = parts[0];
            String user = parts[1];
            String pass = parts[2];

            boolean success = false;

            if(type.equals("LOGIN")) {

                success = Server.userManager
                        .login(user, pass);

            } else if(type.equals("REGISTER")) {

                success = Server.userManager
                        .register(user, pass);
            }

            if(success) {

                username = user;

                out.println("SUCCESS");

                Server.broadcast(username
                        + " joined the chat!");

            } else {

                out.println("FAIL");

                socket.close();

                return;
            }

            String message;

            while((message = in.readLine()) != null) {

                Server.broadcast(username
                        + ": " + message);
            }

        } catch (IOException e) {

            System.out.println("Client disconnected.");

        } finally {

            Server.removeClient(this);

            if(username != null) {

                Server.broadcast(username
                        + " left the chat.");
            }

            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
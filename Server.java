package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 5555;
    private static final Set<ClientHandler> clients =
            Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("TextChat Server Started!");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();

                ClientHandler client = new ClientHandler(socket);
                clients.add(client);

                client.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("Enter username:");
            username = in.readLine();

            Server.broadcast(username + " joined the chat!");

            String message;

            while ((message = in.readLine()) != null) {
                Server.broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Server.removeClient(this);

            if (username != null) {
                Server.broadcast(username + " left.");
            }

            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}

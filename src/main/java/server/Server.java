package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {

    public static final int PORT = 5555;

    private static final Set<ClientHandler> clients =
            Collections.synchronizedSet(new HashSet<>());

    public static UserManager userManager =
            new UserManager();

    public static void main(String[] args) {

        System.out.println("TextChat Server Started!");

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            while(true) {

                Socket socket = serverSocket.accept();

                ClientHandler client =
                        new ClientHandler(socket);

                clients.add(client);

                client.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {

        synchronized (clients) {

            for(ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}
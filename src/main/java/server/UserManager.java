package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final String FILE_NAME = "users.txt";

    private final Map<String, String> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    private void loadUsers() {

        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(":");

                if(parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {

        return users.containsKey(username)
                && users.get(username).equals(password);
    }

    public boolean register(String username, String password) {

        if(users.containsKey(username)) {
            return false;
        }

        users.put(username, password);

        try {

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(FILE_NAME, true));

            writer.write(username + ":" + password);
            writer.newLine();

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
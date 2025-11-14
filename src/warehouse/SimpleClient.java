package warehouse;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket("127.0.0.1", 5050)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            Scanner sc = new Scanner(System.in);

            // ---- READ WELCOME ----
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("Commands")) break;
            }

            // ---- COMMAND LOOP ----
            while (true) {
                System.out.print("> ");
                String cmd = sc.nextLine();
                out.println(cmd);

                if (cmd.equalsIgnoreCase("QUIT"))
                    break;

                // read all responses
                while ((line = in.readLine()) != null) {
                    if (line.trim().isEmpty()) break;
                    System.out.println(line);
                    if (!in.ready()) break;
                }
            }
        }
    }
}

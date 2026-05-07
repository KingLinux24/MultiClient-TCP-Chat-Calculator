import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        try {
            // Get client name from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            this.clientName = scanner.nextLine().trim();

            if (clientName.isEmpty()) {
                clientName = "Anonymous";
            }

            // Connect to server
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server!");
            System.out.println("Commands:");
            System.out.println("  - Type messages to chat");
            System.out.println("  - Use 'CALC:num1:num2:operation' for calculator");
            System.out.println("    Example: CALC:10:5:+ or CALC:20:3:/ or CALC:25:4:%");
            System.out.println("  - Type 'exit' to disconnect\n");

            // Send client name to server
            out.println("NAME:" + clientName);

            // Start listening for server messages in a separate thread
            new Thread(() -> listenToServer(scanner)).start();

            // Send messages to server
            String userInput;
            while (true) {
                System.out.print(clientName + " > ");
                userInput = scanner.nextLine().trim();

                if (userInput.isEmpty()) {
                    continue;
                }

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting...");
                    break;
                }

                out.println(userInput);
            }

            scanner.close();
            disconnect();

        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private void listenToServer(Scanner scanner) {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("\n" + message);
                System.out.print(clientName + " > ");
            }
        } catch (IOException e) {
            if (socket != null && !socket.isClosed()) {
                System.err.println("Connection lost: " + e.getMessage());
            }
        }
    }

    private void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

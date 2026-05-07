import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<ClientHandler> clientHandlers;
    private String clientName;
    private boolean isConnected;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        this.isConnected = true;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error initializing streams: " + e.getMessage());
            isConnected = false;
        }
    }

    @Override
    public void run() {
        try {
            // Get client name
            String clientNameMsg = in.readLine();
            if (clientNameMsg != null && clientNameMsg.startsWith("NAME:")) {
                this.clientName = clientNameMsg.substring(5);
                System.out.println("Client " + clientName + " joined the chat");
                
                // Notify other clients
                broadcastSystemMessage(clientName + " joined the chat");
            }

            String message;
            while (isConnected && (message = in.readLine()) != null) {
                if (message.isEmpty()) continue;

                // Check if it's a calculator request
                if (message.startsWith("CALC:")) {
                    handleCalculatorRequest(message);
                } else {
                    // Regular chat message
                    String broadcastMsg = "[" + clientName + "]: " + message;
                    System.out.println(broadcastMsg);
                    
                    // Send to all clients except sender
                    synchronized (clientHandlers) {
                        for (ClientHandler handler : clientHandlers) {
                            if (handler != this) {
                                handler.sendMessage(broadcastMsg);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling client " + clientName + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void handleCalculatorRequest(String request) {
        try {
            // Format: CALC:num1:num2:operation
            String[] parts = request.substring(5).split(":");
            
            if (parts.length != 3) {
                sendMessage("ERROR: Invalid calculator request format");
                return;
            }

            double num1 = Double.parseDouble(parts[0].trim());
            double num2 = Double.parseDouble(parts[1].trim());
            String operation = parts[2].trim();

            double result = Calculator.calculate(num1, num2, operation);
            String response = "[CALCULATOR] " + num1 + " " + operation + " " + num2 + " = " + result;
            
            sendMessage(response);
            System.out.println("Calculator request from " + clientName + ": " + response);

        } catch (NumberFormatException e) {
            sendMessage("ERROR: Invalid numbers provided");
        } catch (IllegalArgumentException e) {
            sendMessage("ERROR: " + e.getMessage());
        }
    }

    private void broadcastSystemMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage("[SYSTEM]: " + message);
            }
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void disconnect() {
        isConnected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            
            clientHandlers.remove(this);
            
            if (clientName != null) {
                System.out.println("Client " + clientName + " disconnected");
                broadcastSystemMessage(clientName + " left the chat");
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }
}

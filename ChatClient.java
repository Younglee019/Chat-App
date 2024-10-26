import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Server address
    private static final int PORT = 12345; // Port number
    private PrintWriter out;

    public static void main(String[] args) {
        new ChatClient().start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new IncomingMessageHandler(socket)).start();
            Scanner scanner = new Scanner(System.in);
            String message;

            System.out.println("Enter messages to send (type 'exit' to quit):");
            while (true) {
                message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingMessageHandler implements Runnable {
        private BufferedReader in;

        public IncomingMessageHandler(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

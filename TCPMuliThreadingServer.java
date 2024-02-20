import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPMuliThreadingServer {
    public static void main(String[] args) {
        final int MAX_CLIENTS = 10;
        int clientCount = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;

                if (clientCount <= MAX_CLIENTS) {
                    System.out.println("Client " + clientCount + " connected.");

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                } else {
                    System.out.println("Maximum number of clients reached. Closing connection.");
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    // Simulate processing delay
                    Thread.sleep(1000);

                    // Reverse the input string
                    String reversedString = new StringBuilder(inputLine).reverse().toString();

                    // Send the reversed string back to the client
                    writer.println(reversedString);
                }

                reader.close();
                writer.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTCPServer {
    public static void main(String args[]) {
        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(2);
            int serverPort = 6666; // numero da porta do servidor
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (serverSocket.isBound()) {
                System.out.println("Esperando o estabelecimento da conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                threadPool.submit(() -> {
                    handler.run();
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("Conexao feita com: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}
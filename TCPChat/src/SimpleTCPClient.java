import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        try {
            // Tenta criar um socket de comunicação com o server e obter canais de entrada e saída:
            System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
            socket = new Socket(serverIp, serverPort);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // Aguarda a mensagem ser digitada na entrada padrão:
            System.out.println("[C2] Conexão foi estabelecida, eu sou o lado client: " + socket.getLocalSocketAddress());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Digite uma mensagem (ou 'bye' para sair): ");
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("bye")) {
                    String message = "Tchau server :)";
                    output.writeUTF(message);
                    // Recebendo resposta do servidor
                    System.out.println(input.readUTF());
                    break;
                }

                // Envia mensagem para o servidor no canal de saída
                System.out.println("[C3] Enviando sua mensagem para servidor");
                output.writeUTF(msg);
                System.out.println("[C4] Sua mensagem foi enviada, aguardando a resposta...");

                // Recebendo resposta do server
                String response = input.readUTF();
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // stopando a conexão:
            stop();
        }
    }

    public void stop() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Coloque o endereço IP do servidor aqui:
        String serverIp = "127.0.0.1";
        // COloque a porta do servidor aqui:
        int serverPort = 6666;
        try {
            // Cria e da run() no client:
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            Server servidor = new Server(5081);
            servidor.executar();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private final int porta;

    public Server(int porta) throws IOException {
        this.porta = porta;
    }

    public void executar() throws IOException {
        while(true){
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Ouvindo a porta " + porta);

            Socket cliente = null;
            while ((cliente = servidor.accept()) != null) {
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
                DataInputStream entrada = new DataInputStream(cliente.getInputStream());

                String opr = entrada.readUTF();
                int num1 = entrada.readInt();
                int num2 = entrada.readInt();

                double resultado = 0;
                switch (opr) {
                    case "+":
                        resultado = num1 + num2;
                        break;
                    case "-":
                        resultado = num1 - num2;
                        break;
                    case "*":
                        resultado = num1 * num2;
                        break;
                    case "/":
                        resultado = num1 / (double) num2;
                        break;

                }
                saida.writeUTF(resultado + "");
                saida.flush();
                saida.close();
                entrada.close();
            }
            servidor.close();
        }
    }
}
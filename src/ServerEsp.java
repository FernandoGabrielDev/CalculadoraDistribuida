import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEsp {

    public static void main(String[] args) {
        try {
            ServerEsp serverEsp = new ServerEsp(5090);
            serverEsp.executaresp();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private final int porta;

    public ServerEsp(int porta) throws IOException {
        this.porta = porta;
    }

    public void executaresp() throws IOException {
        ServerSocket serverEsp = new ServerSocket(porta);
        System.out.println("Ouvindo a porta " + porta);
        Socket cliente = null;

        while ((cliente = serverEsp.accept()) != null) {
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

            DataOutputStream saidaesp = new DataOutputStream(cliente.getOutputStream());
            DataInputStream entradaesp = new DataInputStream(cliente.getInputStream());

            String opr = entradaesp.readUTF();
            int num1 = entradaesp.readInt();
            int num2 = entradaesp.readInt();

            double resultadoesp = 0;
            switch (opr) {
                case "%":
                    resultadoesp = (double) (num1 * num2)/100 ;
                    break;
                case "r":
                    resultadoesp = Math.pow(num1, 1.0/num2);
                    break;
                case "^":
                    resultadoesp = Math.pow(num1, num2);
                    break;
            }
            saidaesp.writeUTF(resultadoesp + "");
            saidaesp.flush();
            saidaesp.close();
            entradaesp.close();
        }
        serverEsp.close();
    }
}
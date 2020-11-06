import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner ler = new Scanner(System.in);
        String calc = " ";
        while (calc != "Sair") {
        System.out.println("------ CALCULADORA DISTRIBUÍDA ------\n");
        System.out.println("Leia as Regras de como utilizar a calculadora!\n\n");
        System.out.println("Informe o Cálculo desejado\n");
        calc = ler.nextLine();
        String[] result = calc.split(" ");

        if (calc.equals("Sair")){
            System.exit(0);
        }else if(result.length <= 1){
            System.out.println("O Cálculo NÃO Válido\nCaso tenha alguma dúvida sobre a utilização da calculadora,\nLeia as instruções no README.md");
        }

            switch (result[1]) {
                case "+", "-", "*", "/":
                    try {
                        Client cliente = new Client("127.0.0.1", 5081);
                        cliente.executar(calc);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    break;
                case "%", "r", "^":
                    try {
                        Client clienteesp = new Client("127.0.0.1", 5090);
                        clienteesp.executaresp(calc);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private final String host;
    private final int porta;

    public Client(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executar(String calc) throws IOException {
        Scanner ler = new Scanner(System.in);
        Socket cliente = new Socket(host, porta);
        System.out.println("------ O cliente se conectou ao servidor Simples! ------");

        DataInputStream entrada = new DataInputStream(cliente.getInputStream());
        DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());

        String[] result = calc.split(" ");
        String opr = result[1];
        int num1 = Integer.parseInt(result[0]);
        int num2 = Integer.parseInt(result[2]);

        saida.writeUTF(opr);
        saida.writeInt(num1);
        saida.writeInt(num2);
        saida.flush();

        String mensagem = entrada.readUTF();
        System.out.println(calc + " => " + mensagem + "\n");
        System.out.println("Digite '1' para realizar um novo Cálculo ou '0' para finalizar\n");
        int sair = Integer.parseInt(ler.nextLine());
        if (sair == 0){
            System.exit(0);
            saida.flush();
        }else if(sair == 1){
            main(result);
        }else{
            System.out.println("Favor informar uma opção válida\n");
        }

        entrada.close();
        saida.close();
        cliente.close();
    }

    public void executaresp(String calc) throws IOException {
        Scanner ler = new Scanner(System.in);
        Socket clienteesp = new Socket(host, porta);
        System.out.println("O cliente se conectou ao servidor Especial!");

        DataInputStream entradaesp = new DataInputStream(clienteesp.getInputStream());
        DataOutputStream saidaesp = new DataOutputStream(clienteesp.getOutputStream());

        String[] result = calc.split(" ");
        String opr = result[1];
        int num1 = Integer.parseInt(result[0]);
        int num2 = Integer.parseInt(result[2]);

        saidaesp.writeUTF(opr);
        saidaesp.writeInt(num1);
        saidaesp.writeInt(num2);
        saidaesp.flush();

        String mensagemesp = entradaesp.readUTF();
        System.out.println(calc + " => " + mensagemesp + "\n");
        System.out.println("Digite '1' para realizar um novo Cálculo ou '0' para finalizar\n");
        int sair = Integer.parseInt(ler.nextLine());
        if (sair == 0){
            System.exit(0);
            saidaesp.flush();
        }else if(sair == 1){
            main(result);
        }else{
            System.out.println("Favor informar uma opção válida\n");
        }

        entradaesp.close();
        saidaesp.close();
        clienteesp.close();
    }
}

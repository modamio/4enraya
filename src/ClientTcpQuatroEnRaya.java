import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcpQuatroEnRaya extends Thread{
    private String jugador;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Scanner scin;
    private boolean continueConnected;


    private Tauler t;
    private Jugada j;

    public ClientTcpQuatroEnRaya(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        scin = new Scanner(System.in);
        j = new Jugada();
    }
    public void run() {
        String msg = null;
        while(continueConnected){
            //Llegir info del servidor (estat del tauler)
            t = getRequest();

            //Crear codi de resposta a missatge
            switch (t.resultat) {
                case 3:	msg = "Benvingut al joc " + jugador + " - " + t.getNumPlayers(); break;
                case 2:	msg = "Continua Jugant"; break;
                case 1:
                    System.out.println("Empat");
                    System.out.println(t);
                    continueConnected = false;
                    continue;
                case 0:
                    System.out.println("Correcte");
                    System.out.println(t);
                    continueConnected = false;
                    continue;
            }
            System.out.println(msg);
            System.out.println(t);
            if((t.resultat != 0 || t.resultat !=1)) {
                j.jugador = jugador;
                t.toString();
                System.out.println("Entra la columna donde quieres meter la ficha: ");
                j.columna = scin.nextInt();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(j);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        try {
            MulticastSocketClient multicastSocketClient = new MulticastSocketClient(5556,"224.0.0.12");
            multicastSocketClient.runClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close(socket);

    }
    private Tauler getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            t = (Tauler) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
    public void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientTcpQuatroEnRaya.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        String jugador, ipSrv;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Ip del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();

        ClientTcpQuatroEnRaya clientTcp = new ClientTcpQuatroEnRaya(ipSrv,5558);
        clientTcp.jugador = jugador;
        clientTcp.start();
    }
}

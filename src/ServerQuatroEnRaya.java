import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerQuatroEnRaya {
    private int port;
    private QuatroEnRaya quatroEnRaya;
    private Tauler t;

    public Tauler getT() {
        return t;
    }

    public void setT(Tauler t) {
        this.t = t;
    }

    private ServerQuatroEnRaya(int port, Tauler tauler) {
        this.port = port;
        this.quatroEnRaya = new QuatroEnRaya();
        t = tauler;
    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                t.addNUmPlayers();
                ThreadServerQuatroEnRaya FilServidor = new ThreadServerQuatroEnRaya(clientSocket, quatroEnRaya, t);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerQuatroEnRaya.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        Tauler tauler = new Tauler();
        ServerQuatroEnRaya srv = new ServerQuatroEnRaya(5558,tauler);
        Thread thread = new Thread(()->srv.listen());
        thread.start();
        MulticastSocketServer multicastSocketServer = new MulticastSocketServer(5556,"224.0.0.12",tauler);
        multicastSocketServer.runServer();
    }
}

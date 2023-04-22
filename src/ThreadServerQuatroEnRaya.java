import java.io.*;
import java.net.Socket;

public class ThreadServerQuatroEnRaya implements Runnable{

    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private QuatroEnRaya quatroEnRaya;
    private Tauler tauler;
    private boolean acabat;
    public ThreadServerQuatroEnRaya(Socket clientSocket, QuatroEnRaya quatroEnRaya, Tauler t) throws IOException {
        this.clientSocket = clientSocket;
        this.quatroEnRaya = quatroEnRaya;
        tauler = t;
        //Al inici de la comunicació el resultat ha de ser diferent de 0(encertat)
        tauler.resultat = 3;
        acabat = false;
        //Enllacem els canals de comunicació
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        System.out.println("canals i/o creats amb un nou jugador");
    }
    @Override
    public void run() {
        Jugada j = null;
        try {
            while(!acabat) {

                //Enviem tauler al jugador

                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(tauler);
                oos.flush();

                //Llegim la jugada
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    j = (Jugada) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("jugada: " + j.jugador + "->" + j.columna);
                if(!tauler.map_jugadors.containsKey(j.jugador)) tauler.map_jugadors.put(j.jugador, j.columna);
                else {
                    tauler.map_jugadors.put(j.jugador, j.columna);
                }

                //comprobar la jugada i actualitzar tauler amb el resultat de la jugada
                tauler.resultat = quatroEnRaya.estat(j,tauler);
                if(tauler.resultat == 0) {
                    acabat = true;
                    System.out.println(j.jugador + " l'ha encertat");
                    tauler.acabats++;
                } else if (tauler.resultat == 1){
                    acabat = true;
                    System.out.println("Han empatat");
                    tauler.acabats++;
                }
            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        //Enviem últim estat del tauler abans de acabar amb la comunicació i acabem
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(tauler);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSocketServer {
    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;

    Tauler tauler;


    public MulticastSocketServer(int portValue, String strIp, Tauler tauler) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        this.tauler = tauler;

    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData;

        while(continueRunning){
            sendingData = toByte();
            packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
            socket.send(packet);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }


        }
        socket.close();
    }
    public byte[] toByte() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(tauler);
        return byteArrayOutputStream.toByteArray();
    }
}

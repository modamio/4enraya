import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class MulticastSocketClient {
    private boolean continueRunning = false;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    NetworkInterface netIf;
    InetSocketAddress group;


    public MulticastSocketClient(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //netIf = NetworkInterface.getByName("enp1s0");
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
    }

    public void runClient() throws IOException{
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];

        socket.joinGroup(group,netIf);
        System.out.printf("Connectat a %s:%d%n",group.getAddress(),group.getPort());

        while(!continueRunning){
            packet = new DatagramPacket(receivedData, receivedData.length);
            socket.setSoTimeout(5000);
            try{
                socket.receive(packet);
                System.out.println("Paquete recivido");
                continueRunning = getData(packet.getData());
            }catch(SocketTimeoutException | ClassNotFoundException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group,netIf);
        socket.close();
    }

    protected boolean getData(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Tauler tauler = (Tauler) objectInputStream.readObject();
        System.out.println(tauler.toString());
        if (tauler.acabats > 0){
            System.out.println("La partida ha finalizado");
            return true;
        }
        return  false;


    }
}

import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class nClient extends Thread {
    private int netticks;
    static int hasDisconnected;
    static int msgreceived;
    static int sfxreceived;
    static int cmdreceived;
    static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private static nClient instance = null;

    public static nClient instance() {
        if(instance == null)
            instance = new nClient();
        return instance;
    }

    private nClient() {
        netticks = 0;
        msgreceived = 0;
        sfxreceived = 0;
        cmdreceived = 0;
    }

    public static void processPackets() {
        try {
            while(receivedPackets.size() > 1) {
                //this means all other packets are thrown out, bad in long run
                receivedPackets.remove();
            }
            if(receivedPackets.size() > 0) {
                DatagramPacket receivePacket = receivedPackets.peek();
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug(String.format("CLIENT RCV [%d]: %s",
                        receiveDataString.trim().length(), receiveDataString.trim()));
                nReceive.processReceiveDataString(receiveDataString);
                receivedPackets.remove();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        int retries = 0;
        while(true) {
            try {
                netticks += 1;
                if(uiInterface.nettickcounterTime < uiInterface.gameTime) {
                    uiInterface.netReport = netticks;
                    netticks = 0;
                    uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                }
                if(receivedPackets.size() < 1 && hasDisconnected != 1) {
                    InetAddress IPAddress = InetAddress.getByName(sVars.get("joinip"));
                    String sendDataString = nSend.createSendDataString();
                    byte[] clientSendData = sendDataString.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                            sVars.getInt("joinport"));
                    if(uiInterface.clientSocket == null || uiInterface.clientSocket.isClosed()) {
                        uiInterface.clientSocket = new DatagramSocket();
                        uiInterface.clientSocket.setSoTimeout(sVars.getInt("timeout"));
                    }
                    uiInterface.clientSocket.send(sendPacket);
                    xCon.instance().debug("CLIENT SND [" + clientSendData.length + "]:" + sendDataString);
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(sendDataString);
                    if(clientmap.keySet().contains("quit") || clientmap.keySet().contains("disconnect")) {
                        hasDisconnected = 1;
                    }
                    else {
                        byte[] clientReceiveData = new byte[sVars.getInt("rcvbytesclient")];
                        DatagramPacket receivePacket = new DatagramPacket(clientReceiveData, clientReceiveData.length);
                        uiInterface.clientSocket.receive(receivePacket);
                        receivedPackets.add(receivePacket);
                    }
                }
                uiInterface.networkTime = uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("rateclient"));
                if(sVars.getInt("rateclient") > 1000)
                    sleep(0, (int)(uiInterface.networkTime-uiInterface.gameTime));
                else
                    sleep(uiInterface.networkTime-uiInterface.gameTime);
                retries = 0;
            }
            catch(Exception e) {
                retries++;
                e.printStackTrace();
                if(retries > sVars.getInt("netrcvretries"))
                {
                    xCon.ex("disconnect");
                    xCon.ex("echo Lost connection to server");
                }
            }
        }
    }
}

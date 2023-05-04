import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class eGameLogicServerNet implements eGameLogic {
    private int ticks;
    private long nextsecondnanos;
    private eGameSession parentSession;
    private DatagramSocket serverSocket;

    public void setParentSession(eGameSession session) {
        parentSession = session;
    }

    public eGameLogicServerNet() {
        ticks = 0;
        nextsecondnanos = 0;
        try {
            serverSocket = new DatagramSocket(cServerLogic.listenPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(){

    }

    @Override
    public void input() {

    }

    @Override
    public void update() {
        try {

        }
        catch (Exception e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
        ticks++;
        long theTime = System.nanoTime();
        if(nextsecondnanos < theTime) {
            nextsecondnanos = theTime + 1000000000;
            uiInterface.netReportServer = ticks;
            ticks = 0;
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void disconnect() {
        parentSession.destroy();
    }

    @Override
    public void cleanup() {
        sSettings.IS_SERVER = false;
        cServerLogic.netServerThread = null;
        serverSocket.close();
    }
}

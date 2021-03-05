import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

public interface fNetBase {
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    void processPackets();
    void disconnect();
    void addNetCmd(String cmd);
    void readData(String rcvDataString);
}

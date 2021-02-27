import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

public interface fNet {
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    void addNetCmd(String cmd);
    void processPackets();
}

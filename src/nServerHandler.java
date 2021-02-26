import java.net.Socket;

public class nServerHandler extends Thread {
    Socket clientSocket;

    public void run() {

    }

    public nServerHandler(Socket socket) {
        clientSocket = socket;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class nReceive {
    public static void processReceiveDataString(String receiveDataString) {
        if(sSettings.net_server) {
            nServer.readData(receiveDataString);
        }
        else if(sSettings.net_client) {
            nClient.readData(receiveDataString);
        }
    }
}

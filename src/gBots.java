public class gBots {
    public class gBot {

    }
    public class gBotNode {
        int id;
        int x;
        int y;
        public gBotNode(int x, int y) {
            x = x;
            y = y;
        }
    }
    public class gBotEdge {
        gBotNode n1;
        gBotNode n2;
        public gBotEdge(gBotNode n1, gBotNode n2) {
            n1 = n1;
            n2 = n2;
        }
    }
}

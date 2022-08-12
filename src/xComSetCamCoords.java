public class xComSetCamCoords extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 3)
            return "0";
        gCamera.argSet.put("coordx", args[1]);
        gCamera.argSet.put("coordy", args[2]);
        return "1";
    }
}
public class xComPutItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gItemFactory factory = gItemFactory.instance();
            String itemTitle = toks[1];
            if(factory.itemLoadMap.containsKey(itemTitle)) {
                gDoableItemReturn itemReturn = factory.itemLoadMap.get(itemTitle);
                String argString = fullCommand.replace(toks[0] + " ",
                        "").replace(toks[1] + " ", "");
                String[] args = argString.split(" ");

                String rawX = args[0];
                String rawY = args[1];

                if(rawX.charAt(0) == '$') {
                    int transformedX;
                    String[] rxtoksadd = rawX.split("\\+");
                    String[] rxtokssub = rawX.split("-");
                    if(rxtoksadd.length > 1) {
                        int rxmod0 = sVars.getInt(rxtoksadd[0]);
                        int rxmod1 = Integer.parseInt(rxtoksadd[1]);
                        transformedX = rxmod0+rxmod1;
                    }
                    else if(rxtokssub.length > 1) {
                        int rxmod0 = sVars.getInt(rxtokssub[0]);
                        int rxmod1 = Integer.parseInt(rxtokssub[1]);
                        transformedX = rxmod0-rxmod1;
                    }
                    else {
                        transformedX = sVars.getInt(rawX);
                    }
                    args[0] = Integer.toString(transformedX);
                }

                if(rawY.charAt(0) == '$') {
                    int transformedY;
                    String[] rytoksadd = rawY.split("\\+");
                    String[] rytokssub = rawY.split("-");
                    if(rytoksadd.length > 1) {
                        int rymod0 = sVars.getInt(rytoksadd[0]);
                        int rymod1 = Integer.parseInt(rytoksadd[1]);
                        transformedY = rymod0+rymod1;
                    }
                    else if(rytokssub.length > 1) {
                        int rymod0 = sVars.getInt(rytokssub[0]);
                        int rymod1 = Integer.parseInt(rytokssub[1]);
                        transformedY = rymod0-rymod1;
                    }
                    else {
                        transformedY = sVars.getInt(rawY);
                    }
                    args[1] = Integer.toString(transformedY);
                }
                gItem newItem = itemReturn.getItem(args);
                itemReturn.storeItem(newItem, eManager.currentMap.scene);
            }
        }
        return "usage: putitem <ITEM_TITLE> <args>";
    }
}

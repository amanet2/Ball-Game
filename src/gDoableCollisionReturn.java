public class gDoableCollisionReturn {
    public gCollision getCollision(String[] args) {
        String blockid = args[0];
        String prefabid = args[1];
        if (blockid.charAt(0) == '$') {
            int transformed;
            String[] rxtoksadd = blockid.split("\\+");
            String[] rxtokssub = blockid.split("-");
            if (rxtoksadd.length > 1) {
                int rxmod0 = sVars.getInt(rxtoksadd[0]);
                int rxmod1 = Integer.parseInt(rxtoksadd[1]);
                transformed = rxmod0 + rxmod1;
            } else if (rxtokssub.length > 1) {
                int rxmod0 = sVars.getInt(rxtokssub[0]);
                int rxmod1 = Integer.parseInt(rxtokssub[1]);
                transformed = rxmod0 - rxmod1;
            } else {
                transformed = sVars.getInt(blockid);
            }
            blockid = Integer.toString(transformed);
        }

        if (prefabid.charAt(0) == '$') {
            int transformed;
            String[] rxtoksadd = prefabid.split("\\+");
            String[] rxtokssub = prefabid.split("-");
            if (rxtoksadd.length > 1) {
                int rxmod0 = sVars.getInt(rxtoksadd[0]);
                int rxmod1 = Integer.parseInt(rxtoksadd[1]);
                transformed = rxmod0 + rxmod1;
            } else if (rxtokssub.length > 1) {
                int rxmod0 = sVars.getInt(rxtokssub[0]);
                int rxmod1 = Integer.parseInt(rxtokssub[1]);
                transformed = rxmod0 - rxmod1;
            } else {
                transformed = sVars.getInt(prefabid);
            }
            prefabid = Integer.toString(transformed);
        }

        String[] rawXargs = args[2].split("\\.");
        for(int i = 0; i < rawXargs.length; i++) {
            String rawX = rawXargs[i];
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
                rawXargs[i] = Integer.toString(transformedX);
            }
        }

        String[] rawYargs = args[3].split("\\.");
        for (int i = 0; i < rawYargs.length; i++) {
            String rawY = rawYargs[i];
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
                rawYargs[i] = Integer.toString(transformedY);
            }
        }

        int[] xarr = new int[rawXargs.length];
        for(int i = 0; i < xarr.length; i++) {
            xarr[i] = Integer.parseInt(rawXargs[i]);
        }
        int[] yarr = new int[rawYargs.length];
        for(int i = 0; i < yarr.length; i++) {
            yarr[i] = Integer.parseInt(rawYargs[i]);
        }
        gCollision coll = new gCollision(xarr, yarr);
        coll.put("id", blockid);
        coll.put("prefabid", prefabid);
        return coll;
    }
}

public class xComEditorSetNewTileQuickDims extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gThingTile t = cEditorLogic.state.newTile;
            int[] ods = new int[]{t.getInt("dimw"), t.getInt("dimh")};
            t.put("dimw", toks[1]);
            t.put("dimh", toks[2]);
            int[] d = new int[]{t.getInt("dimw")-ods[0], t.getInt("dimh")-ods[1]};

            if(t.getInt("dim0w") > 0) {
                t.putInt("dim0w", t.getInt("dimw"));
            }
            if(t.getInt("dim0h") > 0) {
                if(t.getInt("dim3h") < 1 && t.getInt("dim2h") < 1) { //basically detects topdownblock/roof
                    t.putInt("dim0h", t.getInt("dim0h")+d[1]);
                }
            }
            if(t.getInt("dim1w") > 0) {
                t.putInt("dim1w", t.getInt("dimw"));
            }
            if(t.getInt("dim1h") > 0) {

            }
            if(t.getInt("dim2w") > 0) {
                t.putInt("dim2w", t.getInt("dimw"));
            }
            if(t.getInt("dim2h") > 0) {
                t.putInt("dim2h", t.getInt("dimh"));
            }
            if(t.getInt("dim3w") > 0) {
                t.putInt("dim3w", t.getInt("dimw"));
            }
            if(t.getInt("dim3h") > 0) {

            }
            if(t.getInt("dim4w") > 0) {
                t.putInt("dim4w", t.getInt("dimw"));
            }
            if(t.getInt("dim4h") > 0) {

            }
            if(t.getInt("dim5w") > 0) {

            }
            if(t.getInt("dim5h") > 0) {
                t.putInt("dim5h", t.getInt("dimh") - t.getInt("dim0h") - t.getInt("dim3h")
                        - t.getInt("dim4h"));
            }
            if(t.getInt("dim6w") > 0) {
                if(t.getInt("dim5w") < 1 && t.getInt("dim2w") < 1) { //basically detects sidescrollerblock
                    t.putInt("dim6w", t.getInt("dim6w") + d[0]);
                }
            }
            if(t.getInt("dim6h") > 0) {
                t.putInt("dim6h", t.getInt("dimh") - t.getInt("dim0h") - t.getInt("dim3h")
                        - t.getInt("dim4h"));
            }

        }
        return "";
    }
}


public class gTiles {
    static String[] tile_selection = new String[] {
        "topLeftWall",
        "topWall",
        "topRightWall",
        "rightWall",
        "bottomRightWall",
        "bottomWall",
        "bottomLeftWall",
        "leftWall",
        "lrtunnel",
        "tbtunnel",
        "tbox",
        "rbox",
        "bbox",
        "lbox",
        "boxed",
        "topdownBlock",
        "topdownRoof",
        "cornerUR",
        "cornerBR",
        "cornerBL",
        "cornerUL",
        "sidescrollerBlock",
        "floor",
        "empty"
    };

    static void setCreateDimsForTile(String s) {
        if(s.equals("topLeftWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-100);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("topWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("topRightWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-100);
        }
        if(s.equals("rightWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh"));
        }
        if(s.equals("bottomRightWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-250);
        }
        if(s.equals("bottomWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("bottomLeftWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-250);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("leftWall")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("tbtunnel")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh"));
        }
        if(s.equals("lrtunnel")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("tbox")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-100);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-100);
        }
        if(s.equals("rbox")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-350);
        }
        if(s.equals("bbox")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-250);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-250);
        }
        if(s.equals("lbox")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-350);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("boxed")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", 100);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim3h", 100);
            cEditorLogic.state.newTile.putInt("dim4w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim4h", 150);
            cEditorLogic.state.newTile.putInt("dim5w", 100);
            cEditorLogic.state.newTile.putInt("dim5h", cEditorLogic.state.newTile.getInt("dimh")-350);
            cEditorLogic.state.newTile.putInt("dim6w", 100);
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh")-350);
        }
        if(s.equals("topdownBlock")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", cEditorLogic.state.newTile.getInt("dimh")-150);
            cEditorLogic.state.newTile.putInt("dim1w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim1h", 150);
            cEditorLogic.state.newTile.putInt("dim2w", 0);
            cEditorLogic.state.newTile.putInt("dim2h", 0);
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("topdownRoof")) {
            cEditorLogic.state.newTile.putInt("dim0w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim0h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", 0);
            cEditorLogic.state.newTile.putInt("dim2h", 0);
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("cornerUR")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", -1);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("cornerBR")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", -2);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("cornerBL")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", -3);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("cornerUL")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", -4);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("sidescrollerBlock")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", 0);
            cEditorLogic.state.newTile.putInt("dim2h", 0);
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim6h", cEditorLogic.state.newTile.getInt("dimh"));
        }
        if(s.equals("floor")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", cEditorLogic.state.newTile.getInt("dimw"));
            cEditorLogic.state.newTile.putInt("dim2h", cEditorLogic.state.newTile.getInt("dimh"));
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
        if(s.equals("empty")) {
            cEditorLogic.state.newTile.putInt("dim0w", 0);
            cEditorLogic.state.newTile.putInt("dim0h", 0);
            cEditorLogic.state.newTile.putInt("dim1w", 0);
            cEditorLogic.state.newTile.putInt("dim1h", 0);
            cEditorLogic.state.newTile.putInt("dim2w", 0);
            cEditorLogic.state.newTile.putInt("dim2h", 0);
            cEditorLogic.state.newTile.putInt("dim3w", 0);
            cEditorLogic.state.newTile.putInt("dim3h", 0);
            cEditorLogic.state.newTile.putInt("dim4w", 0);
            cEditorLogic.state.newTile.putInt("dim4h", 0);
            cEditorLogic.state.newTile.putInt("dim5w", 0);
            cEditorLogic.state.newTile.putInt("dim5h", 0);
            cEditorLogic.state.newTile.putInt("dim6w", 0);
            cEditorLogic.state.newTile.putInt("dim6h", 0);
        }
    }
}

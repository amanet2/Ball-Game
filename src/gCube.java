import javafx.geometry.Point3D;

public class gCube extends gMesh {
    public gCube(int x , int y, int z) {
        vertices = new Point3D[]{
            new Point3D(-1, 1, 1),
            new Point3D(1, 1, 1),
            new Point3D(-1, -1, 1),
            new Point3D(-1, -1, -1),
            new Point3D(-1, 1, -1),
            new Point3D(1, 1, -1),
            new Point3D(1, -1, 1),
            new Point3D(1, -1, -1)
        };
        coords3 = new Point3D(x, y, z);
        rotation3 = new Point3D(0, 0, 0);
    }
}

import javafx.geometry.Point3D;
import javafx.scene.transform.Transform;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class gDevice {
    public static int[] backBuffer = new int[sSettings.width*sSettings.height*4];

    public static void putPixel(int x, int y, Color color) {
        var index = (x + y * sSettings.width) * 4;
        backBuffer[index] =  color.getRed();
        backBuffer[index + 1] =  color.getBlue();
        backBuffer[index + 2] = color.getGreen();
        backBuffer[index + 3] = color.getAlpha();
    }

//    public Point2D project(Point3D coord, int[][] transformMatrix) {
//        // transforming the coordinates
//        var point = Vector3.TransformCoordinate(coord, transMat);
//        var pointa = Transform.affine()
//        // The transformed coordinates will be based on coordinate system
//        // starting on the center of the screen. But drawing on screen normally starts
//        // from top left. We then need to transform them again to have x:0, y:0 on top left.
//        var x = point.getX * sSettings.width + sSettings.width / 2.0f;
//        var y = -point.Y * sSettings.height + sSettings.height / 2.0f;
//    }

    public static Image getImageFromBuffer(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) image.getData();
        for(int i = 0; i < backBuffer.length; i+=4) {
            backBuffer[i] = Color.BLACK.getRed();
            backBuffer[i+1] = Color.BLACK.getGreen();
            backBuffer[i+2] = Color.BLACK.getBlue();
            backBuffer[i+3] = Color.BLACK.getAlpha();
        }
        raster.setPixels(0,0,width,height, backBuffer);
        return image;
    }

    public static void drawPoint(Point2D point) {
        // Clipping what's visible on screen
        if (point.getX() >= 0 && point.getY() >= 0
                && point.getX() < sSettings.width && point.getY() < sSettings.height) {
            // Drawing a yellow point
            putPixel((int)point.getX(), (int)point.getY(), new Color(1.0f, 1.0f, 0.0f, 1.0f));
        }
    }
}

package Pacman;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

abstract public class BoardObject {
    protected int positionX;
    protected int positionY;
    static int width = 21;
    static int height = 21;
    protected String texturePath;
    Rectangle drawnRectangle;

    protected int step;

    public BoardObject(int x, int y) {
        positionX = x;
        positionY = y;
    }

    public boolean CutsWithObjStartingAt(int x, int y)
    {
        if (Contains(x,y)) return true;
        if (Contains(x+BoardObject.GetWidth()-1,y)) return true;
        if (Contains(x,y + BoardObject.GetHeight() - 1)) return true;
        if (Contains(x+BoardObject.GetWidth()-1,y + BoardObject.GetHeight() - 1)) return true;
        return false;
    }

    public boolean Contains(int x, int y)
    {
        if (x < positionX) return false;
        if (x >= positionX + width) return false;
        if (y < positionY) return false;
        if (y >= positionY + height) return false;
        return true;
    }

    public int GetObjectWidth()
    {
        return width;
    }

    public int GetObjectHeight()
    {
        return height;
    }

    public static int GetWidth()
    {
        return width;
    }

    public static int GetHeight()
    {
        return height;
    }

    public static boolean IsInBoard(int x, int y, int boardWidth, int boardHeight)
    {
        if (x - width < 0) return false;
        if (y - height < 0) return false;
        if (x + width >= boardWidth) return false;
        if (y + height >= boardHeight) return false;
        return true;
    }

    public Rectangle GetDrawnRectangle() {
        int startX = positionX;
        int startY = positionY;
        drawnRectangle = new javafx.scene.shape.Rectangle(startX,startY,width,height);
        Image img = LoadImage(texturePath);
        ImagePattern imagePattern = new ImagePattern(img);
        drawnRectangle.setFill(imagePattern);
        return drawnRectangle;
    }

    public Rectangle GetActualRectangle()
    {
        return drawnRectangle;
    }

    private String GetCurrentPath()
    {
        String current = null;
        try {
            current = new java.io.File(".").getCanonicalPath();
        }
        catch(Exception e) {
        }
        return current;
    }

    private BufferedImage LoadBufferedImage(String texturePath)
    {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(texturePath));
        } catch (IOException e) {
        }
        width = img.getWidth();
        height = img.getHeight();
        return img;
    }

    public Image LoadImage(String texturePath)
    {
        BufferedImage image = LoadBufferedImage(texturePath);
        Image img = SwingFXUtils.toFXImage(image, null);
        return img;
    }
}

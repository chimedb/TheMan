package Pacman;

public class Bow extends BoardObject {
    public int time;
    public boolean taken;
    public boolean visible;

    public Bow(int x, int y)
    {
        super(x, y);
        time = 0;
        taken = false;
        visible = false;
        texturePath = "resources/bow.png";
    }
}

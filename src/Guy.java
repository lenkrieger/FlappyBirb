import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Guy {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean dead;

    public double yvel;
    public double gravity;

    private int jumpDelay;
    private double rotation;

    private Image image;
    private Keyboard keyboard;

    public Guy() {
        x = 100;
        y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.4; //0.5 - best
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;

        keyboard = Keyboard.getInstance();
    }

    public void update() {
        yvel += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        if (!dead && keyboard.isDown(KeyEvent.VK_SPACE) && jumpDelay <= 0) {
            yvel = -10;
            jumpDelay = 10;
        }

        y += (int)yvel;
    }

    public Render getRender() {
        Render r = new Render();
        r.x = x;
        r.y = y;

        if (image == null) {
            image = Util.loadImage("lib/bird.png");     
        }
        r.image = image;

       

        return r;
    }
}

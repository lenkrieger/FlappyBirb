import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int sign_DELAY = 100;

    private Boolean paused;

    private int pauseDelay;
    private int restartDelay;
    private int signDelay;

    private Guy guy;
    private ArrayList<Sign> signs;
    private Keyboard keyboard;

    public int score;
    public Boolean gameover;
    public Boolean started;

    public Game() {
        keyboard = Keyboard.getInstance();
        restart();
    }

    public void restart() {
        paused = false;
        started = false;
        gameover = false;

        score = 0;
        pauseDelay = 0;
        restartDelay = 0;
        signDelay = 0;

        guy = new Guy();
        signs = new ArrayList<Sign>();
    }

    public void update() {
        watchForStart();

        if (!started)
            return;

        watchForPause();
        watchForReset();

        if (paused)
            return;

        guy.update();

        if (gameover)
            return;

        movesigns();
        checkForCollisions();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        renders.add(new Render(0, 0, "lib/foreground.png"));
        for (Sign sign : signs)
            renders.add(sign.getRender());
        
        renders.add(guy.getRender());
        return renders;
    }

    private void watchForStart() {
        if (!started && keyboard.isDown(KeyEvent.VK_SPACE)) {
            started = true;
        }
    }

    private void watchForPause() {
        if (pauseDelay > 0)
            pauseDelay--;

        if (keyboard.isDown(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0)
            restartDelay--;

        if (keyboard.isDown(KeyEvent.VK_R) && restartDelay <= 0) {
            restart();
            restartDelay = 10;
            return;
        }
    }

    private void movesigns() {
        signDelay--;

        if (signDelay < 0) {
            signDelay = sign_DELAY;
            Sign northsign = null;
            Sign southsign = null;

            // Look for signs off the screen
            for (Sign sign : signs) {
                if (sign.x - sign.width < 0) {
                    if (northsign == null) {
                        northsign = sign;
                    } else if (southsign == null) {
                        southsign = sign;
                        break;
                    }
                }
            }

            if (northsign == null) {
                Sign sign = new Sign("north");
                signs.add(sign);
                
                northsign = sign;
            } else {
                northsign.reset();
            }

            if (southsign == null) {
                Sign sign = new Sign("south");
                signs.add(sign);
                
                southsign = sign;
            } else {
                southsign.reset();
            }

            northsign.y = southsign.y + southsign.height + 250;
            northsign.x = southsign.x + 50;
        }

        for (Sign sign : signs) {
            sign.update();
        }
    }

    private void checkForCollisions() {

        for (Sign sign : signs) {
            if (sign.collides(guy.x, guy.y - 50, guy.width, guy.height)) {
                gameover = true;
                guy.dead = true;
            } else if (sign.x == guy.x && sign.orientation.equalsIgnoreCase("south")) {
                score++;
            }
        }

        // Ground + guy collision
        if (guy.y + guy.height > App.HEIGHT - 80) {
            
            guy.y = App.HEIGHT - 80 - guy.height;
        }
    }
}

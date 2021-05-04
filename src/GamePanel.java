import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    final BufferedImage apple = ImageIO.read(new File("D:\\Images\\apple.png"));
    final BufferedImage head = ImageIO.read(new File("D:\\Images\\snakeHead.png"));
    final BufferedImage body = ImageIO.read(new File("D:\\Images\\snakeBody.png"));

    static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 900;

    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 125;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean justDied = false;

    int timesLooped = 0;

    Timer timer;
    Random random;

    GamePanel() throws IOException {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.RED);

//            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            g.drawImage(apple, appleX, appleY, UNIT_SIZE, UNIT_SIZE, null);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                } else {
                    if (justDied) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    g.drawImage(body, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                }

            }
            justDied = false;
            drawScore(g);
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;


    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
//        Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
//        Checks if head touches left border
        if (x[0] < 0) running = false;
        if (x[0] > SCREEN_WIDTH) running = false;
        if (y[0] < 0) running = false;
        if (y[0] > SCREEN_HEIGHT) running = false;

        if (!running) timer.stop();
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.PLAIN, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, 10, 55);
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Minecrafter", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAme Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.PLAIN, 45));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2+90);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();

            timesLooped++;

//            if (timesLooped == 25) {
//                timer.stop();
//
//                timer = new Timer(DELAY-15, this);
//                timer.start();
//            }
//
//            if (timesLooped == 50) {
//                timer.stop();
//
//                timer = new Timer(DELAY-30, this);
//                timer.start();
//            }

//            if (timesLooped == 75) {
//                timer.stop();
//
//                timer = new Timer(DELAY-45, this);
//                timer.start();
//            }
//            if (timesLooped == 100) {
//                timer.stop();
//
//                timer = new Timer(DELAY-55, this);
//                timer.start();
//            }

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        justDied = true;
                        x[0] = 0;
                        y[0] = 0;
                        bodyParts = 6;
                        System.out.println(x[0]);
                        running = true;
                        startGame();
                        repaint();
                        direction = 'R';
                    }

            }
        }
    }
}

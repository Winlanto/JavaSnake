import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class GameField extends JPanel implements ActionListener {
    private final int fieldSize = 400;
    private final int tileSize = 20;
    private final int allTiles = fieldSize / tileSize;
    private ImageIcon IISnakeHead;
    private ImageIcon IISnakeBody;
    private ImageIcon IISnakeTail;
    private Image food;
    private Image tile;
    private int foodX;
    private int foodY;
    private final int[] x = new int[allTiles];
    private final int[] y = new int[allTiles];
    private int snakeSize;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private final int[] btnsArrows = {VK_UP, VK_LEFT, VK_DOWN, VK_RIGHT};
    private final int[] btnsWASD = {VK_W, VK_A, VK_S, VK_D,};

    public GameField() {
        setBackground(Color.darkGray);
        loadImages();
        start();
        addKeyListener(new fieldKeyListener());
        setFocusable(true);
    }

    public void start() {
        snakeSize = 3;
        for (int i = 0; i < snakeSize; i++) {
            x[i] = 200 - i * tileSize;
            y[i] = 200;
        }
        Timer timer = new Timer(250, this);
        timer.start();
        createFood();
    }

    private void createFood() {
        foodX = new Random().nextInt(20) * tileSize;
        foodY = new Random().nextInt(20) * tileSize;
    }

    public void loadImages() {
        food = new ImageIcon("food.png").getImage();
        tile = new ImageIcon("tile.png").getImage();
        IISnakeHead = new ImageIcon("snakeHead.png");
        IISnakeBody = new ImageIcon("snakeBody.png");
        IISnakeTail = new ImageIcon("snakeTail.png");
    }

    static private Image rotate(ImageIcon imgIcon, double angle) {
        int w = imgIcon.getIconWidth();
        int h = imgIcon.getIconHeight();
        int type = BufferedImage.TYPE_INT_ARGB;
        BufferedImage image = new BufferedImage(h, w, type);
        Graphics2D g2 = image.createGraphics();
        double x = (h - w) / 2.0;
        double y = (w - h) / 2.0;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        g2.drawImage(imgIcon.getImage(), at, null);
        g2.dispose();
        imgIcon = new ImageIcon(image);
        return imgIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(food, foodX, foodY, this);
            /*for (int i = 0; i<allTiles; i++){
                if(){
                    g.drawImage(tile, x[i], y[i], this);
                }
            }*/
            if (up){
                g.drawImage(rotate(IISnakeHead, -90.0), x[0], y[0], this);
            }else if (down){
                g.drawImage(rotate(IISnakeHead, 90.0), x[0], y[0], this);
            }else if (left){
                g.drawImage(rotate(IISnakeHead, 180.0), x[0], y[0], this);
            }else{
                g.drawImage(rotate(IISnakeHead, 0.0), x[0], y[0], this);
            }
            for (int i = 1; i < snakeSize - 1; i++) {
                //g.drawImage(snakeBody, x[i], y[i], this);
                if (x[i] == x[i-1] && y[i] >= y[i-1]){
                    g.drawImage(rotate(IISnakeBody, -90.0), x[i], y[i], this);
                }else if (x[i] == x[i-1] && y[i] <= y[i-1]){
                    g.drawImage(rotate(IISnakeBody, 90.0), x[i], y[i], this);
                }else if (x[i] >= x[i-1] && y[i] == y[i-1]){
                    g.drawImage(rotate(IISnakeBody, 180.0), x[i], y[i], this);
                }else{
                    g.drawImage(rotate(IISnakeBody, 0.0), x[i], y[i], this);
                }
            }
            if (x[snakeSize-1] == x[snakeSize-2] && y[snakeSize-1] >= y[snakeSize-2]){
                g.drawImage(rotate(IISnakeTail, -90.0), x[snakeSize - 1], y[snakeSize - 1], this);
            }else if (x[snakeSize-1] == x[snakeSize-2] && y[snakeSize-1] <= y[snakeSize-2]){
                g.drawImage(rotate(IISnakeTail, 90.0), x[snakeSize - 1], y[snakeSize - 1], this);
            }else if (x[snakeSize-1] >= x[snakeSize-2] && y[snakeSize-1] == y[snakeSize-2]){
                g.drawImage(rotate(IISnakeTail, 180.0), x[snakeSize - 1], y[snakeSize - 1], this);
            }else{
                g.drawImage(rotate(IISnakeTail, 0.0), x[snakeSize - 1], y[snakeSize - 1], this);
            }
        }
    }

    public void move() {
        for (int i = snakeSize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) x[0] -= tileSize;
        if (right) x[0] += tileSize;
        if (up) y[0] -= tileSize;
        if (down) y[0] += tileSize;
        if (x[0] == foodX && y[0] == foodY) {
            createFood();
            snakeSize++;
        }
        repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollisions();
        }
    }

    private void checkCollisions() {
        for (int i = snakeSize; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] >= fieldSize) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] >= fieldSize) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }


    class fieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if ((key == btnsArrows[0] || key == btnsWASD[0]) && !down) {
                left = false;
                right = false;
                up = true;
                down = false;
            }
            if ((key == btnsArrows[1] || key == btnsWASD[1]) && !right) {
                left = true;
                right = false;
                up = false;
                down = false;
            }
            if ((key == btnsArrows[2] || key == btnsWASD[2]) && !up) {
                left = false;
                right = false;
                up = false;
                down = true;
            }
            if ((key == btnsArrows[3] || key == btnsWASD[3]) && !left) {
                left = false;
                right = true;
                up = false;
                down = false;
            }
        }
    }
}

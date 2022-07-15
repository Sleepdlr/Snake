/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Viktória
 */
public class Snake {

    private Image SnakeHead;
    private ArrayList<ImageIcon> snakeWhole;
    private Image right;
    private Image left;
    private Image up;
    private Image down;
    private Image body;
    private Image food;
    private Image enemy;
    private int db;

    private boolean foodInBoard = false;
    private Point foodposition = new Point(-1, -1);
    private ArrayList<Point> snakeBody;
    private ArrayList<Point> enemyposition;

    private int x;
    private int y;
    private int velx;
    private int vely;

    private boolean isdead = false;

    private Direction direction;
    public int BODY = 25;
    private final int SPEED = 25;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    /**
     * Snake konstruktor.
     */
    public Snake() {
        
        this.direction = Direction.getRandomSide();
        this.snakeBody = new ArrayList<>();
        this.snakeWhole = new ArrayList<>();
        this.enemyposition = new ArrayList<>();

        this.right = new ImageIcon("data/snake/right.png").getImage();
        this.left = new ImageIcon("data/snake/left.png").getImage();
        this.up = new ImageIcon("data/snake/up.png").getImage();
        this.down = new ImageIcon("data/snake/down.png").getImage();
        this.body = new ImageIcon("data/snake/body.png").getImage();
        this.food = new ImageIcon("data/snake/food.png").getImage();
        this.enemy = new ImageIcon("data/snake/enemy.png").getImage();

        this.x = WIDTH / 2;
        this.y = HEIGHT / 2;

        this.snakeBody.add(getBodyPoint(direction, x, y));
        for (int i = 1; i < 1; i++) { 
            Point p = this.snakeBody.get(i - 1);
            this.snakeBody.add(getBodyPoint(direction, p.x, p.y));
        }

        Random rand = new Random();
        db = rand.nextInt(30 + 1) + 10;
        for (int i = 0; i < db; i++) {
            enemyposition.add(generateEnemy(x, y));
        }

        this.setDir(direction);

    }
    
    /**
     * Visszatér azzal, hogy él-e a kígyó.
     *
     * @return logikai érték
     */
    public boolean moveIsValid() {
        return !isdead;
    }

    /**
     * Visszatér a kígyó hosszával -1-el.
     *
     * @return kígyó hossza -1
     */
    public int getLenght() {
        return snakeBody.size() - 1;
    }

    /**
     * Megnézi, hogy sikerült-e megszerezni az élelmet.
     *
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return igaz, ha a fej az ételen van
     */
    private boolean EatFood(int x, int y) {
        return (foodposition.x != -1 && foodposition.y != -1 && (foodposition.x == x && foodposition.y == y));
    }

    /**
     * Megnézi, hogy a kígyó ellenségbe ütközött-e.
     *
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return igaz, ha a fej akadályba ütközött
     */
    private boolean deadByEnemy(int x, int y) {
        return enemyposition.contains(new Point(x, y));
    }

    /**
     * Megnézi, hogy éppen a kígyó saját testébe ütközött-e.
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return igaz, ha a fej saját testébe ütközött.
     */
    private boolean deadBySelf(int x, int y) {
        return snakeBody.contains(new Point(x, y));
    }    
    
    /**
     * Kirajzolja a kígyó fejét, testét, ételt és az akadályokat. Illetve
     * ellőrzi, hogy evett-e a kígyó, és van-e étel a pályán.
     */
    void draw(Graphics g) {
        switch (direction) {
            case UP:
                SnakeHead = up;
                break;
            case DOWN:
                SnakeHead = down;
                break;
            case LEFT:
                SnakeHead = left;
                break;
            default:
                SnakeHead = right;
        }

        if (EatFood(x, y)) {
            Point last = this.snakeBody.get(snakeBody.size() - 1);
            this.snakeBody.add(getBodyPoint(direction, last.x, last.y));
            this.foodInBoard = false;
        }

        g.drawImage(SnakeHead, x, y, BODY, BODY, null);

        for (Point p : snakeBody) {
            g.drawImage(body, p.x, p.y, BODY, BODY, null);
        }

        if (!this.foodInBoard) {
            this.foodposition = generateFood(x, y);
            this.foodInBoard = true;
        } else {
            g.drawImage(food, foodposition.x, foodposition.y, BODY, BODY, null);
        }

        for (int i = 0; i < enemyposition.size(); i++) {
            Point en = enemyposition.get(i);
            g.drawImage(enemy, en.x, en.y, BODY, BODY, null);
        }
    }
    
    /**
     * Ételt generál oda.
     *
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return étel poziciója
     */
    private Point generateFood(int x, int y) {
        Random rand = new Random();
        int gx = rand.nextInt(32);
        int gy = rand.nextInt(24);

        if ((gx != x && gy != y) && !snakeBody.contains(new Point(gx * BODY, gy * BODY)) && !enemyposition.contains(new Point(gx * BODY, gy * BODY))) {
            return new Point(gx * BODY, gy * BODY);
        } else {
            return generateFood(x, y);
        }
    }

    /**
     * Ellenséget generál.
     *
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return lehetséges akadály poziciója
     */
    private Point generateEnemy(int x, int y) {
        Random rand = new Random();
        int gx = rand.nextInt(32);
        int gy = rand.nextInt(24);

        if ((gx != foodposition.x && gy != foodposition.y) && (gx != x && gy != y) && !snakeBody.contains(new Point(gx * BODY, gy * BODY)) && !enemyposition.contains(new Point(gx * BODY, gy * BODY))) {
            return new Point(gx * BODY, gy * BODY);
        } else {
            return generateEnemy(x, y);
        }
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        /**
         * Visszatért az enum-ból egy iránnyal.
         *
         * @return random irány
         */
        public static Direction getRandomSide() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    /**
     * Visszatér azzal a pontal ami a kígyó feje mögött van.
     *
     * @param dir Kígyó iránya
     * @param x Kígyó fejének X koordinátája
     * @param y Kígyó fejének Y koordinátája
     * @return kígyó feje mögötti pozició
     */
    private Point getBodyPoint(Direction dir, int x, int y) {
        int newX = x;
        int newY = y;

        switch (dir) {
            case UP:
                newY = newY + BODY;
                break;
            case DOWN:
                newY = newY - BODY;
                break;
            case LEFT:
                newX = newX + BODY;
                break;
            case RIGHT:
                newX = newX - BODY;
                break;
        }

        return new Point(newX, newY);
    }

    /**
     * Új irányt állít be a kígyónak.
     *
     * @param i Kígyó új iránya
     */
    public void setDir(Direction i) {
        if (!isdead) {
            this.direction = i;
            switch (i) {
                case UP:
                    velx = 0 * SPEED;
                    vely = -1 * SPEED;
                    break;
                case DOWN:
                    velx = 0 * SPEED;
                    vely = 1 * SPEED;
                    break;
                case LEFT:
                    velx = -1 * SPEED;
                    vely = 0 * SPEED;
                    break;
                default:
                    velx = 1 * SPEED;
                    vely = 0 * SPEED;
            }
        }
    }

    /**
     * Ellenörzi, hogy vége van-e a játéknak ha nem, mozgatja a kígyó fejét, és a testét.
     */
    public void move() {
        if (deadByEnemy(x + velx, y + vely)) {
            isdead = true;
            velx = 0;
            vely = 0;
        }

        if (deadBySelf(x + velx, y + vely)) {
            isdead = true;
            velx = 0;
            vely = 0;
        }

        if (!isdead) {
            Point prewHead = new Point(x, y);
            x += velx;
            y += vely;
            if (x >= WIDTH || x <= 0 - BODY) {
                velx = 0;
                isdead = true;
            }

            if (y >= HEIGHT || y <= 0 - BODY) {
                vely = 0;
                isdead = true;
            }

            Point prewBody = new Point(snakeBody.get(0));
            snakeBody.get(0).move(prewHead.x, prewHead.y);
            int i = 1;
            while (i < snakeBody.size()) {
                Point tmp = new Point(snakeBody.get(i));
                snakeBody.get(i).move(prewBody.x, prewBody.y);
                prewBody = new Point(tmp);
                i++;
            }
        }
    }


}

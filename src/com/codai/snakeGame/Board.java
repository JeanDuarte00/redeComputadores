package com.codai.snakeGame;

import com.codai.utils.AudioPlayer;
import com.codai.utils.TAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    String baseUrl = "C:\\Users\\jean_\\IdeaProjects";

    AudioPlayer audioPlayer = new AudioPlayer();

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 80;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int score;
    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean enterKey = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    private TAdapter keys = new TAdapter(rightDirection, leftDirection, upDirection, downDirection, enterKey);


    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(keys);
        setBackground( new Color(0, 0, 0) );
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initEffects();
        initGame();
    }

    private void initEffects () {
        this.audioPlayer.addSoundEffect("gain", baseUrl + "\\snakeRedes\\src\\resources\\music\\gainScore.wav");
        this.audioPlayer.addSoundEffect("lose", baseUrl + "\\snakeRedes\\src\\resources\\music\\loseScore.wav");
        this.audioPlayer.addSoundEffect("track", baseUrl + "\\snakeRedes\\src\\resources\\music\\track.wav");
        this.audioPlayer.setTrackSound(this.audioPlayer.getMusic("track"));
    }

    private void loadImages() {

        ImageIcon icon;

        try {
            icon = new ImageIcon(baseUrl + "\\snakeRedes\\src\\resources\\img\\dot.png");
            ball = icon.getImage();

            icon = new ImageIcon(baseUrl + "\\snakeRedes\\src\\resources\\img\\java1.png");
            apple = icon.getImage();

            icon = new ImageIcon(baseUrl + "\\snakeRedes\\src\\resources\\img\\head.png");
            head = icon.getImage();

        } catch (Exception err) {

            System.out.println("Erro: " + err.getMessage());
        }
    }

    private void initGame() {

        audioPlayer.playTrack();

        dots = 1;
        score = 0;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        putAppleOn();

        timer = new Timer(80, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            this.score(g);

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);

        }
    }

    private void score(Graphics g) {
        System.out.println("score: " + score);
        String msg = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(new Color(255,255,255,100));
        g.setFont(small);
        g.drawString(msg, (B_WIDTH- metr.stringWidth(msg))/2, B_HEIGHT/20);
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        String msgRest = "ENTER para voltar ao jogo";

        Font small = new Font("Helvetica", Font.BOLD, 34);
        Font smaller = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        FontMetrics metrr = getFontMetrics(smaller);

        g.setColor(Color.red);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);


        g.setColor(Color.red);
        g.setFont(smaller);
        g.drawString(msgRest, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 4);


    }

    private void checkApple() {

        if ((x[0] > apple_x && x[0] < apple_x+20) && (y[0] == apple_y)) {

            this.audioPlayer.play("gain");

            this.swapColors();

            score++;
            dots++;
            putAppleOn();
        }
    }

    private void swapColors () {
        Image redHead = head;
        Image body = ball;


        head = body;
        ball = redHead;

    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (keys.isLeftDirection()) {
            x[0] -= DOT_SIZE;
        }

        if (keys.isRightDirection()) {
            x[0] += DOT_SIZE;
        }

        if (keys.isUpDirection()) {
            y[0] -= DOT_SIZE;
        }

        if (keys.isDownDirection()) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            this.audioPlayer.play("lose");
            // timer.stop();

        }
    }

    private void putAppleOn() {

        int x = (int) (Math.random() * RAND_POS);
        apple_x = ((x * DOT_SIZE));

        int y = (int) (Math.random() * RAND_POS);
        apple_y = ((y * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();

        } else {
            if ( keys.isEnterKey() ) {
                restartGame();
            }
        }

        repaint();
    }

    public void restartGame () {

        keys.setDownDirection(false);
        keys.setUpDirection(false);
        keys.setLeftDirection(false);
        keys.setRightDirection(false);
        keys.setEnterKey(false);
        inGame = true;
        dots = 1;
        score = 0;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        putAppleOn();
    }

}
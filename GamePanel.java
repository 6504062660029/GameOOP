import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class GamePanel extends JPanel {
    private UI ui;
    private Archer archer;
    private Stage currentStage;
    private int stageNumber = 1;
    private Timer gameTimer;
    private boolean gameWon = false;
    private boolean gameOver = false;
    private boolean isStageSelectionScreen = true;
    private JButton nextStageButton, exitButton;
    private long stageStartTime;

    // Background images
    private Image selectionBackground;
    private Image winBackground;
    private Image loseBackground;

    public GamePanel(UI ui, Archer archer) {
        this.ui = ui;
        this.archer = archer;

        // Load background images
        selectionBackground = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\bgwelcome.jpg").getImage();
        winBackground = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\bgwin.jpg").getImage();
        loseBackground = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\bglose.jpg").getImage();

        setFocusable(true);
        requestFocusInWindow();

        setupKeyListener();
        setupMouseListener();

        if (isStageSelectionScreen) {
            showStageSelectionScreen();
        } else {
            startStage(stageNumber);
        }
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isStageSelectionScreen && !gameWon && !gameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> archer.move('W');
                        case KeyEvent.VK_A -> archer.move('A');
                        case KeyEvent.VK_S -> archer.move('S');
                        case KeyEvent.VK_D -> archer.move('D');
                    }
                    repaint();
                }
            }
        });
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isStageSelectionScreen && !gameWon && !gameOver) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    archer.shoot(mouseX, mouseY);
                }
            }
        });
    }

    private void showStageSelectionScreen() {
        setLayout(null);
        JButton stage1Button = new JButton("Stage 1");
        JButton stage2Button = new JButton("Stage 2");
        JButton stage3Button = new JButton("Stage 3");

        stage1Button.setBounds(300, 300, 200, 50);
        stage2Button.setBounds(300, 370, 200, 50);
        stage3Button.setBounds(300, 440, 200, 50);

        stage1Button.addActionListener(e -> startGameWithStage(1));
        stage2Button.addActionListener(e -> startGameWithStage(2));
        stage3Button.addActionListener(e -> startGameWithStage(3));

        add(stage1Button);
        add(stage2Button);
        add(stage3Button);

        revalidate();
        repaint();
    }

    private void startGameWithStage(int stageNumber) {
        this.stageNumber = stageNumber;
        isStageSelectionScreen = false;
        removeAll();
        startStage(stageNumber);
    }

    private void startStage(int stageNumber) {
        archer.setHp(100);
        currentStage = new Stage(stageNumber, archer);
        stageStartTime = System.currentTimeMillis();

        if (gameTimer != null) {
            gameTimer.cancel();
        }
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameLoop();
            }
        }, 0, 16);

        gameWon = false;
        gameOver = false;

        removeWinLoseButtons();
        requestFocusInWindow();
    }

    private void gameLoop() {
        if (gameWon || gameOver) {
            return;
        }

        currentStage.update();

        if (stageNumber == 2) {
            long elapsedSeconds = (System.currentTimeMillis() - stageStartTime) / 1000;
            if (elapsedSeconds >= 50) {
                gameWon = true;
                gameTimer.cancel();
                showWinScreen();
            } else if (currentStage.isGameOver()) {
                gameOver = true;
                gameTimer.cancel();
                showLoseScreen();
            }
        } else {
            if (currentStage.isStageCleared()) {
                gameWon = true;
                gameTimer.cancel();
                showWinScreen();
            } else if (currentStage.isGameOver()) {
                gameOver = true;
                gameTimer.cancel();
                showLoseScreen();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isStageSelectionScreen) {
            g.drawImage(selectionBackground, 0, 0, getWidth(), getHeight(), this);
        } else if (gameWon) {
            g.drawImage(winBackground, 0, 0, getWidth(), getHeight(), this);
        } else if (gameOver) {
            g.drawImage(loseBackground, 0, 0, getWidth(), getHeight(), this);
        } else {
            currentStage.render(g);
            ui.render(g);
        }
    }

    private void showWinScreen() {
        removeWinLoseButtons();
        setLayout(null);

        nextStageButton = new JButton("Next Stage");
        exitButton = new JButton("Exit");

        nextStageButton.setBounds(280, 350, 100, 50);
        exitButton.setBounds(420, 350, 100, 50);

        nextStageButton.addActionListener(e -> {
            gameWon = false;
            stageNumber++;
            if (stageNumber <= 3) {
                removeWinLoseButtons();
                startStage(stageNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Game Complete! Thanks for playing.");
                System.exit(0);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(nextStageButton);
        add(exitButton);

        revalidate();
        repaint();
    }

    private void showLoseScreen() {
        removeWinLoseButtons();
        setLayout(null);

        exitButton = new JButton("Exit");
        exitButton.setBounds(350, 350, 100, 50);
        exitButton.addActionListener(e -> System.exit(0));

        add(exitButton);

        revalidate();
        repaint();
    }

    private void removeWinLoseButtons() {
        if (nextStageButton != null) {
            remove(nextStageButton);
            nextStageButton = null;
        }
        if (exitButton != null) {
            remove(exitButton);
            exitButton = null;
        }
        revalidate();
        repaint();
    }
}

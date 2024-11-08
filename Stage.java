import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

public class Stage {
    private int stageNumber;
    private Archer archer;
    private List<Monster> monsters;
    private boolean stageCleared;
    private boolean gameOver = false;
    private boolean bossDefeated = false;
    private Image backgroundImage;
    private int killCount = 0;
    private static final int KILL_TARGET = 10;  // Win condition for Stage 1
    private Timer spawnTimer;
    private Timer survivalTimer;
    private long startTime;

    public Stage(int stageNumber, Archer archer) {
        this.stageNumber = stageNumber;
        this.archer = archer;
        this.monsters = new ArrayList<>();
        this.stageCleared = false;

        // Load background image based on the stage number
        String imagePath = switch (stageNumber) {
            case 1 -> "C:\\Users\\pooh\\Desktop\\gamejavafinal\\bg.jpg";
            case 2 -> "C:\\Users\\pooh\\Desktop\\gamejavafinal\\bg2.jpg";
            case 3 -> "C:\\Users\\pooh\\Desktop\\gamejavafinal\\bg3.jpg";
            default -> "";
        };
        backgroundImage = new ImageIcon(imagePath).getImage();

        // Initialize different spawning behaviors for each stage
        if (stageNumber == 2) {
            startSurvivalTimer();
            startMonsterSpawning(); // Spawn monsters in Stage 2
        } else if (stageNumber == 3) {
            spawnBossMonster(); // Spawn the boss in Stage 3
            startRegularMonsterSpawning(); // Spawn other monsters every 2 seconds
        } else {
            startMonsterSpawning();  // Regular monster spawning in Stage 1
        }
    }



    public int getKillCount() {
        return killCount;
    }

    public int getKillTarget() {
        return KILL_TARGET;
    }

    private void startMonsterSpawning() {
        spawnTimer = new Timer();
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Stage 1 win condition based on kill count
                if (stageNumber == 1 && killCount < KILL_TARGET && !gameOver) {
                    spawnMonster();
                } else if (stageNumber == 2 && !gameOver) {
                    spawnMonster(); // Continuous spawning for Stage 2
                } else if (killCount >= KILL_TARGET && stageNumber == 1) {
                    stageCleared = true; // Trigger win condition for Stage 1
                    spawnTimer.cancel();
                }
            }
        }, 0, 4000);
    }

    private void startSurvivalTimer() {
        startTime = System.currentTimeMillis();
        survivalTimer = new Timer();
        survivalTimer.schedule(new TimerTask() {
            private int survivalTime = 0;

            @Override
            public void run() {
                if (gameOver) {
                    survivalTimer.cancel();
                } else {
                    survivalTime++;
                    if (survivalTime >= 50) {
                        stageCleared = true;
                        survivalTimer.cancel();
                    }
                }
            }
        }, 1000, 1000); // Increment every second
    }

    private void spawnBossMonster() {
        int spawnX = 400; // Center horizontally
        int spawnY = 50;  // Near the top of the map

        BossMonster boss = new BossMonster(spawnX, spawnY, 200, 2, 50); // Adjust other parameters as needed
        monsters.add(boss);
    }

    private void startRegularMonsterSpawning() {
        spawnTimer = new Timer();
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!gameOver && !stageCleared) {
                    spawnMonster();
                } else {
                    spawnTimer.cancel();
                }
            }
        }, 0, 2000); // Spawn every 2 seconds
    }

    private void spawnMonster() {
        Random rand = new Random();
        Monster monster = rand.nextBoolean()
            ? new MeleeMonster(rand.nextInt(750) + 25, rand.nextInt(550) + 25, 20, 2, 10)
            : new RangedMonster(rand.nextInt(750) + 25, rand.nextInt(550) + 25, 15, 1, 8, archer);
        monsters.add(monster);
    }

    public void update() {
        if (gameOver || stageCleared) return;

        archer.update(archer);
        List<Monster> monstersToRemove = new ArrayList<>();
        List<Projectile> arrowsToRemove = new ArrayList<>();

        for (Monster monster : monsters) {
            monster.update(archer);

            for (Projectile arrow : archer.getArrows()) {
                if (arrow.collidesWith(monster)) {
                    monster.takeDamage(10);
                    arrowsToRemove.add(arrow);
                    if (monster.isDead()) {
                        monstersToRemove.add(monster);
                        if (stageNumber == 3 && monster instanceof BossMonster) {
                            bossDefeated = true;
                            stageCleared = true;
                            spawnTimer.cancel();
                        } else if (stageNumber == 1) {
                            killCount++;
                            if (killCount >= KILL_TARGET) {
                                stageCleared = true;  // Set the stage as cleared once the target is met
                                spawnTimer.cancel();
                            }
                        }
                    }
                }
            }

            if (monster instanceof RangedMonster) {
                RangedMonster rangedMonster = (RangedMonster) monster;
                List<Projectile> fireballsToRemove = new ArrayList<>();

                for (Projectile fireball : rangedMonster.getProjectiles()) {
                    if (fireball.collidesWith(archer)) {
                        archer.takeDamage(10);
                        fireballsToRemove.add(fireball);
                        if (archer.isDead()) {
                            gameOver = true;
                        }
                    }
                }
                rangedMonster.getProjectiles().removeAll(fireballsToRemove);
            }

            if (monster.collidesWith(archer)) {
                archer.takeDamage(10);
                if (archer.isDead()) {
                    gameOver = true;
                }
            }
        }

        monsters.removeAll(monstersToRemove);
        archer.getArrows().removeAll(arrowsToRemove);
    }

    public boolean isStageCleared() {
        return stageCleared;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, 800, 600, null);
        archer.render(g);
        for (Monster monster : monsters) {
            monster.render(g);
        }

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        if (stageNumber == 1) { // Show kill count only in Stage 1
            g.drawString("Kills: " + killCount + " / " + KILL_TARGET, 650, 20);
        } else if (stageNumber == 2) { // Display survival timer in Stage 2
            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            g.drawString("Survive Time: " + elapsedSeconds + " / 50 seconds", 10, 60);
        }
    }
}

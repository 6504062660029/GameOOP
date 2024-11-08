import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class BossMonster extends Monster {
    private Image bossImage;

    public BossMonster(int x, int y, int hp, int speed, int damage) {
        super(x, y, hp, speed, damage);
        bossImage = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\boss.png").getImage();
    }

    public void move(Archer archer) {
        // Make the boss monster move towards the archer
        int deltaX = archer.getX() - x;
        int deltaY = archer.getY() - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance != 0) {
            x += (int)(deltaX / distance * speed);  // Move towards archer with specified speed
            y += (int)(deltaY / distance * speed);
        }
    }

    public void update(Archer archer) {
        move(archer);
        attack(archer);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(bossImage, x, y, 80, 80, null); // Boss is larger
        drawHealthBar(g, 80, 10); // Draw health bar for boss monster
    }

    @Override
    public void attack(Archer archer) {
        if (collidesWith(archer)) {
            archer.takeDamage(damage);
        }
    }
}

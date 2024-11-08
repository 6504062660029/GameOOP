import java.awt.*;
import javax.swing.ImageIcon;

public class MeleeMonster extends Monster {
    private Image meleeMonsterImage;

    public MeleeMonster(int x, int y, int hp, int speed, int damage) {
        super(x, y, hp, speed, damage);
        meleeMonsterImage = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\malee.png").getImage();
    }

    @Override
    public void update(Archer archer) {
        // Follow the Archer
        int dx = archer.getX() - this.x;
        int dy = archer.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            this.x += (int) (dx / distance * speed);
            this.y += (int) (dy / distance * speed);
        }
    }

    @Override
    public void render(Graphics g) {
        // Draw the monster image
        g.drawImage(meleeMonsterImage, x, y, 50, 50, null);

        // Draw the health bar
        super.drawHealthBar(g, 50, 5); // Width: 50, Offset: 5
    }

    @Override
    public void attack(Archer archer) {
        archer.takeDamage(damage);
    }
}

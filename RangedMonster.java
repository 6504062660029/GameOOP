import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;

public class RangedMonster extends Monster {
    private Image rangedMonsterImage;
    private List<Projectile> fireballs;
    private int attackCooldown = 100;
    private int cooldownTimer = 0;
    private Archer targetArcher;

    public RangedMonster(int x, int y, int hp, int speed, int damage, Archer archer) {
        super(x, y, hp, speed, damage);
        this.targetArcher = archer;
        this.rangedMonsterImage = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\mage.png").getImage();
        this.fireballs = new ArrayList<>();
    }

    @Override
    public void update(Archer archer) {
        cooldownTimer++;
        if (cooldownTimer >= attackCooldown) {
            attack(archer);
            cooldownTimer = 0;
        }

        // Update fireball positions and remove those out of bounds
        Iterator<Projectile> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Projectile fireball = iterator.next();
            fireball.move();
            if (fireball.isOutOfBounds(800, 600)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void attack(Archer archer) {
        if (archer != null) {
            shootFireball(archer.getX(), archer.getY());
        }
    }

    private void shootFireball(int targetX, int targetY) {
        fireballs.add(new Projectile(x + 25, y + 25, targetX, targetY, "fireball")); // Offset for center
    }

    // Return the list of projectiles
    public List<Projectile> getProjectiles() {
        return fireballs;
    }

    // Remove a specific projectile from the list
    public void removeProjectile(Projectile fireball) {
        fireballs.remove(fireball);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(rangedMonsterImage, x, y, 50, 50, null);
        for (Projectile fireball : fireballs) {
            fireball.draw(g);
        }
        super.drawHealthBar(g, 50, 10); // Health bar width: 50, offset: 10
    }
}

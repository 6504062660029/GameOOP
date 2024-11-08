import java.awt.Color;
import java.awt.Graphics;

public abstract class Entity {
    protected int x, y, hp;
    private final int maxHp; // To calculate health bar ratio

    public Entity(int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = hp; // Store the initial HP as max HP
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0; // Ensure HP doesn't drop below 0
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public abstract void update(Archer archer); // Allow passing Archer for targeting
    public abstract void render(Graphics g); // Render the entity using Graphics

    // Draw health bar above the entity
    protected void drawHealthBar(Graphics g, int width, int offset) {
        int healthBarWidth = width;
        int healthBarHeight = 5;
        int healthX = x;
        int healthY = y - offset;

        // Draw background (empty health bar)
        g.setColor(Color.RED);
        g.fillRect(healthX, healthY, healthBarWidth, healthBarHeight);

        // Draw foreground (current health)
        g.setColor(Color.GREEN);
        int filledWidth = (int) (healthBarWidth * ((double) hp / maxHp)); // Scale based on max HP
        g.fillRect(healthX, healthY, filledWidth, healthBarHeight);
    }
}

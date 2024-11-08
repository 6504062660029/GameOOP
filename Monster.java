import java.awt.Graphics;

public abstract class Monster extends Entity {
    protected int speed;
    protected int damage;

    public Monster(int x, int y, int hp, int speed, int damage) {
        super(x, y, hp);
        this.speed = speed;
        this.damage = damage;
    }

    public boolean collidesWith(Entity other) {
        int distance = (int) Math.hypot(other.x - this.x, other.y - this.y);
        return distance < 30; // Adjust as needed for collision range
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    // Abstract attack method that each subclass will implement
    public abstract void attack(Archer archer);

    @Override
    public abstract void render(Graphics g); // Each subclass should implement this
}

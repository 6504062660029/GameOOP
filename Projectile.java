import java.awt.*;

public class Projectile {
    private int x, y;
    private final int directionX, directionY;
    private final int speed = 10;
    private final String type;
    private final int size = 10;

    public Projectile(int startX, int startY, int targetX, int targetY, String type) {
        this.x = startX;
        this.y = startY;
        this.type = type;

        // Calculate direction vector and normalize it
        int dx = targetX - startX;
        int dy = targetY - startY;
        double length = Math.sqrt(dx * dx + dy * dy);

        this.directionX = (int) (dx / length * speed);
        this.directionY = (int) (dy / length * speed);
    }

    public void move() {
        x += directionX;
        y += directionY;
    }

    public void draw(Graphics g) {
        if ("arrow".equals(type)) {
            g.setColor(Color.BLACK); // Archer's arrows are black
            g.fillRect(x, y, size, size / 2); // Rectangular arrow shape
        } else if ("fireball".equals(type)) {
            g.setColor(Color.RED); // Ranged monster's fireballs are red
            g.fillOval(x, y, size, size); // Circular fireball shape
        }
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x < -size || y < -size || x > screenWidth + size || y > screenHeight + size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean collidesWith(Entity entity) {
        Rectangle projectileBounds = new Rectangle(x, y, size, size);
        Rectangle entityBounds = new Rectangle(entity.getX(), entity.getY(), 50, 50); // Assuming entity size 50x50
        return projectileBounds.intersects(entityBounds);
    }
}

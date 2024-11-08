import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Archer extends Entity {
    private int speed = 5;
    private List<Projectile> arrows;
    private Image archerImage;

    public Archer(int x, int y, int hp) {
        super(x, y, hp);
        this.arrows = new ArrayList<>();
        archerImage = new ImageIcon("C:\\Users\\pooh\\Desktop\\gamejavafinal\\archer.png").getImage();
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public List<Projectile> getArrows() {
        return arrows;
    }

    public void move(char direction) {
        switch (direction) {
            case 'W' -> y = Math.max(0, y - speed);
            case 'A' -> x = Math.max(0, x - speed);
            case 'S' -> y = Math.min(600 - 50, y + speed);
            case 'D' -> x = Math.min(800 - 50, x + speed);
        }
    }

    public void shoot(int targetX, int targetY) {
        arrows.add(new Projectile(x + 25, y + 25, targetX, targetY, "arrow"));
    }

    @Override
    public void update(Archer archer) {
        arrows.removeIf(arrow -> {
            arrow.move();
            return arrow.isOutOfBounds(800, 600);
        });
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(archerImage, x, y, 50, 50, null);
        for (Projectile arrow : arrows) {
            arrow.draw(g);
        }
        super.drawHealthBar(g, 50, 10);
    }
}

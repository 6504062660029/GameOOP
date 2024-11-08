import java.awt.*;

public class UI {
    private Archer archer;

    public UI(Archer archer) {
        this.archer = archer;
    }

    public void render(Graphics g) {
        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        // Draw health bar background
        g.setColor(Color.RED);
        g.fillRect(20, 20, 200, 20);

        // Draw current health as the foreground
        g.setColor(Color.GREEN);
        g.fillRect(20, 20, archer.getHp() * 2, 20);

        // Display HP textS
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("HP: " + archer.getHp(), 20, 15);
    }
}

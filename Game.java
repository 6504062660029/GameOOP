import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {
    private Archer archer;
    private Stage currentStage;
    private UI ui;
    private GamePanel gamePanel;

    public Game() {
        setTitle("Archer vs Monsters Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        archer = new Archer(400, 300, 100); // Initialize archer
        ui = new UI(archer); // Initialize UI

        gamePanel = new GamePanel(ui, archer); // Updated constructor
        gamePanel.setPreferredSize(new Dimension(800, 600));
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }
}

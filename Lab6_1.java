import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Lab6_1 extends JPanel implements Runnable {

    private String[] texts = {
            "Hello world",
            "Java Swing animation",
            "Random moving string",
            "Lab 6 task 1"
    };

    private String currentText;
    private int x = 50, y = 50;
    private int dx, dy;

    public Lab6_1() {
        Random r = new Random();
        currentText = texts[r.nextInt(texts.length)];

        // Випадковий напрямок руху
        int dir = r.nextInt(4);
        switch (dir) {
            case 0 -> { dx = 2; dy = 0; }
            case 1 -> { dx = -2; dy = 0; }
            case 2 -> { dx = 0; dy = 2; }
            case 3 -> { dx = 0; dy = -2; }
        }

        new Thread(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(currentText, x, y);
    }

    @Override
    public void run() {
        while (true) {
            try { Thread.sleep(30); } catch (Exception ignored) {}

            x += dx;
            y += dy;

            if (x < 0 || x > getWidth() - 150 ||
                y < 20 || y > getHeight() - 20) {

                Random r = new Random();
                currentText = texts[r.nextInt(texts.length)];

                int dir = r.nextInt(4);
                switch (dir) {
                    case 0 -> { dx = 2; dy = 0; }
                    case 1 -> { dx = -2; dy = 0; }
                    case 2 -> { dx = 0; dy = 2; }
                    case 3 -> { dx = 0; dy = -2; }
                }
            }

            repaint();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab6-1 Animation");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Lab6_1());
        frame.setVisible(true);
    }
}

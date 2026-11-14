package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Board extends JComponent implements Runnable, KeyListener {

    // Constantes descriptivas para evitar números fijos dispersos (Recomendación Actividad 5)
    private static final int BOARD_WIDTH = 640;
    private static final int BOARD_HEIGHT = 480;
    private static final int BORDER_THICKNESS = 5;
    private static final int REPAINT_DELAY_MS = 100;
    private static final Font FONT_PAUSED = new Font("SansSerif", Font.BOLD, 24);

    private Dimension preferredSize = null;
    private Ellipse2D.Double ball;
    private Paddle paddle;
    private BallRunner ballRunner;
    private boolean isPaused = false;
    private boolean running = true;

    public Board() {
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(
                BORDER_THICKNESS, BORDER_THICKNESS,
                BORDER_THICKNESS, BORDER_THICKNESS, Color.BLACK));

        // Inicialización de componentes del modelo de juego
        ball = new Ellipse2D.Double(20, 320, 20, 20);
        paddle = new Paddle();
        ballRunner = new BallRunner(ball, paddle);

        // Inicio de hilos (Lógica y Renderizado independientes)
        Thread t1 = new Thread(ballRunner);
        t1.start();

        Thread t2 = new Thread(this);
        t2.start();
    }

    @Override
    protected void paintComponent(Graphics graphicsContext) {
        super.paintComponent(graphicsContext);
        Graphics2D g = (Graphics2D) graphicsContext;

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
        }

        // Renderizado del estado de pausa (Separado de la lógica)
        if (isPaused) {
            g.setFont(FONT_PAUSED);
            g.setColor(Color.BLUE);
            g.drawString("Paused", 20, 30);
        }

        g.setStroke(new BasicStroke(5.0f));
        g.setColor(Color.RED);
        g.fill(ball);
        g.setColor(Color.BLACK);
        g.fill(paddle.getPaddle());
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        } else {
            return super.getPreferredSize();
        }
    }

    public void setPreferredSize(Dimension newPrefSize) {
        preferredSize = newPrefSize;
        super.setPreferredSize(newPrefSize);
    }

    // Hilo dedicado exclusivamente a solicitar el repintado gráfico
    @Override
    public void run() {
        while (running) {
            repaint();
            try {
                Thread.sleep(REPAINT_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopGame() {
        running = false;
        if (ballRunner != null) {
            ballRunner.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Control de entrada separado (Teclado)
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (!isPaused) {
                    paddle.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!isPaused) {
                    paddle.moveRight();
                }
                break;
            case KeyEvent.VK_SPACE:
                isPaused = true;
                ballRunner.pause();
                repaint();
                break;
            case KeyEvent.VK_R:
                isPaused = false;
                ballRunner.resume();
                repaint();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
package mx.unison.pong;

import java.awt.geom.Ellipse2D;

public class BallRunner implements Runnable {

    public static final int MAX_X = 640;
    public static final int MAX_Y = 480;
    public static final int SIGN = -1;

    public static final int DX = 20;
    public static final int DY = 20;
    private final Ellipse2D.Double ball;

    private final Paddle paddle;

    private int ballX;
    private int ballY;

    private boolean isPaused;
    private boolean stop = false;

    public enum GameState {
        RUNNING, PAUSED, STOPPED
    }

    private GameState state = GameState.RUNNING;

    public BallRunner(Ellipse2D.Double shape, Paddle p) {
        super();
        ball = shape;
        paddle = p;
        ballX = 320;
        ballY = 240;
        ball.x = ballX;
        ball.y = ballY;
        isPaused = false;
    }

    public synchronized void pause() {
        state = GameState.PAUSED;
    }

    public synchronized void resume() {
        state = GameState.RUNNING;
        notifyAll(); // Despierta al hilo para que continúe sin gastar CPU
    }

    public synchronized void stop() {
        state = GameState.STOPPED;
        notifyAll();
    }

    @Override
    public void run() {
        int directionY = 1;
        int directionX = 1;

        while (state != GameState.STOPPED) {

            // Bloqueo eficiente para evitar espera activa (sin Thread.yield)
            synchronized (this) {
                while (state == GameState.PAUSED) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            if (state == GameState.STOPPED) break;

            if (paddle.intersects(ball)) {
                directionY = directionY * SIGN;
            }

            if (ball.getMinY() < 0 || ball.getMaxY() > MAX_Y) {
                directionY = directionY * SIGN;
            }

            if (ball.getMinX() < 0 || ball.getMaxX() > MAX_X) {
                directionX = directionX * SIGN;
            }

            // Corrección de multiplicación y signos
            ballX = ballX + (DX * directionX);
            ballY = ballY + (DY * directionY);

            ball.x = ballX;
            ball.y = ballY;

            try {
                Thread.sleep(150L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
package game;

import Items.Ball;
import Items.Bat;
import Items.PingPongTable;
import Items.ScoreBoard;

public class PingPongRulesImpl implements PingPongRules {
    private static final int MOVING_UP = 1;
    private static final int MOVING_DOWN = -1;
    private Ball ball;
    private Bat leftBat;
    private Bat rightBat;
    private ScoreBoard scoreBoard;
    private PingPongTable pingPongTable;

    public PingPongRulesImpl(Ball ball, Bat leftBat, Bat rightBat, ScoreBoard scoreBoard, PingPongTable pingPongTable) {
        this.ball = ball;
        this.leftBat = leftBat;
        this.rightBat = rightBat;
        this.scoreBoard = scoreBoard;
        this.pingPongTable = pingPongTable;
    }

    public boolean isBallKnockedByBat(Ball ball, Bat bat) {
        return ball.getY() == bat.getY() && isBallOnBatHeight(ball, bat);
    }

    private boolean isBallOnBatHeight(Ball ball, Bat bat) {
        return ball.getX() == bat.getX1() || ball.getX() == bat.getX2() || ball.getX() == bat.getX3();
    }

    @Override
    public boolean pointScored(Bat bat) {
        return !isBallKnockedByBat(ball, bat) && pingPongTable.isBallEscaped(ball.getX(), ball.getY());
    }

    private String whichPlayerScored() {
        if (ball.getY() == 0 && !isBallKnockedByBat(ball, leftBat)) {
            return "RightBat";
        } else if (ball.getY() == 15 && !isBallKnockedByBat(ball, rightBat)){
            return "LeftBat";
        }
        return "Something not right";
    }

    @Override
    public void changeBallDirection(Bat bat, Ball ball) {
        if (isBallKnockedByBat(ball, bat)) {
            if (ball.getDirection() == 1) {
                ball.setDirection(2);
            } else if (ball.getDirection() == 2) {
                ball.setDirection(1);
            }
        }
    }

    @Override
    public void resetBallPositions() {
        ball.setY(8);
        ball.setX(5);
    }

    @Override
    public void resetLeftBatPosition() {
        leftBat.setY(1);
        leftBat.setX1(4);
        leftBat.setX2(5);
        leftBat.setX3(6);
    }

    @Override
    public void resetRightBatPosition() {
        rightBat.setY(13);
        rightBat.setX1(4);
        rightBat.setX2(5);
        rightBat.setX3(6);
    }

    @Override
    public void updateScore() {
        String winner = whichPlayerScored();
        if ("LeftBat".equalsIgnoreCase(winner)) {
            scoreBoard.setLeftBatScore(scoreBoard.getLeftBatScore() + 1);
        } else if ("RightBat".equalsIgnoreCase(winner)) {
            scoreBoard.setRightBatScore(scoreBoard.getRightBatScore() + 1);
        }
    }

    @Override
    public void moveBat(char direction, Bat bat) {
        switch (direction) {
            case 's':
                tryMoveBat(1, 0, bat);
                break;
            case 'w' :
                tryMoveBat(-1,0, bat);
                break;
        }
    }

    @Override
    public void moveBall() {
        switch(ball.getDirection()){
            case 1:
                tryMoveBall(-1);
                break;
            case 2:
                tryMoveBall(1);
                break;
        }
    }

    private void tryMoveBall(int moveY) {
        if (!pingPongTable.isBallBouncedToWall(ball.getX() + whichDirectionBallGoes(ball.getVerticalDirection()), ball.getY() + moveY )) {
            ball.setX(ball.getX() + whichDirectionBallGoes(ball.getVerticalDirection()));
            ball.setY(ball.getY() + moveY);
        } else if(pingPongTable.isBallBouncedToWall(ball.getX() + whichDirectionBallGoes(ball.getVerticalDirection()), ball.getY() + moveY)) {
            if (ball.getVerticalDirection() == 3) {
                ball.setX(ball.getX() + whichDirectionBallGoes(ball.getVerticalDirection()));
                ball.setY(ball.getY() + moveY);
            } else if (ball.getVerticalDirection() == 1) {
                ball.setVerticalDirection(3);
                ball.setX(ball.getX() + whichDirectionBallGoes(ball.getVerticalDirection()));
                ball.setY(ball.getY() + moveY);
            }
        }
    }

    private int whichDirectionBallGoes(int verticalDirection) {
        int moveX;
                switch(verticalDirection) {
            case 1 : moveX = -1;
                break;
            case 3 : moveX = 1;
                break;
            default:  moveX = 0;
                break;
        }
        return moveX;
    }

    private void tryMoveBat(int moveX, int moveY, Bat bat) {
        if (moveX == MOVING_UP) {
            if (!pingPongTable.isBatBumpToWall(bat.getX3()+moveX,bat.getY() + moveY)) {
                bat.setX1(bat.getX1()+moveX);
                bat.setX2(bat.getX2()+moveX);
                bat.setX3(bat.getX3()+moveX);
            }
        } else if (moveX == MOVING_DOWN) {
            if (!pingPongTable.isBatBumpToWall(bat.getX1()+moveX,bat.getY() + moveY)) {
                bat.setX1(bat.getX1()+moveX);
                bat.setX2(bat.getX2()+moveX);
                bat.setX3(bat.getX3()+moveX);
            }
        }
    }
}
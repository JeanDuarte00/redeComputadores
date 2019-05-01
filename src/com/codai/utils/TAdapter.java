package com.codai.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {


    private TAdapter tAdapter;
    private boolean rightDirection = true;
    private boolean leftDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean enterKey = false;

    public boolean isEnterKey() { return enterKey;}

    public boolean isRightDirection() {
        return rightDirection;
    }

    public boolean isLeftDirection() {
        return leftDirection;
    }

    public boolean isUpDirection() {
        return upDirection;
    }

    public boolean isDownDirection() {
        return downDirection;
    }

    public void setEnterKey(boolean enterKey) { this.enterKey = enterKey;}

    public void setRightDirection(boolean rightDirection) {
        this.rightDirection = rightDirection;
    }

    public void setLeftDirection(boolean leftDirection) {
        this.leftDirection = leftDirection;
    }

    public void setUpDirection(boolean upDirection) {
        this.upDirection = upDirection;
    }

    public void setDownDirection(boolean downDirection) {
        this.downDirection = downDirection;
    }


    public TAdapter (boolean right, boolean left, boolean up, boolean down, boolean enter) {

        this.enterKey = enter;
        this.upDirection = up;
        this.downDirection = down;
        this.leftDirection = left;
        this.rightDirection = right;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if( key == KeyEvent.VK_ENTER){
            enterKey = true;
        }

        if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_UP) && (!downDirection)) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }

        if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }
}
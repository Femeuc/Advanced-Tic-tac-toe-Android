package com.femeuc.advancedtic_tac_toe;

public class Square {
    private boolean isOccupied;
    private Alliance alliance;
    private int id;

    public Square(int id) {
        this.isOccupied = false;
        alliance = null;
        this.id = id;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    private void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
        this.setOccupied(true);
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

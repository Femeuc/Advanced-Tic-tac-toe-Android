package com.femeuc.advancedtic_tac_toe.classes;

import com.femeuc.advancedtic_tac_toe.classes.Alliance;

public class Square {
    private Alliance alliance;
    private int id;

    public Square(int id) {
        alliance = null;
        this.id = id;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return alliance;
    }

}

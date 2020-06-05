package com.femeuc.advancedtic_tac_toe;

public enum Alliance {
    CROSS {
        @Override
        public Alliance getOppositeAlliance() {
            return CIRCLE;
        }

        @Override
        public boolean isCross() {
            return true;
        }

        @Override
        public boolean isCircle() {
            return false;
        }

        @Override
        public String toString() {
            return "X";
        }
    },
    CIRCLE {
        @Override
        public Alliance getOppositeAlliance() {
            return CROSS;
        }

        @Override
        public boolean isCross() {
            return false;
        }

        @Override
        public boolean isCircle() {
            return true;
        }

        @Override
        public String toString() {
            return "O";
        }
    };

    public abstract Alliance getOppositeAlliance();

    public abstract boolean isCross();

    public abstract boolean isCircle();

    @Override
    public abstract String toString();
}

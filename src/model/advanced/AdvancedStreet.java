package model.advanced;

public class AdvancedStreet extends AdvancedVertex {
    public enum StreetTypes {
        DUMMY,
        CROSSING,
        TJUNCTION,
        LINE,
        CURVE,
        NEEDLE
    }
    StreetTypes type;

    public AdvancedStreet(int row, int column, int value, StreetTypes type) {
        super(row, column, value);
        this.type = type;
        initParts(type);
    }

    void initParts(StreetTypes type) {
        switch (type) {
            case DUMMY:
                parts = new boolean[][] {
                        {false, false, false},
                        {false, true, false},
                        {false, false, false}
                };
                break;
            case CROSSING:
                parts = new boolean[][] {
                        {false, true, false},
                        {true, true, true},
                        {false, true, false}
                };
                break;
            case TJUNCTION:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, true},
                        {false, true, false}
                };
                break;
            case LINE:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, true},
                        {false, false, false}
                };
                break;
            case CURVE:
                parts = new boolean[][] {
                        {false, true, false},
                        {true, true, false},
                        {false, false, false}
                };
                break;
            case NEEDLE:
                parts = new boolean[][] {
                        {false, false, false},
                        {true, true, false},
                        {false, false, false}
                };
                break;
        }
    }

    public StreetTypes getType() {
        return type;
    }

    public void setType(StreetTypes type) {
        this.type = type;
    }
}

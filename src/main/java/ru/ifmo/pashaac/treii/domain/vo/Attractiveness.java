package ru.ifmo.pashaac.treii.domain.vo;

/**
 * Created by Pavel Asadchiy
 * on 22:52 18.10.17.
 */
public enum Attractiveness {

    LOW("D8F781"),
    MEDIUM("A2E726"),
    GOOD("FFD700"),
    HIGH("FF9A00"),
    VERY_HIGH("FF0000");

    private String color;

    Attractiveness(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

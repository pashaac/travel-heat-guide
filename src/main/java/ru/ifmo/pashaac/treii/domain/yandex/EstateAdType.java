package ru.ifmo.pashaac.treii.domain.yandex;

/**
 * Created by Pavel Asadchiy
 * on 1:14 15.11.17.
 */
public enum EstateAdType {
    RENT, SALE;

    public static EstateAdType valueOfByCode(int code) {
        switch (code) {
            case 1:
                return SALE;
            case 2:
                return RENT;
            default:
                throw new IllegalArgumentException("No estate ad type for code: " + code);
        }
    }
}

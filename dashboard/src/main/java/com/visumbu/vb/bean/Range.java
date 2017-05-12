/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

/**
 *
 * @author deeta1
 */
public enum Range {
    TODAY(0),
    YESTERDAY(1),
    THIS_WEEK(2),
    LAST_WEEK(3),
    THIS_MONTH(4),
    LAST_MONTH(5),
    THIS_YEAR(6),
    LAST_YEAR(7),
    DAY(8),
    WEEK(9),
    MONTH(10),
    YEAR(11);

    private final int value;

    Range(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

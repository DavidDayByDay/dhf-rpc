package com.better.hessian;

import java.io.Serializable;

public class Car implements Serializable {
    private int year;
    private Model model;
    private Color color;

    public Car(){}
    public Car( Model model, Color color) {
        this.model = model;
        this.color = color;
    }
}

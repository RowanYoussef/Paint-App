package com.paintApp.paint.Shapes;

import com.paintApp.paint.Services.ShapeFactory;

import java.util.List;

public class Circle extends AbsShape implements ShapeIF  {
    private Double x;
    private Double y;
    private Double radius;
    public Double getRadius() {
        return radius;
    }
    public void setRadius(Double radius) {
        this.radius = radius;
    }
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }


    @Override
    public void draw(String Name,int id, String boarderColor, boolean fill, List<Double> dim) {
        this.setName("Circle");
        this.setId(id);
        this.setBoarderColor(boarderColor);
        this.setFill(fill);
        this.setX(dim.get(0));
        this.setY(dim.get(1));
        this.setRadius(dim.get(4));

    }

    @Override
    public String toString() {
        return super.toString() +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}

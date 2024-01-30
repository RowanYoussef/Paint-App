package com.paintApp.paint.Shapes;

import java.util.List;

public class Rectangle extends AbsShape  implements ShapeIF {
    private Double x;
    private Double y;
    private Double height;
    private Double width;

    @Override
    public String toString() {
        return super.toString() +
                "x=" + x +
                ", y=" + y +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    public Double getX1() {
        return x;
    }

    public void setX1(Double x) {
        this.x = x;
    }

    public Double getY1() {
        return y;
    }

    public void setY1(Double y) {
        this.y = y;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
    @Override
    public void draw(String Name,int id, String boarderColor, boolean fill, List<Double> dim) {
        this.setName("Rectangle");
        this.setId(id);
        this.setBoarderColor(boarderColor);
        this.setFill(fill);
        this.setX1(dim.get(0));
        this.setY1(dim.get(1));
        this.setHeight(dim.get(2));
        this.setWidth(dim.get(3));

    }

}

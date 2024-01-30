package com.paintApp.paint.Shapes;

import java.util.List;

public class Line extends AbsShape implements ShapeIF {
    private Double x1;
    private Double x2;
    private Double y1;
    private Double y2;


    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }

    @Override
    public void draw(String Name,int id, String boarderColor, boolean fill, List<Double> dim) {
        this.setName("Line");
        this.setId(id);
        this.setBoarderColor(boarderColor);
        this.setFill(fill);
        this.setX1(dim.get(0));
        this.setY1(dim.get(1));
        this.setX2(dim.get(2));
        this.setY2(dim.get(3));
    }
    @Override
    public String toString() {
        return super.toString() +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }


}

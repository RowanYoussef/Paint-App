package com.paintApp.paint.Shapes;

import java.util.List;

public class Ellipse extends AbsShape implements ShapeIF  {
    private Double x1;
    private Double y1;
    private Double height;
    private Double width;

    @Override
    public String toString() {
        return super.toString()+
                "x1=" + x1 +
                ", y1=" + y1 +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
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
        this.setName("Ellipse");
        this.setId(id);
        this.setBoarderColor(boarderColor);
        this.setFill(fill);
        this.setX1(dim.get(0));
        this.setY1(dim.get(1));
        this.setHeight(dim.get(2));
        this.setWidth(dim.get(3));
    }

}

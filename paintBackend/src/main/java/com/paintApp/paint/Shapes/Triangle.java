package com.paintApp.paint.Shapes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Triangle extends AbsShape implements ShapeIF {
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;
    private Double x3;
    private Double y3;

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

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }

    public Double getX3() {
        return x3;
    }

    public void setX3(Double x3) {
        this.x3 = x3;
    }

    public Double getY3() {
        return y3;
    }

    public void setY3(Double y3) {
        this.y3 = y3;
    }

    @Override
    public void draw(String Name,int id, String boarderColor, boolean fill, List<Double> dim) {
           this.setName("Triangle");
           this.setId(id);
           this.setBoarderColor(boarderColor);
           this.setFill(fill);
           this.setX1(dim.get(0));
           this.setY1(dim.get(1));
           this.setX2(dim.get(2));
           this.setY2(dim.get(3));
           this.setX3(dim.get(4));
           this.setY3(dim.get(5));
    }
    @Override
	public String clone(String name, String id, String boardercolor ,String fill, String x,
			String y, String x1, String y1, String x2, String y2){
    	List<String> copy = new ArrayList<>();
    	ObjectMapper objectMapper = new ObjectMapper();
    	copy.add(name);
    	copy.add(id);
    	copy.add(boardercolor);
    	copy.add(fill);
    	copy.add(x);
    	copy.add(y);
    	copy.add(x1);
    	copy.add(y1);
    	copy.add(x2);
    	copy.add(y2);
    	return copy.toString();
    	
    }

    @Override
    public String toString() {
        return super.toString() +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", x3=" + x3 +
                ", y3=" + y3 +
                '}';
    }
}

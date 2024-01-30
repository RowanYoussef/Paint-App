package com.paintApp.paint.Shapes;

import java.util.List;

public interface ShapeIF   {
    public void draw(String name,int id, String boarderColor, boolean fill, List<Double> dim);
    
}

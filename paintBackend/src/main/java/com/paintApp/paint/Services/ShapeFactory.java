package com.paintApp.paint.Services;

import com.paintApp.paint.Shapes.*;

public class ShapeFactory {
    public ShapeIF getShape(String shape)
    {
        if(shape.equals("Circle"))
            return new Circle();
        else if (shape.equals("Line"))
            return new Line();
        else if (shape.equals("Rectangle"))
            return new Rectangle();
        else if(shape.equals("Triangle"))
            return new Triangle();
        else return new Ellipse();
    }
}

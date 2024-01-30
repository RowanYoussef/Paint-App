package com.paintApp.paint.Shapes;

import java.util.List;

public class AbsShape  {
    private String name;
    private int id;
    private String boarderColor;
    private boolean fill;
    private List<Double>dim;
    
    public void setDim(List<Double> dim) {
    	this.dim=dim;
    }
    

    public List<Double> getDim() {
		return dim;
	}


	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBoarderColor() {
        return boarderColor;
    }

    public void setBoarderColor(String boarderColor) {
        this.boarderColor = boarderColor;
    }

    public boolean getFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }


	@Override
	public String toString() {
		return "AbsShape [name=" + name + ", id=" + id + ", boarderColor=" + boarderColor + ", fill=" + fill + ", dim="
				+ dim + "]";
	}


	public void draw(String Name, int id, String boarderColor, boolean fill, List<Double> dim) {
		// TODO Auto-generated method stub
		
	}


	public String clone(String name, String id, String boardercolor, String fill, String x, String y, String x1,
			String y1, String x2, String y2) {
		// TODO Auto-generated method stub
		return null;
	}



}
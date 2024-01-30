/*package com.paintApp.paint;

import java.util.HashMap;
import java.util.List;

public class Store {
    HashMap<Integer , String[]> storeHashmap = new HashMap<>();

    public void setStoreHashmap(HashMap<Integer, String[]> storeHashmap) {
        this.storeHashmap = storeHashmap;
    }

    public HashMap<Integer, String[]> getStoreHashmap() {
        return storeHashmap;
    }

    public void storeInHashmap(int id, String boarderColor, String fillColor, List<Double> dim){
        String[] properties = new String[7];

        properties[0] = boarderColor;
        properties[1] = fillColor;
        properties[2] = String.valueOf(dim.get(0));
        properties[3] = String.valueOf(dim.get(1));
        properties[4] = String.valueOf(dim.get(2));
        properties[5] = String.valueOf(dim.get(3));
        properties[6] = String.valueOf(dim.get(4));

        storeHashmap.put(id,properties);
    }

    public void clearHashmap(){
        storeHashmap.clear();
    }
}*/

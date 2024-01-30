/*package com.paintApp.paint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;


public class SaveAndLoad {

    public void saveInXML(String filePath){
        Store store = new Store();
        try (FileOutputStream fos = new FileOutputStream(new File(filePath).getAbsolutePath())){

            XMLEncoder xmlEncoder = new XMLEncoder(fos);
            xmlEncoder.writeObject(store.storeHashmap);
            store.clearHashmap();
            xmlEncoder.close();
        }
        catch (FileNotFoundException ex){
            throw new RuntimeException(ex);
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public HashMap<Integer, String[]> loadFromXML(String filePath){
        Store store = new Store();
        HashMap<Integer , String[]> loaded = new HashMap<>();
        try(FileInputStream fis = new FileInputStream(new File(filePath).getAbsolutePath())){

            XMLDecoder xmlDecoder = new XMLDecoder(fis);
            loaded = (HashMap<Integer, String[]>) xmlDecoder.readObject();
            xmlDecoder.close();
        }
        catch (FileNotFoundException ex){
            throw new RuntimeException(ex);
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
        store.setStoreHashmap(loaded);
        return loaded;
    }

    public void saveInJSON(String filePath){
        Store store = new Store();
        ObjectMapper om = new ObjectMapper();

        try{
            String toBeSaved = om.writeValueAsString(store.storeHashmap);
            store.clearHashmap();
            FileWriter fw = new FileWriter(filePath);
            fw.write(toBeSaved);
            fw.flush();
            fw.close();

        }catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}*/

package com.paintApp.paint.Controlles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintApp.paint.Services.ShapeFactory;
//import com.paintApp.paint.Services.UndoAndRedoService;
import com.paintApp.paint.Shapes.ShapeIF;
import jdk.jfr.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    ShapeFactory shapeFactoryService = new ShapeFactory();
   // UndoAndRedoService undoAndRedoService=new UndoAndRedoService();
    private List<ShapeIF> myShapes=new ArrayList<>();

    @RequestMapping(method=RequestMethod.POST,value="/shape")
    public void drawShape(@RequestParam String type, @RequestParam int id,
            @RequestParam String boardercolor, @RequestParam boolean fill,
            @RequestParam List<Double> dim) {
        shapeFactoryService = new ShapeFactory();
        ShapeIF shapeIF = shapeFactoryService.getShape(type);
        shapeIF.draw(type, id,boardercolor,fill,dim);
        myShapes.add(shapeIF);
        System.out.println(myShapes.get(0));
    }
   /* @GetMapping("/clone")
    public String copy(@RequestParam String type, @RequestParam int id,
                       @RequestParam String boardercolor, @RequestParam boolean fill,
                       @RequestParam List<Double> dim)
    {
        ShapeIF shapeIF =shapeFactoryService.getShape(type);
        shapeIF.draw(type,id,boardercolor,fill,dim);
        myShapes.add(shapeIF);
        try {
           return  new ObjectMapper().writeValueAsString(shapeIF.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/undo")
    public String Undo() throws JsonProcessingException {
        if (myShapes.size() != 0) {
            undoAndRedoService.undoAndRedo.add(myShapes.remove(myShapes.size() - 1));
            undoAndRedoService.setFlag(undoAndRedoService.getFlag+1);
        }
        return new ObjectMapper().writeValueAsString(myShapes.toString());
    }
    @GetMapping("/redo")
    public String Redo() throws JsonProcessingException {
        if (undoAndRedoService.getFlag() >0) {
            myShapes.add(undoAndRedoService.undoAndRedo.remove(undoAndRedoService.undoAndRedo.size()-1));
            undoAndRedoService.setFlag(undoAndRedoService.getFlag-1);
        }
        return new ObjectMapper().writeValueAsString(myShapes.toString());

    }*/
}

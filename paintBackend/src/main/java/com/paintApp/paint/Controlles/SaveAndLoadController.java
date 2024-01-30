/*package com.paintApp.paint.Controlles;

import com.paintApp.paint.SaveAndLoad;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class SaveAndLoadController {
    @GetMapping("/saveXML")
    public void saveXML (@RequestParam String filePath){
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        saveAndLoad.saveInXML(filePath);
        return;
    }

    @GetMapping("/saveJSON")
    public void savaJSON (@RequestParam String filePath){
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        saveAndLoad.saveInJSON(filePath);
        return;
    }
}*/

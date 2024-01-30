package com.paintApp.paint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.paintApp.paint.Controllers"})
@ComponentScan({"com.paintApp.paint.Services"})
@ComponentScan({"com.paintApp.paint.Shapes"})
public class PaintApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaintApplication.class, args);
	}

}

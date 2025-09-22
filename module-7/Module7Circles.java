package com.mycompany.csd420_project01;

/*
 * Scott Macioce
 * 09/22/2025
 * CSD420 - Module 7 Assignment
 * Purpose: Display four circles with styles applied from external CSS file mystyle.css
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Module7Circles extends Application {

    @Override
    public void start(Stage stage) {
        // Create four circles
        Circle circle1 = new Circle(40);
        circle1.getStyleClass().add("plaincircle");

        Circle circle2 = new Circle(40);
        circle2.getStyleClass().add("plaincircle");
        circle2.getStyleClass().add("circleborder");

        Circle circle3 = new Circle(40);
        circle3.setId("redcircle");

        Circle circle4 = new Circle(40);
        circle4.setId("greencircle");

        // Put circles in an HBox
        HBox circles = new HBox(10, circle1, circle2, circle3, circle4);

        // Surround everything with a bordered VBox
        VBox container = new VBox(circles);
        container.getStyleClass().add("border");

        // Create scene and apply stylesheet
        Scene scene = new Scene(new StackPane(container), 400, 150, Color.WHITE);
        scene.getStylesheets().add("mystyle.css");

        stage.setTitle("Module 7 Programming Assignment");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

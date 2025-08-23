/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.mycompany.csd420_project01;

/*
 * Scott Macioce | 2025-08-22 | CSD420-J307 - Assignment: Random Card Viewer
 * Purpose: Display four randomly selected card images from a "cards" directory.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomCardViewer extends Application {

    //Constants & simple configuration
    private static final int CARD_COUNT = 52;     // image files are 1.png..52.png
    private static final String CARDS_DIR = "cards"; // folder holding images
    private static final double CARD_WIDTH = 120; // displayed width

    // UI nodes reused between refreshes
    private final FlowPane cardRow = new FlowPane(10, 10); 
    private Set<Integer> lastHand = new HashSet<>();       

    @Override
    public void start(Stage stage) {
        //Build UI container: cards row + Refresh button
        cardRow.setPadding(new Insets(12));

        Button refreshBtn = new Button("Refresh");
        // Lambda expression wires the click to refresh method
        refreshBtn.setOnAction(e -> showNewHand());

        VBox root = new VBox(10, cardRow, refreshBtn);
        root.setPadding(new Insets(12));

        // First draw
        showNewHand();

        stage.setTitle("Random Card Viewer (JavaFX)");
        stage.setScene(new Scene(root, 560, 240));
        stage.show();
    }

    /*Draws four unique random cards and updates the UI*/
    private void showNewHand() {
        Set<Integer> newHand;
        do {
            newHand = pickFourDistinct();
        } while (newHand.equals(lastHand)); // simple guard so Refresh actually changes the set

        // Replace thumbnails in the FlowPane
        cardRow.getChildren().setAll(buildImageViews(newHand));
        lastHand = newHand;
    }

    /*Returns a set of 4 distinct integers between 1 and CARD_COUNT*/
    private Set<Integer> pickFourDistinct() {
        List<Integer> deck = new ArrayList<>(CARD_COUNT);
        for (int i = 1; i <= CARD_COUNT; i++) deck.add(i);
        Collections.shuffle(deck);
        return new HashSet<>(deck.subList(0, 4));
    }

    /*Creates ImageViews for each card number using files like cards/7.png */
    private List<ImageView> buildImageViews(Set<Integer> hand) {
        List<ImageView> views = new ArrayList<>(4);
        for (int n : hand) {
            Image img = loadCardImage(n);
            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(CARD_WIDTH);
            views.add(iv);
        }
        return views;
    }

    /** Loads a card image by index from the local "cards" directory. */
    private Image loadCardImage(int n) {
        File f = new File(CARDS_DIR, n + ".png");
        return new Image(f.toURI().toString(), false); // no background loading needed
    }

    public static void main(String[] args) {
        launch(args);
    }
}


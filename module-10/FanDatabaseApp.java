/*
 * Scott Macioce
 * 10/05/2025
 * Assignment: Module 10 – Fan Database Viewer and Updater
 * 
 * Purpose: Connects to a MySQL database and allows
 *          the user to display and update fan records in the 'fans' table.
 *          
 */

package com.mycompany.csd420_project01;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class FanDatabaseApp extends Application {

    // Database connection details
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/databasedb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "student1";
    private static final String PASSWORD = "pass";

    // UI Components
    private TextField idField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();
    private TextField favoriteTeamField = new TextField();
    private Label statusLabel = new Label("Ready");

    @Override
    public void start(Stage stage) {
        // Labels
        Label idLabel = new Label("Fan ID:");
        Label firstLabel = new Label("First Name:");
        Label lastLabel = new Label("Last Name:");
        Label teamLabel = new Label("Favorite Team:");

        // Buttons
        Button displayButton = new Button("Display");
        Button updateButton = new Button("Update");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(firstLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(lastLabel, 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(teamLabel, 0, 3);
        grid.add(favoriteTeamField, 1, 3);

        HBox buttons = new HBox(10, displayButton, updateButton);
        grid.add(buttons, 1, 4);
        grid.add(statusLabel, 1, 5);

        // Event Handlers
        displayButton.setOnAction(e -> displayRecord());
        updateButton.setOnAction(e -> updateRecord());

        // Scene setup
        Scene scene = new Scene(grid, 400, 250);
        stage.setTitle("Fan Database Viewer & Updater");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Display a fan record from the database based on ID entered.
     */
    private void displayRecord() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            statusLabel.setText("Enter a Fan ID to display.");
            return;
        }

        String sql = "SELECT firstname, lastname, favoriteteam FROM fans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idText));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                firstNameField.setText(rs.getString("firstname"));
                lastNameField.setText(rs.getString("lastname"));
                favoriteTeamField.setText(rs.getString("favoriteteam"));
                statusLabel.setText("Record displayed successfully.");
            } else {
                clearFields();
                statusLabel.setText("No record found for ID: " + idText);
            }

        } catch (SQLException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Update an existing fan record in the database.
     */
    private void updateRecord() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            statusLabel.setText("Enter a Fan ID to update.");
            return;
        }

        String sql = "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstNameField.getText().trim());
            ps.setString(2, lastNameField.getText().trim());
            ps.setString(3, favoriteTeamField.getText().trim());
            ps.setInt(4, Integer.parseInt(idText));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                statusLabel.setText("Record updated successfully.");
            } else {
                statusLabel.setText("No record found to update.");
            }

        } catch (SQLException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Clears text fields when no record is found.
     */
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        favoriteTeamField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

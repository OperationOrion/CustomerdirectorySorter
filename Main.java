import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main extends Application {

    private List<String> names = new ArrayList<>();
    
    public void start(Stage primaryStage) {
    	
        //Create basic UI components
        Label inputLabel = new Label("Enter Name:");
        TextField nameField = new TextField();
        Button addButton = new Button("Add Name");
        Button loadButton = new Button("Load File");
        Button saveButton = new Button("Save Sorted Names");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(200);
        
        //Set up layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(inputLabel, nameField, addButton, loadButton, resultArea, saveButton);
        
        //Create scene
        Scene scene = new Scene(layout, 400, 400);
        
        //Add button action
        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                names.add(name);
                sortAndDisplayNames(resultArea);
                nameField.clear();
            } else {
                resultArea.setText("Please enter a valid name (non-empty)");
            }
        });
        
        // Load button action
        loadButton.setOnAction(e -> {
            loadFromFile(primaryStage, resultArea);
        });
        
        // Save button action
        saveButton.setOnAction(e -> {
            if (!names.isEmpty()) {
                saveToFile(primaryStage);
            } else {
                resultArea.setText("No names to save!");
            }
        });
        
        // Set up stage
        primaryStage.setTitle("Name Sorter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void sortAndDisplayNames(TextArea resultArea) {
        // Sort names: first by first letter (case-insensitive), then alphabetically within each group
        names.sort(Comparator.comparing((String s) -> s.isEmpty() ? '\0' : s.toLowerCase().charAt(0))
                            .thenComparing(String::compareToIgnoreCase));
        
        // Display sorted names
        StringBuilder displayText = new StringBuilder();
        for (String name : names) {
            displayText.append(name).append("\n");
        }
        resultArea.setText(displayText.toString());
    }
    
    //load name data set from existing file on desktop using FileChooser
    private void loadFromFile(Stage stage, TextArea resultArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Names File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        //bufferedReader to interpret file
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            	//Clear existing names
                names.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (!trimmedLine.isEmpty()) {
                        names.add(trimmedLine);
                    }
                }
                //if file doesnt contain names, throw error
                if (!names.isEmpty()) {
                    sortAndDisplayNames(resultArea);
                } else {
                    resultArea.setText("No valid names found in the file!");
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error reading file: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    //save to file new sorted name set
    private void saveToFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Sorted Names");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("sorted_names.txt");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String name : names) {
                    writer.write(name);
                    writer.newLine();
                }
                //success saving file notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("File saved successfully!");
                alert.showAndWait();
            } catch (IOException e) {
            	//problem saving file notification
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error saving file: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    //End of current program capabilities - MM - 7/15/25
    public static void main(String[] args) {
        launch(args);
    }
}
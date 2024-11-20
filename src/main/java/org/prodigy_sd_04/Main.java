package org.prodigy_sd_04;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    Solver solver = new Solver();

    HBox main_1 = new HBox();
    VBox main_1a = new VBox();
    GridPane[] main_2 = {new GridPane(), new GridPane()};
    TextField[] unfilled = new TextField[81];
    TextField[] filled = new TextField[81];
    Button main_3 = new Button("Solve");
    Button resetButton = new Button("Clear");

    @Override
    public void start(Stage stage) {
        main_2[0].setPadding(new Insets(10, 10, 10, 10));
        main_2[1].setPadding(new Insets(10, 10, 10, 0));

        main_2[0].getStyleClass().add("grid-pane");
        main_2[1].getStyleClass().add("grid-pane");

        main_1.setSpacing(20);

        int control = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                unfilled[control] = createTextField("unfilled", row, col);
                filled[control] = createTextField("filled", row, col);

                main_2[0].add(unfilled[control], col, row);
                main_2[1].add(filled[control], col, row);
                control++;
            }
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(main_3, resetButton);
        buttonBox.getStyleClass().add("button-box");

        main_3.setOnAction(e -> solveIt());
        resetButton.setOnAction(e -> resetPuzzle());

        main_1.getChildren().addAll(main_2[0], main_2[1]);
        main_1a.getChildren().addAll(main_1, buttonBox);

        Scene sc = new Scene(main_1a);
        sc.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Sudoku Solver");
        stage.setScene(sc);
        stage.show();
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void resetPuzzle() {
        for (TextField field : unfilled) {
            field.clear();
        }
        for (TextField field : filled) {
            field.clear();
            field.setStyle("-fx-font-size: 25px");
        }
        showAlert(AlertType.INFORMATION, "Clear", "The Sudoku puzzle has been cleared.");
    }

    private TextField createTextField(String type, int row, int col) {
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        textField.setMaxHeight(50);
        textField.getStyleClass().add(type);
        textField.getStyleClass().add("text-field");

        if (col % 3 == 2 && col != 8) textField.getStyleClass().add("right-border");
        if (row % 3 == 2 && row != 8) textField.getStyleClass().add("bottom-border");

        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-9]?")) {
                return change;
            }
            return null;
        }));

        return textField;
    }

    private void solveIt() {
        boolean can = true;
        int[][] read = new int[9][9];
        int ctrl = 0;

        // Check if the puzzle is empty
        boolean isEmpty = true;
        for (TextField field : unfilled) {
            if (!field.getText().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            showAlert(AlertType.ERROR, "Error", "Please enter some numbers ON THE LEFT,to solve the puzzle.");
            return;
        }

        // First pass: Read the initial values and store them
        boolean[] isInitialValue = new boolean[81];
        for (int i = 0; i < 81; i++) {
            if (!unfilled[i].getText().isEmpty()) {
                isInitialValue[i] = true;
            }
        }

        // Second pass: Process the values
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (unfilled[ctrl].getText().isEmpty()) {
                    read[row][col] = 0;
                } else {
                    try {
                        read[row][col] = Integer.parseInt(unfilled[ctrl].getText());
                        if (isInitialValue[ctrl]) {
                            filled[ctrl].setText(String.valueOf(read[row][col]));
                            filled[ctrl].setStyle("-fx-text-fill: black; -fx-font-size: 25px;");
                        }
                    } catch (NumberFormatException e) {
                        showAlert(AlertType.ERROR, "Error", "Invalid input. Please enter numbers only.");
                        can = false;
                    }
                }
                ctrl++;
            }
        }

        if (can) {
            boolean ok = solver.solve(read, 0, 0);
            if (ok) {
                read = solver.getSolved();
                ctrl = 0;
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (!isInitialValue[ctrl]) {
                            filled[ctrl].setText(String.valueOf(read[row][col]));
                            filled[ctrl].setStyle("-fx-text-fill: red; -fx-font-size: 25px;");
                        }
                        ctrl++;
                    }
                }
                showAlert(AlertType.INFORMATION, "Success", "Sudoku puzzle solved successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "This Sudoku puzzle cannot be solved. Please check your input.");
            }
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
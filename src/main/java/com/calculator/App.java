package com.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.application.Platform;

public class App extends Application {
    private TextField display;
    private TextField secondaryDisplay;
    private TextArea historyArea;
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private double memory = 0;
    private boolean isRadianMode = true;
    private boolean isDarkMode = true;
    private List<String> history = new ArrayList<>();
    private boolean isNewCalculation = true;
    private Scene scene;
    private Label memoryIndicator;

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout with a dark gradient background
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a1a, #2d2d2d);");

        // Initialize history area first
        historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefRowCount(3);
        historyArea.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-text-fill: white;");
        history = new ArrayList<>();

        // Create UI components
        setupUI(root, primaryStage);

        // Configure stage
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Scientific Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Apply initial theme
        applyTheme(isDarkMode);
    }

    private void setupUI(VBox root, Stage primaryStage) {
        // Create all UI components
        VBox displaySection = createDisplaySection();
        HBox controlBox = createControlBox();
        HBox calculatorLayout = createCalculatorLayout();
        VBox historySection = createHistorySection();
        HBox topBar = createTopBar(primaryStage);

        // Final assembly
        root.getChildren().addAll(
            topBar,
            displaySection,
            controlBox,
            calculatorLayout,
            historySection
        );

        // Create scene with initial size
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    // UI Creation Methods
    private HBox createTopBar(Stage primaryStage) {
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 15;");
        topBar.setPadding(new Insets(5, 10, 5, 10));

        ToggleButton themeToggle = new ToggleButton("🌙");
        themeToggle.setSelected(true);
        themeToggle.getStyleClass().add("theme-toggle");
        themeToggle.setOnAction(e -> {
            toggleTheme();
            themeToggle.setText(isDarkMode ? "🌙" : "☀");
        });

        Button copyButton = new Button("📋");
        copyButton.getStyleClass().add("copy-button");
        copyButton.setOnAction(e -> {
            copyResultToClipboard();
            FadeTransition ft = new FadeTransition(Duration.millis(200), copyButton);
            ft.setFromValue(1.0);
            ft.setToValue(0.5);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();
        });

        Button closeButton = new Button("×");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> primaryStage.close());

        topBar.getChildren().addAll(themeToggle, copyButton, closeButton);
        return topBar;
    }

    private VBox createDisplaySection() {
        VBox displaySection = new VBox(5);
        displaySection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 20;");
        displaySection.setPadding(new Insets(15));

        memoryIndicator = new Label("");
        memoryIndicator.setStyle("-fx-text-fill: #00ccff; -fx-font-size: 12px;");

        display = new TextField("0");
        display.setId("display");
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPrefHeight(60);
        display.setStyle("-fx-font-size: 32px; -fx-background-color: transparent; -fx-text-fill: white;");

        secondaryDisplay = new TextField();
        secondaryDisplay.setEditable(false);
        secondaryDisplay.setAlignment(Pos.CENTER_RIGHT);
        secondaryDisplay.setPrefHeight(30);
        secondaryDisplay.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: #888888;");

        displaySection.getChildren().addAll(memoryIndicator, secondaryDisplay, display);
        return displaySection;
    }

    private HBox createControlBox() {
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 15;");
        controlBox.setPadding(new Insets(10));

        ToggleButton radDegToggle = new ToggleButton("RAD");
        radDegToggle.setSelected(true);
        radDegToggle.getStyleClass().add("mode-toggle");
        radDegToggle.setOnAction(e -> {
            isRadianMode = radDegToggle.isSelected();
            radDegToggle.setText(isRadianMode ? "RAD" : "DEG");
        });

        HBox memoryBox = new HBox(5);
        memoryBox.getChildren().addAll(
            createMemoryButton("MC"),
            createMemoryButton("MR"),
            createMemoryButton("M+"),
            createMemoryButton("M-"),
            createMemoryButton("MS")
        );

        controlBox.getChildren().addAll(radDegToggle, new Separator(javafx.geometry.Orientation.VERTICAL), memoryBox);
        return controlBox;
    }

    private HBox createCalculatorLayout() {
        GridPane mainGrid = createMainGrid();
        GridPane scientificGrid = createScientificGrid();
        VBox rightButtons = createRightButtons();

        VBox leftSection = new VBox(8);
        leftSection.getChildren().addAll(scientificGrid, mainGrid);

        HBox calculatorLayout = new HBox(8);
        calculatorLayout.setAlignment(Pos.CENTER);
        calculatorLayout.getChildren().addAll(leftSection, rightButtons);

        return calculatorLayout;
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(8);
        mainGrid.setVgap(8);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(10));

        String[][] buttonLabels = {
            {"7", "8", "9", "÷"},
            {"4", "5", "6", "×"},
            {"1", "2", "3", "-"},
            {"0", ".", "±", "+"}
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            for (int j = 0; j < buttonLabels[i].length; j++) {
                String label = buttonLabels[i][j];
                Button btn;
                if (label.matches("[0-9.]")) {
                    btn = createNumberButton(label);
                } else if (label.equals("±")) {
                    btn = createPlusMinusButton();
                } else {
                    btn = createOperatorButton(label);
                }
                mainGrid.add(btn, j, i);
            }
        }

        return mainGrid;
    }

    private GridPane createScientificGrid() {
        GridPane scientificGrid = new GridPane();
        scientificGrid.setHgap(8);
        scientificGrid.setVgap(8);
        scientificGrid.setAlignment(Pos.CENTER);

        String[] scientificOps = {
            "sin", "cos", "tan",
            "log", "ln", "√",
            "x²", "x³", "xʸ",
            "1/x", "n!", "π",
            "e", "EXP", "mod"
        };

        for (int i = 0; i < scientificOps.length; i++) {
            Button btn = createScientificButton(scientificOps[i]);
            scientificGrid.add(btn, i % 3, i / 3);
        }

        return scientificGrid;
    }

    private VBox createRightButtons() {
        VBox rightButtons = new VBox(8);
        rightButtons.setAlignment(Pos.CENTER);
        rightButtons.getChildren().addAll(
            createClearButton(),
            createBackspaceButton(),
            createEqualsButton()
        );
        return rightButtons;
    }

    private VBox createHistorySection() {
        VBox historySection = new VBox(5);
        Label historyLabel = new Label("History");
        historyLabel.getStyleClass().add("history-label");
        
        historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefRowCount(3);
        historyArea.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-text-fill: white;");
        
        historySection.getChildren().addAll(historyLabel, historyArea);
        return historySection;
    }

    // Button Creation Methods
    private Button createNumberButton(String number) {
        Button btn = new Button(number);
        btn.getStyleClass().add("number-button");
        btn.setOnAction(e -> handleNumber(number));
        return btn;
    }

    private Button createOperatorButton(String operator) {
        Button btn = new Button(operator);
        btn.getStyleClass().add("operator-button");
        btn.setOnAction(e -> handleOperator(operator));
        return btn;
    }

    private Button createScientificButton(String function) {
        Button btn = new Button(function);
        btn.getStyleClass().add("scientific-button");
        btn.setOnAction(e -> handleScientificOperation(function));
        return btn;
    }

    private Button createEqualsButton() {
        Button btn = new Button("=");
        btn.getStyleClass().add("equals-button");
        btn.setOnAction(e -> handleEquals());
        return btn;
    }

    private Button createClearButton() {
        Button btn = new Button("C");
        btn.getStyleClass().add("function-button");
        btn.setOnAction(e -> handleClear());
        return btn;
    }

    private Button createBackspaceButton() {
        Button btn = new Button("⌫");
        btn.getStyleClass().add("function-button");
        btn.setOnAction(e -> handleBackspace());
        return btn;
    }

    private Button createPlusMinusButton() {
        Button btn = new Button("±");
        btn.getStyleClass().add("function-button");
        btn.setOnAction(e -> handlePlusMinus());
        return btn;
    }

    private Button createMemoryButton(String op) {
        Button btn = new Button(op);
        btn.getStyleClass().add("memory-button");
        btn.setOnAction(e -> handleMemoryOperation(op));
        return btn;
    }

    // Calculator Operation Methods
    public void handleNumber(String number) {
        if (isNewCalculation) {
            currentNumber = "";
            isNewCalculation = false;
        }
        currentNumber += number;
        display.setText(currentNumber);
    }

    public void handleOperator(String op) {
        if (!currentNumber.isEmpty()) {
            firstNumber = Double.parseDouble(currentNumber);
            operator = op;
            currentNumber = "";
            secondaryDisplay.setText(String.valueOf(firstNumber) + " " + operator);
        }
    }

    public void handleEquals() {
        if (!currentNumber.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            double result = calculateResult(firstNumber, secondNumber, operator);
            String operation = firstNumber + " " + operator + " " + secondNumber + " = " + result;
            addToHistory(operation);
            currentNumber = String.valueOf(result);
            display.setText(currentNumber);
            operator = "";
            secondaryDisplay.setText("");
            isNewCalculation = true;
        }
    }

    private double calculateResult(double first, double second, String op) {
        switch (op) {
            case "+": return first + second;
            case "-": return first - second;
            case "×": return first * second;
            case "÷": return second == 0 ? Double.NaN : first / second;
            default: return 0;
        }
    }

    public void handleClear() {
        currentNumber = "0";
        operator = "";
        firstNumber = 0;
        display.setText(currentNumber);
        secondaryDisplay.setText("");
        isNewCalculation = true;
    }

    public void handleBackspace() {
        if (currentNumber.length() > 0) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            if (currentNumber.isEmpty() || currentNumber.equals("-")) {
                currentNumber = "0";
            }
            display.setText(currentNumber);
        }
    }

    public void handlePlusMinus() {
        if (!currentNumber.isEmpty() && !currentNumber.equals("0")) {
            if (currentNumber.startsWith("-")) {
                currentNumber = currentNumber.substring(1);
            } else {
                currentNumber = "-" + currentNumber;
            }
            display.setText(currentNumber);
        }
    }

    public void handleScientificOperation(String operation) {
        if (currentNumber.isEmpty()) return;
        
        double number = Double.parseDouble(currentNumber);
        double result = 0;
        
        switch (operation) {
            case "sin":
                result = Math.sin(isRadianMode ? number : Math.toRadians(number));
                break;
            case "cos":
                result = Math.cos(isRadianMode ? number : Math.toRadians(number));
                break;
            case "tan":
                result = Math.tan(isRadianMode ? number : Math.toRadians(number));
                break;
            case "√":
                result = Math.sqrt(number);
                break;
            case "x²":
                result = Math.pow(number, 2);
                break;
            case "x³":
                result = Math.pow(number, 3);
                break;
            case "log":
                result = Math.log10(number);
                break;
            case "ln":
                result = Math.log(number);
                break;
            case "1/x":
                result = 1 / number;
                break;
            case "n!":
                result = factorial(number);
                break;
            case "π":
                result = Math.PI;
                break;
            case "e":
                result = Math.E;
                break;
        }
        
        String operation_str = operation + "(" + number + ") = " + result;
        addToHistory(operation_str);
        currentNumber = String.valueOf(result);
        display.setText(currentNumber);
        isNewCalculation = true;
    }

    private double factorial(double n) {
        if (n < 0) return Double.NaN;
        if (n == 0 || n == 1) return 1;
        return n * factorial(n - 1);
    }

    // Memory Operations
    public void handleMemoryOperation(String op) {
        switch (op) {
            case "MC":
                memory = 0;
                memoryIndicator.setText("");
                break;
            case "MR":
                currentNumber = String.valueOf(memory);
                display.setText(currentNumber);
                break;
            case "M+":
                memory += Double.parseDouble(currentNumber);
                memoryIndicator.setText("M");
                break;
            case "M-":
                memory -= Double.parseDouble(currentNumber);
                memoryIndicator.setText("M");
                break;
            case "MS":
                memory = Double.parseDouble(currentNumber);
                memoryIndicator.setText("M");
                break;
        }
    }

    // History Management
    private void addToHistory(String operation) {
        if (history.size() >= 10) {
            history.remove(0);
        }
        history.add(operation);
        updateHistoryDisplay();
    }

    private void updateHistoryDisplay() {
        StringBuilder sb = new StringBuilder();
        for (String op : history) {
            sb.append(op).append("\n");
        }
        historyArea.setText(sb.toString());
    }

    // Theme Management
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme(isDarkMode);
    }

    private void applyTheme(boolean darkMode) {
        String theme = darkMode ? "dark" : "light";
        scene.getRoot().setStyle(darkMode ? 
            "-fx-background-color: linear-gradient(to bottom right, #1a1a1a, #2d2d2d);" :
            "-fx-background-color: linear-gradient(to bottom right, #f0f0f0, #e0e0e0);"
        );
    }

    // Clipboard Operations
    private void copyResultToClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(display.getText());
        clipboard.setContent(content);
    }

    // Getters for Testing
    public String getDisplay() {
        return display.getText();
    }

    public String getSecondaryDisplay() {
        return secondaryDisplay.getText();
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public String getOperator() {
        return operator;
    }

    public double getFirstNumber() {
        return firstNumber;
    }

    public double getMemory() {
        return memory;
    }

    public boolean isRadianMode() {
        return isRadianMode;
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
}

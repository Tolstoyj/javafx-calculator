module com.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    
    exports com.calculator;
    opens com.calculator to javafx.fxml;
} 
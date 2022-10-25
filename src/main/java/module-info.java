module com.example.clientetextfinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clientetextfinder to javafx.fxml;
    exports com.example.clientetextfinder;
}
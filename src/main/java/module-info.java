module com.example.clientetextfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires Spire.Doc;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;


    opens com.example.clientetextfinder to javafx.fxml;
    exports com.example.clientetextfinder;
}
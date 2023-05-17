module com.example.dftplot {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.dftplot to javafx.fxml, com.google.gson;
    exports com.example.dftplot;



}
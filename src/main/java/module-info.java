module org.prodigy_sd_04 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.prodigy_sd_04 to javafx.fxml;
    exports org.prodigy_sd_04;
}
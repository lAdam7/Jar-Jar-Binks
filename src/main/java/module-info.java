module JarJarBinks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens JarJarBinks to javafx.fxml;
    exports JarJarBinks;
}
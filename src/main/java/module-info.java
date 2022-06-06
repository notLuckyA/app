module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;

opens com.myapp.app.Database.Hibernate;
opens com.myapp.app.Database.Tables;
exports com.myapp.app.Database.Tables;
exports com.myapp.app.Database.Hibernate;
    opens com.myapp.app to javafx.fxml;
    exports com.myapp.app;
    exports com.myapp.app.Contolers;
    opens com.myapp.app.Contolers to javafx.fxml;
    exports com.myapp.app.App;
    opens com.myapp.app.App to javafx.fxml;
}
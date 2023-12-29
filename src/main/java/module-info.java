module com.winery{
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires persistence.api;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;

    exports com.winery.entities;
    opens com.winery.entities to org.hibernate.orm.core;

    opens com.winery.winery_prod to javafx.fxml;
    exports com.winery.winery_prod;
    exports com.winery.controllers;
    opens com.winery.controllers to javafx.fxml, org.hibernate.orm.core;


}


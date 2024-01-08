module com.winery{

    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;

    exports com.winery.entities;
    opens com.winery.entities to org.hibernate.orm.core;
    opens com.winery.winery_prod to javafx.fxml;
    exports com.winery.winery_prod;
    exports com.winery.controllers;
    opens com.winery.controllers to javafx.fxml, org.hibernate.orm.core;
    exports com.winery.service;
    opens com.winery.service to javafx.fxml, org.hibernate.orm.core;


}


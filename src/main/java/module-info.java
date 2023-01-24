module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    exports com.example.socialnetworkgui;
    exports com.example.socialnetworkgui.models;
    exports com.example.socialnetworkgui.validators;
    exports com.example.socialnetworkgui.repository;
    exports com.example.socialnetworkgui.service;
    exports com.example.socialnetworkgui.controller;

    opens com.example.socialnetworkgui to javafx.fxml;
    opens com.example.socialnetworkgui.models to javafx.fxml;
    opens com.example.socialnetworkgui.validators to javafx.fxml;
    opens com.example.socialnetworkgui.repository to javafx.fxml;
    opens com.example.socialnetworkgui.service to javafx.fxml;
    opens com.example.socialnetworkgui.controller to javafx.fxml;
    exports com.example.socialnetworkgui.models.account;
    opens com.example.socialnetworkgui.models.account to javafx.fxml;
    exports com.example.socialnetworkgui.models.friendship;
    opens com.example.socialnetworkgui.models.friendship to javafx.fxml;
    exports com.example.socialnetworkgui.models.chat;
    opens com.example.socialnetworkgui.models.chat to javafx.fxml;
    exports com.example.socialnetworkgui.repository.account;
    opens com.example.socialnetworkgui.repository.account to javafx.fxml;
    exports com.example.socialnetworkgui.repository.chat;
    opens com.example.socialnetworkgui.repository.chat to javafx.fxml;
    exports com.example.socialnetworkgui.repository.friendship;
    opens com.example.socialnetworkgui.repository.friendship to javafx.fxml;
    exports com.example.socialnetworkgui.service.account;
    opens com.example.socialnetworkgui.service.account to javafx.fxml;
    exports com.example.socialnetworkgui.service.chat;
    opens com.example.socialnetworkgui.service.chat to javafx.fxml;
    exports com.example.socialnetworkgui.service.friendship;
    opens com.example.socialnetworkgui.service.friendship to javafx.fxml;
    exports com.example.socialnetworkgui.validators.account;
    opens com.example.socialnetworkgui.validators.account to javafx.fxml;
    exports com.example.socialnetworkgui.validators.friendship;
    opens com.example.socialnetworkgui.validators.friendship to javafx.fxml;
}
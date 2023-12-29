package com.winery.accessControl;

import com.winery.entities.User;
import javafx.scene.control.Alert;

public class AccessController {

    private final User currentUser;

    public AccessController(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean checkAdminOrOperatorAccess() {
        //   showMessage("UnauthorizedAccess","Insufficient privileges for the operation");
        return currentUser.getRoleName().equals("ADMIN") || currentUser.getRoleName().equals("OPERATOR");

    }
    public void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean checkAdminAccess() {
        return currentUser.getRoleName().equals("ADMIN");
    }
}

package com.winery.accessControl;

import com.winery.entities.User;

public class AccessController {

    private final User currentUser;

    public AccessController(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean checkAdminOrOperatorAccess() {
        return currentUser.getRoleName().equals("ADMIN") || currentUser.getRoleName().equals("OPERATOR");

    }

    public boolean checkAdminAccess() {
        return currentUser.getRoleName().equals("ADMIN");
    }
}

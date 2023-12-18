package com.winery.controllers;

import com.winery.entities.Role;
import com.winery.entities.User;
import com.winery.entities.UserRole;
import com.winery.service.RoleService;
import com.winery.service.UserService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateUserController {

    @FXML
    private TextField newUsername;

    @FXML
    private PasswordField newPassword;

    @FXML
    private ComboBox<String> newRole;

    private RoleService roleService;

    private  UserService userService;

    public CreateUserController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.roleService = RoleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxItems();
    }

    private void setComboBoxItems() {

        List<String> roleNames = Arrays.stream(UserRole.values())
                .map(UserRole::name)
                .collect(Collectors.toList());

        ObservableList<String> roleItems = FXCollections.observableArrayList(roleNames);

        newRole.setItems(roleItems);
    }


    @FXML
    private void createUser() {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String roleName = newRole.getValue();

        if (username.isEmpty() || password.isEmpty() || roleName == null) {
            return;
        }

        Role role = new Role();
        role.setRoleName(UserRole.valueOf(roleName));
        Integer roleId = roleService.findIdByName(role.getRoleName());
        role.setId(roleId);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);

        System.out.println("New User: " + newUser.getUsername() + ", Role ID: " + newUser.getRoleId());
         userService.save(newUser);
    }


}

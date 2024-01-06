package com.winery.controllers;

import com.winery.entities.*;
import com.winery.service.RoleService;
import com.winery.service.UserService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML
    private Label eventMessage;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User,String> roleColumn;

    private final RoleService roleService;

    private final UserService userService;

    public CreateUserController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.roleService = RoleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxItems();

        roleColumn.setCellValueFactory(cellData -> {
            Role role = cellData.getValue().getRole();
            return new SimpleStringProperty(role != null ? String.valueOf(role.getRoleName()) : "");
        });

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        List<User> userList = userService.getAll();
        userTableView.getItems().addAll(userList);

        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
        });
    }


    private void updateFieldsWithSelectedRow(User selectedUser) {
        newUsername.setText(selectedUser.getUsername());
        newPassword.setText(selectedUser.getPassword());
        Role selectedRole = selectedUser.getRole();
        newRole.setValue(String.valueOf(selectedRole.getRoleName()));

    }


    private void clearInputFields() {
        newRole.getSelectionModel().clearSelection();
        newUsername.clear();
        newPassword.clear();
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
            eventMessage.setText("Please fill in fields ");
        }

        boolean nameExists = userTableView.getItems().stream()
                .anyMatch(user -> user.getUsername().equals(username));


        if (nameExists) {
            eventMessage.setText(username+" already exists");
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


        userService.save(newUser);
        userTableView.getItems().add(newUser);
        userTableView.refresh();
        System.out.println("New User: " + newUser.getUsername() + ", Role ID: " + newUser.getRoleId());
        eventMessage.setText("Successfully created user");

    }
    @FXML
    private void editUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            String username = newUsername.getText();
            String password = newPassword.getText();
            String roleName = newRole.getValue();

            if (username.isEmpty() || password.isEmpty() || roleName == null) {
                eventMessage.setText("Please fill in fields ");
            }

            boolean nameExists = userTableView.getItems().stream()
                    .filter(user -> !user.equals(selectedUser))
                    .anyMatch(user -> user.getUsername().equals(username));


            if (nameExists) {
                eventMessage.setText(username+" already exists");
                return;
            }

            Role role = new Role();
            role.setRoleName(UserRole.valueOf(roleName));
            Integer roleId = roleService.findIdByName(role.getRoleName());
            role.setId(roleId);

            userService.update(selectedUser, new String[]{username,password, String.valueOf(role.getId())});
            userTableView.refresh();
            eventMessage.setText("User successfully updated");

        } else {
            eventMessage.setText("Please select a row to update");
        }
    }
    @FXML
    private void deleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try{
              userService.delete(selectedUser);
              userTableView.getItems().remove(selectedUser);
              userTableView.refresh();
              eventMessage.setText("User successfully deleted");
            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the user. It does not exist.");
            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the user.");
            }
        } else {
            eventMessage.setText("Please select a row to delete");
        }
    }
}

package com.winery.service;


import com.winery.entities.Role;
import com.winery.entities.User;
import com.winery.entities.UserRole;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    private static EntityManager entityManager;
    private static UserService userService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        userService = UserService.getInstance(entityManager, Session.getInstance());

    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }


    @Test
    void validateAndGetUser() {
        Role adminRole = new Role();
        adminRole.setRoleName(UserRole.ADMIN);
        adminRole.setId(1);
        User admin =new User(1,"a","123", adminRole);

        userService.save(admin);
        userService.validateAndGetUser("admin","123");
        userService.delete(admin);
    }

    @Test
    void getById() {
        Optional<User> userOptional = userService.getById(1);

        assertTrue(userOptional.isPresent(), "User with ID 1 should be present");

        User user = userOptional.get();
        assertNotNull(user, "User should not be null");

        List<Role> roles = Collections.singletonList(user.getRole());
        assertNotNull(roles, "Roles should not be null");

        assertEquals(1, roles.size(), "Expected 1 role for user with ID 1");
    }


    @Test
    void update() {
        Role hostRole = new Role();
        hostRole.setRoleName(UserRole.HOST);
        hostRole.setId(3);
        User testUser =new User(13,"h","123", hostRole);

        userService.save(testUser);
        testUser.setPassword("123456");
        userService.update(testUser,new String[]{String.valueOf(testUser.getId()),testUser.getUsername(),testUser.getPassword()});
        userService.delete(testUser);
    }

}

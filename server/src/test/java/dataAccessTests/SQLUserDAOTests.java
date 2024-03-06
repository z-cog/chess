package dataAccessTests;

import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLUserDAOTests {

    @Test
    public void clearUserTest() {
        try {
            var user = new SQLUserDAO();
            assertDoesNotThrow(user::clear);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

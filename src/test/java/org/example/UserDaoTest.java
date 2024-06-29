package org.example;
import com.example.database.Dao.UserDAO;
import com.example.models.User;
import com.example.server.MyHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class UserDaoTest {

    @BeforeEach
    public void setup() throws IOException {
        MyHttpServer.startServer();
    }

    @Test
    public void testGetUserByCredentials_ValidCredentials() throws IOException {
        User user = UserDAO.getUserByLogin("i");
        assertNotNull(user, "User should not be null");
        Assertions.assertEquals("b59c67bf196a4758191e42f76670ceba", user.getPassword());
    }

    @Test
    public void testGetUserByCredentials_InvalidCredentials() throws IOException {
        User user = UserDAO.getUserByLogin("a");
        assertNull(user, "User should not be null");
    }
}

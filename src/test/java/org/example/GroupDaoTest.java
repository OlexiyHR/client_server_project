package org.example;

import com.example.database.Dao.GroupDAO;
import com.example.models.Group;
import com.example.server.MyHttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class GroupDaoTest {

    @BeforeEach
    public void setup() throws IOException {
        MyHttpServer.startServer();
    }

    @Test
    public void testCreateGroup_ValidCredentials() throws IOException {
        Group group = new Group("Продовольчі", "Включає їжу та напої");
        assertTrue(GroupDAO.createGroup(group));
        Group check = GroupDAO.getGroup("Продовольчі");
        assertNotNull(check);
        assertEquals("Продовольчі", check.getGroup_name());
        assertEquals("Включає їжу та напої", check.getDescription());
    }

    @Test
    public void testCreateGroup_TheSameCredentials() throws IOException {
        Group group = new Group("Продовольчі", "Їжа");
        assertFalse(GroupDAO.createGroup(group));
        Group check = GroupDAO.getGroup("Продовольчі");
        assertNotNull(check);
        assertEquals("Продовольчі", check.getGroup_name());
        assertNotEquals("Їжа", check.getDescription());
    }

    @Test
    public void testGetGroup_ValidCredentials() throws IOException {
        Group group = GroupDAO.getGroup("Продовольчі");
        assertNotNull(group);
        assertEquals("Продовольчі", group.getGroup_name());
        assertEquals("Включає їжу та напої", group.getDescription());
    }

    @Test
    public void testGetGroup_InvalidCredentials() throws IOException {
        Group group = GroupDAO.getGroup("No");
        assertNull(group);
    }

    @Test
    public void testUpdateGroup_ValidCredentials() throws IOException {
        Group group = new Group("Непродовольчі", "Побутова техніка, електроніка, меблі для дому та офісу, аксесуари");
        assertTrue(GroupDAO.updateGroup(group, "Продовольчі"));
        Group check = GroupDAO.getGroup("Продовольчі");
        assertNull(check);
        Group check1 = GroupDAO.getGroup("Непродовольчі");
        assertNotNull(check1);
        assertEquals("Непродовольчі", check1.getGroup_name());
        assertEquals("Побутова техніка, електроніка, меблі для дому та офісу, аксесуари", check1.getDescription());
    }

    @Test
    public void testUpdateGroup_InvalidCredentials() throws IOException {
        Group group = new Group("А", "А");
        assertFalse(GroupDAO.updateGroup(group, "No"));
        Group check = GroupDAO.getGroup("А");
        assertNull(check);
    }
    @Test
    public void testUpdateGroup_ExistingCredentials() throws IOException {
        Group group = new Group("Продовольчі", "Включає їжу та напої");
        assertTrue(GroupDAO.createGroup(group));
        assertFalse(GroupDAO.updateGroup(group, "Непродовольчі"));
        Group check = GroupDAO.getGroup("Продовольчі");
        assertNotNull(check);
        Group check1 = GroupDAO.getGroup("Непродовольчі");
        assertNotNull(check1);
        assertEquals("Непродовольчі", check1.getGroup_name());
        assertEquals("Побутова техніка, електроніка, меблі для дому та офісу, аксесуари", check1.getDescription());
    }

    @Test
    public void testDeleteGroup_ValidCredentials() throws IOException {
        Group group = GroupDAO.getGroup("Непродовольчі");
        assertNotNull(group);
        assertTrue(GroupDAO.deleteGroup("Непродовольчі"));
        Group group1 = GroupDAO.getGroup("Непродовольчі");
        assertNull(group1);
    }

    @Test
    public void testDeleteGroup_InvalidCredentials() throws IOException {
        Group group = GroupDAO.getGroup("No");
        assertNull(group);
        assertFalse(GroupDAO.deleteGroup("No"));
        Group group1 = GroupDAO.getGroup("No");
        assertNull(group1);
    }

}


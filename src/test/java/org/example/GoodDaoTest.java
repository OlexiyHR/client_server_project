package org.example;

import com.example.database.Dao.GoodDAO;
import com.example.database.Dao.GroupDAO;
import com.example.models.Good;
import com.example.models.Group;
import com.example.server.MyHttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GoodDaoTest {

    @BeforeEach
    public void setup() throws IOException {
        MyHttpServer.startServer();
    }

    @Test
    public void testCreateGood_ValidCredentials() throws IOException {
        Good good = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        assertTrue(GoodDAO.createGood(good));
        Good retrievedGood = GoodDAO.getGood("Гречка");
        // Перевіряємо, що товар не є null
        assertNotNull(retrievedGood, "Отриманий товар не повинен бути null");
        // Перевіряємо, що назва групи товару збігається
        assertEquals("Продовольчі", retrievedGood.getGroup_name(), "Назва групи товару повинна бути 'Продовольчі'");
        // Перевіряємо, що опис товару збігається
        assertEquals("Крупа гречана", retrievedGood.getDescription(), "Опис товару повинен бути 'Крупа гречана'");
        // Перевіряємо, що виробник товару збігається
        assertEquals("Продимекс", retrievedGood.getProducer(), "Виробник товару повинен бути 'Продимекс'");
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount(), "Кількість товару повинна бути 0");
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("20.50"), retrievedGood.getPrice(), "Ціна товару повинна бути '20.50'");
        // Перевіряємо, що назва товару збігається
        assertEquals("Гречка", retrievedGood.getProduct_name(), "Назва товару повинна бути 'Гречка'");
    }

    @Test
    public void testCreateGood_TheSameCredentials() throws IOException {
        Good good = new Good("Гречка", "Крупа гречана", "Продат", 0, new BigDecimal("20.50"), "Продовольчі");
        assertFalse(GoodDAO.createGood(good));
        Good retrievedGood = GoodDAO.getGood("Гречка");
        // Перевіряємо, що товар не є null
        assertNotNull(retrievedGood, "Отриманий товар не повинен бути null");
        // Перевіряємо, що назва групи товару збігається
        assertEquals("Продовольчі", retrievedGood.getGroup_name(), "Назва групи товару повинна бути 'Продовольчі'");
        // Перевіряємо, що опис товару збігається
        assertEquals("Крупа гречана", retrievedGood.getDescription(), "Опис товару повинен бути 'Крупа гречана'");
        // Перевіряємо, що виробник товару збігається
        assertNotEquals("Продат", retrievedGood.getProducer(), "Виробник товару повинен бути 'Продимекс'");
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount(), "Кількість товару повинна бути 0");
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("20.50"), retrievedGood.getPrice(), "Ціна товару повинна бути '20.50'");
        // Перевіряємо, що назва товару збігається
        assertEquals("Гречка", retrievedGood.getProduct_name(), "Назва товару повинна бути 'Гречка'");
    }

    @Test
    public void testCreateGood_InvalidCredentials() throws IOException {
        Good good = new Good("Цукор", "Цукор білий", "Агропром", 50, new BigDecimal("15.00"), "Продукти");
        assertFalse(GoodDAO.createGood(good));
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNull(retrievedGood);
        Group group = GroupDAO.getGroup("Продукти");
        assertNull(group);
    }

    @Test
    public void testGetGood_ValidCredentials() throws IOException {
        Good retrievedGood = GoodDAO.getGood("Гречка");
        assertNotNull(retrievedGood);
        // Перевіряємо, що назва групи товару збігається
        assertEquals("Продовольчі", retrievedGood.getGroup_name(), "Назва групи товару повинна бути 'Продовольчі'");
        // Перевіряємо, що опис товару збігається
        assertEquals("Крупа гречана", retrievedGood.getDescription(), "Опис товару повинен бути 'Крупа гречана'");
        // Перевіряємо, що виробник товару збігається
        assertNotEquals("Продат", retrievedGood.getProducer(), "Виробник товару повинен бути 'Продимекс'");
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount(), "Кількість товару повинна бути 0");
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("20.50"), retrievedGood.getPrice(), "Ціна товару повинна бути '20.50'");
        // Перевіряємо, що назва товару збігається
        assertEquals("Гречка", retrievedGood.getProduct_name(), "Назва товару повинна бути 'Гречка'");
    }

    @Test
    public void testGetGood_InvalidCredentials() throws IOException {
        Good good = GoodDAO.getGood("No");
        assertNull(good);
    }

    @Test
    public void testUpdateGood_ValidCredentials() throws IOException {
        Good good = new Good("Цукор", "Цукор білий", "Агропром", 0, new BigDecimal("15.00"), "Продовольчі");
        assertTrue(GoodDAO.updateGood(good, "Гречка"));
        Good check = GoodDAO.getGood("Гречка");
        assertNull(check);
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNotNull(retrievedGood);
        // Перевіряємо, що назва групи товару збігається
        assertEquals("Продовольчі", retrievedGood.getGroup_name());
        // Перевіряємо, що опис товару збігається
        assertEquals("Цукор білий", retrievedGood.getDescription());
        // Перевіряємо, що виробник товару збігається
        assertEquals("Агропром", retrievedGood.getProducer());
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount());
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("15.00"), retrievedGood.getPrice());
        // Перевіряємо, що назва товару збігається
        assertEquals("Цукор", retrievedGood.getProduct_name());
    }

    @Test
    public void testUpdateGood_InvalidCredentials() throws IOException {
        Good good = new Good("А", "А", "А", 0, new BigDecimal("1.00"), "Продовольчі");
        assertFalse(GoodDAO.updateGood(good, "No"));
        Good check = GoodDAO.getGood("No");
        assertNull(check);
        Good retrievedGood = GoodDAO.getGood("А");
        assertNull(retrievedGood);
    }

    @Test
    public void testUpdateGood_InvalidGroup() throws IOException {
        Good good = new Good("Цукор", "Цукор білий", "Агропром", 0, new BigDecimal("15.00"), "No");
        assertFalse(GoodDAO.updateGood(good, "Цукор"));
        Group group = GroupDAO.getGroup("No");
        assertNull(group);
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNotNull(retrievedGood);
        // Перевіряємо, що назва групи товару збігається
        assertNotEquals("No", retrievedGood.getGroup_name());
        // Перевіряємо, що опис товару збігається
        assertEquals("Цукор білий", retrievedGood.getDescription());
        // Перевіряємо, що виробник товару збігається
        assertEquals("Агропром", retrievedGood.getProducer());
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount());
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("15.00"), retrievedGood.getPrice());
        // Перевіряємо, що назва товару збігається
        assertEquals("Цукор", retrievedGood.getProduct_name());
    }

    @Test
    public void testUpdateGood_ExistingCredentials() throws IOException {
        Good good = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        assertTrue(GoodDAO.createGood(good));
        assertFalse(GoodDAO.updateGood(good, "Цукор"));
        Good check = GoodDAO.getGood("Гречка");
        assertNotNull(check);
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNotNull(retrievedGood);
        // Перевіряємо, що назва групи товару збігається
        assertEquals("Продовольчі", retrievedGood.getGroup_name());
        // Перевіряємо, що опис товару збігається
        assertEquals("Цукор білий", retrievedGood.getDescription());
        // Перевіряємо, що виробник товару збігається
        assertEquals("Агропром", retrievedGood.getProducer());
        // Перевіряємо, що кількість товару збігається
        assertEquals(0, retrievedGood.getAmount());
        // Перевіряємо, що ціна товару збігається
        assertEquals(new BigDecimal("15.00"), retrievedGood.getPrice());
        // Перевіряємо, що назва товару збігається
        assertEquals("Цукор", retrievedGood.getProduct_name());
    }

    @Test
    public void testCredit_ValidCredentials(){
        assertTrue(GoodDAO.credit("Цукор", 100));
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNotNull(retrievedGood);
        assertEquals(100, retrievedGood.getAmount());
    }

    @Test
    public void testCredit_InvalidCredentials() throws IOException {
        Good good = GoodDAO.getGood("No");
        assertNull(good);
        assertFalse(GoodDAO.credit("No", 100));
        Good good1 = GoodDAO.getGood("No");
        assertNull(good1);
    }


    @Test
    public void testWriteOff_ValidCredentials(){
        assertTrue(GoodDAO.writeOff("Цукор", 30));
        Good retrievedGood = GoodDAO.getGood("Цукор");
        assertNotNull(retrievedGood);
        assertEquals(70, retrievedGood.getAmount());
    }

    @Test
    public void testWriteOff_InvalidCredentials() throws IOException {
        Good good = GoodDAO.getGood("No");
        assertNull(good);
        assertFalse(GoodDAO.writeOff("No", 100));
        Good good1 = GoodDAO.getGood("No");
        assertNull(good1);
    }

    @Test
    public void testAllGoods() throws IOException {
        Good good1 = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        Good good2 = new Good("Цукор", "Цукор білий", "Агропром", 70, new BigDecimal("15.00"), "Продовольчі");

        List<Good> goods = GoodDAO.getAllGoods();
        assertNotNull(goods);
        assertTrue(goods.size() >= 2);

        // Перевіряємо наявність кожного з товарів у списку
        boolean foundGood1 = false;
        boolean foundGood2 = false;
        for (Good good : goods) {
            if (good.getProduct_name().equals(good1.getProduct_name())) {
                foundGood1 = true;
            } else if (good.getProduct_name().equals(good2.getProduct_name())) {
                foundGood2 = true;
            }
        }

        // Перевіряємо, що обидва товари знайдено у списку
        assertTrue(foundGood1);
        assertTrue(foundGood2);
    }


    @Test
    public void testAllGoodsFromGroup1_ValidCredentials() throws IOException {
        Good good1 = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        Good good2 = new Good("Цукор", "Цукор білий", "Агропром", 70, new BigDecimal("15.00"), "Продовольчі");

        List<Good> goods = GoodDAO.getAllGoodsFromGroup("Продовольчі");
        assertNotNull(goods);
        assertTrue(goods.size() >= 2);

        // Перевіряємо наявність кожного з товарів у списку
        boolean foundGood1 = false;
        boolean foundGood2 = false;
        for (Good good : goods) {
            if (good.getProduct_name().equals(good1.getProduct_name())) {
                foundGood1 = true;
            } else if (good.getProduct_name().equals(good2.getProduct_name())) {
                foundGood2 = true;
            }
        }

        // Перевіряємо, що обидва товари знайдено у списку
        assertTrue(foundGood1);
        assertTrue(foundGood2);
    }

    @Test
    public void testAllGoodsFromGroup2_ValidCredentials() throws IOException {
        Good good1 = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        Good good2 = new Good("Цукор", "Цукор білий", "Агропром", 70, new BigDecimal("15.00"), "Продовольчі");

        List<Good> goods = GoodDAO.getAllGoodsFromGroup("Неродовольчі");
        assertNotNull(goods);
        assertTrue(goods.size() == 0);

        // Перевіряємо наявність кожного з товарів у списку
        boolean foundGood1 = false;
        boolean foundGood2 = false;
        for (Good good : goods) {
            if (good.getProduct_name().equals(good1.getProduct_name())) {
                foundGood1 = true;
            } else if (good.getProduct_name().equals(good2.getProduct_name())) {
                foundGood2 = true;
            }
        }

        // Перевіряємо, що обидва товари знайдено у списку
        assertFalse(foundGood1);
        assertFalse(foundGood2);
    }

    @Test
    public void testAllGoodsFromGroup_InvalidCredentials() throws IOException {
        Good good1 = new Good("Гречка", "Крупа гречана", "Продимекс", 0, new BigDecimal("20.50"), "Продовольчі");
        Good good2 = new Good("Цукор", "Цукор білий", "Агропром", 70, new BigDecimal("15.00"), "Продовольчі");

        List<Good> goods = GoodDAO.getAllGoodsFromGroup("No");
        assertTrue(goods.size() == 0);

        // Перевіряємо наявність кожного з товарів у списку
        boolean foundGood1 = false;
        boolean foundGood2 = false;
        for (Good good : goods) {
            if (good.getProduct_name().equals(good1.getProduct_name())) {
                foundGood1 = true;
            } else if (good.getProduct_name().equals(good2.getProduct_name())) {
                foundGood2 = true;
            }
        }

        // Перевіряємо, що обидва товари знайдено у списку
        assertFalse(foundGood1);
        assertFalse(foundGood2);
    }

    @Test
    public void testDeleteGood_ValidCredentials() throws IOException {
        Good good = GoodDAO.getGood("Цукор");
        assertNotNull(good);
        assertTrue(GoodDAO.deleteGood("Цукор"));
        Good good1 = GoodDAO.getGood("Цукор");
        assertNull(good1);
    }

    @Test
    public void testDeleteGood_InvalidCredentials() throws IOException {
        Good good = GoodDAO.getGood("No");
        assertNull(good);
        assertFalse(GoodDAO.deleteGood("No"));
        Good good1 = GoodDAO.getGood("No");
        assertNull(good1);
    }

}


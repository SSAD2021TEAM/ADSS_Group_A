package ShipmentsTests;

import BusinessLayer.ShipmentsModule.Facade;
import BusinessLayer.ShipmentsModule.Response;
import DataAccessLayer.dbMaker;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LocationTest {
    private Facade data;

    @Before
    public void setUp() {
        data = new Facade();
        try (Connection con = DriverManager.getConnection(dbMaker.path)) {
            String sqlStatement = "delete from Locations";
            PreparedStatement p = con.prepareStatement(sqlStatement);
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddLocation() {
        int numOfLocations = data.getAllLocations().getValue().size();
        data.addLocation("Haifa, 12", "0509691235", "Yazan");
        data.addLocation("Beersheva, Alexander Yennai 17", "0509991523", "Tariq");
        assertEquals(numOfLocations + 2, data.getAllLocations().getValue().size());
        assertEquals("Beersheva, Alexander Yennai 17", data.getAllLocations().getValue().get(1).getAddress());
        assertEquals("0509991523", data.getAllLocations().getValue().get(1).getPhoneNumber());
        assertEquals("Tariq", data.getAllLocations().getValue().get(1).getContactName());
    }

    @Test
    public void testNotValidFields() {
        Response res1 = data.addLocation("  ", "0509991523", "Tariq");
        Response res2 = data.addLocation("Beersheva, Alexander Yennai 17", null, "Tariq");
        Response res3 = data.addLocation("Beersheva, Alexander Yennai 17", "Yazan", null);
        assertEquals("Couldn't add new location - Invalid parameters", res1.getMsg());
        assertEquals("Couldn't add new location - Invalid parameters", res2.getMsg());
        assertEquals("Couldn't add new location - Invalid parameters", res3.getMsg());
        assertEquals(0, data.getAllLocations().getValue().size());
    }
}
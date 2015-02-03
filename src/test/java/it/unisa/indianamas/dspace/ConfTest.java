package it.unisa.indianamas.dspace;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.junit.Test;

public class ConfTest {

    @Test
    public void test() {
        
            ConfigurationManager.loadConfig("dspace.cfg");
            String prop = ConfigurationManager.getProperty("property.name");
            System.out.println(prop+" lllllllllllllllllllll");
            try {
                Context context = new Context();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

}

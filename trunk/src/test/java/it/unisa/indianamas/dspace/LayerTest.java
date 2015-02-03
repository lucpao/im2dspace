package it.unisa.indianamas.dspace;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.unisa.indianamas.dspace.IM2DSpaceLayer;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class LayerTest {
    ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
   
   Logger logger = Logger.getLogger("edu.unisa.indianamas.ontology.LayerTest");
   
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void layerTest() throws Exception {
        logger.info("Starting Layer Test");
        IM2DSpaceLayer layerDSpace = (IM2DSpaceLayer)context.getBean("layerBean");
        assertTrue(layerDSpace.getUser().equals("user"));
        assertTrue(layerDSpace.getPwd().equals("password"));
        assertTrue(layerDSpace.getUri().equals("localhost"));
        assertNotNull(layerDSpace.getLayer());
        
        logger.info("Layer Test Successfully terminated");

    }

  
    
}


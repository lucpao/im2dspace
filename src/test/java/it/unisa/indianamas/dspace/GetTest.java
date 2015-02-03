package it.unisa.indianamas.dspace;


import it.unige.indianamas.ontology.DO;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetTest {

   ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    
   IM2DSpaceLayer layerDSpace; 
   Logger logger = Logger.getLogger("edu.unisa.indianamas.ontology.GetTest");
   
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        layerDSpace = (IM2DSpaceLayer)context.getBean("layerBean");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTest() throws JAXBException  {
        logger.info("Starting GET Test");
        ArrayList<DO> list = layerDSpace.getAll();
        logger.info("LIST SIZE : "+list.size());
        logger.info("GET Test terminated");
    }

  
    
}


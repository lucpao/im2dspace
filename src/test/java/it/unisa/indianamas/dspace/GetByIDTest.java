package it.unisa.indianamas.dspace;

import static org.junit.Assert.*;
import it.unige.indianamas.ontology.Attached_File;
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

public class GetByIDTest {

    ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    
    IM2DSpaceLayer layerDSpace; 
    Logger logger = Logger.getLogger("edu.unisa.indianamas.dspace.GetByIDTest");
   
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
    public void test() throws JAXBException {
        logger.info("Starting GET By ID Test");
        DO doObj = layerDSpace.getById(564);
        System.out.println("ID "+doObj.getObject_ID());
        String name = ((Attached_File)doObj.getHas_attached_file().get(0)).getFile_name();
        String internalId = ((Attached_File)doObj.getHas_attached_file().get(0)).getFile_path();
        System.out.println(name);
        System.out.println(internalId);
        logger.info("GET Test terminated");
    }

}

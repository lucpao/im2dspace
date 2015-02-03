package it.unisa.indianamas.dspace;


import static org.junit.Assert.assertTrue;
import it.unige.indianamas.ontology.DO;
import it.unige.indianamas.ontology.GPS;
import it.unige.indianamas.ontology.Person;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Classification;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Interpretation;
import it.unige.indianamas.ontology.impl.DefaultGod_of_Storms_Fertilising_the_Earth;
import it.unige.indianamas.ontology.impl.DefaultHarness;
import it.unige.indianamas.ontology.impl.DefaultPastoral_Scene;
import it.unisa.dspace.entities.response.items.Item;
import it.unisa.dspace.entities.response.items.Metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FromTest {
   private final static String description = "pippi, pluto e paperino";
   private final static String creator = "pippi pluto";
   private final static String uri = "http://indianamas.it";
   private final static String format = "formato dati";
   Logger logger = Logger.getLogger("edu.unisa.indianamas.ontology.FromTest");
   
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
    public void fromDSpaceTest() throws Exception {
        logger.info("Starting fromDSpace Test");
        Item item = new Item();
        List<Metadata> metadata = new ArrayList<Metadata>();
        metadata.add(new Metadata("dc","description", null, description));
        metadata.add(new Metadata("indianamas","interpretation", null, "interpretation:DefaultPastoral_Scene;confidence:40;source:human"));
        metadata.add(new Metadata("indianamas","interpretation", null, "interpretation:DefaultGod_of_Storms_Fertilising_the_Earth;confidence:60;source:MAS"));
        metadata.add(new Metadata("indianamas","classification", null, "carving:DefaultHarness;confidence:60;source:MAS"));
        metadata.add(new Metadata("dcterms","point",null,"east=1.0; north=2; elevation=3; units=4;zunits=5; projection=6; name=7"));
        metadata.add(new Metadata("dc","contributor","other",creator));
        metadata.add(new Metadata("dc","description","uri",uri));
        metadata.add(new Metadata("dc","format",null,format));
        item.setMetadataList(metadata);
        IM2DSpaceConverter converter = new IM2DSpaceConverter();
        DO doObj = converter.fromDSpace(item);
        
        assertTrue(description.equals(doObj.getFree_text_description()));
        Iterator<DefaultCalculated_Interpretation> iteratorI =doObj.getAllHas_interpretations();
        DefaultCalculated_Interpretation interp1 = iteratorI.next();
        assertTrue(interp1.getIdentified_interpretation() instanceof DefaultPastoral_Scene);
        DefaultCalculated_Interpretation interp2 = iteratorI.next();
        assertTrue(interp2.getIdentified_interpretation() instanceof DefaultGod_of_Storms_Fertilising_the_Earth );
        Iterator<DefaultCalculated_Classification> iteratorC =doObj.getAllHas_classifications();
        DefaultCalculated_Classification interp3 = iteratorC.next();
        assertTrue(interp3.getIdentified_classification() instanceof DefaultHarness);
        GPS gps = doObj.getHas_GPS();
        assertTrue(gps.getEast()==1f);
        assertTrue(gps.getNorth()==2f);
        assertTrue(gps.getElevation()==3f);
        assertTrue(gps.getGps_name().equals("7"));
        Person creator = doObj.getHas_creator();
        assertTrue(creator.getName_and_surname().equals(this.creator));
        assertTrue(doObj.getUri().equals(uri));
    //    assertTrue(doObj.getHas_format().equals(format));
        logger.info("fromDSpace Test Successfully terminated");

    }

  
    
}


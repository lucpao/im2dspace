package it.unisa.indianamas.dspace;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.unisa.indianamas.dspace.IM2DSpaceLayer;
import it.unisa.indianamas.dspace.enumerate.Type;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.unige.indianamas.ontology.Bicknell_Tracing;
import it.unige.indianamas.ontology.Calculated_Classification;
import it.unige.indianamas.ontology.DO;
import it.unige.indianamas.ontology.DO_with_Confidence;
import it.unige.indianamas.ontology.Domain_Specific_Image;
import it.unige.indianamas.ontology.GPS;
import it.unige.indianamas.ontology.Image;
import it.unige.indianamas.ontology.Person;
import it.unige.indianamas.ontology.Text;
import it.unige.indianamas.ontology.User;
import it.unige.indianamas.ontology.impl.DefaultBicknell_Tracing;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Classification;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Interpretation;
import it.unige.indianamas.ontology.impl.DefaultDO;
import it.unige.indianamas.ontology.impl.DefaultDO_with_Confidence;
import it.unige.indianamas.ontology.impl.DefaultDomain_Specific_Image;
import it.unige.indianamas.ontology.impl.DefaultGPS;
import it.unige.indianamas.ontology.impl.DefaultImage;
import it.unige.indianamas.ontology.impl.DefaultMonteBeigua;
import it.unige.indianamas.ontology.impl.DefaultPastoral_Scene;
import it.unige.indianamas.ontology.impl.DefaultPerson;
import it.unige.indianamas.ontology.impl.DefaultSinuous_Line;
import it.unige.indianamas.ontology.impl.DefaultText;
import it.unige.indianamas.ontology.impl.DefaultUser;
import it.unige.indianamas.ontology.impl.DefaultVelina;

public class InsertTest {
    ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    
    Logger logger = Logger.getLogger("it.unisa.indianamas.ontology.InsertTest");

    IM2DSpaceLayer layerDSpace; 
    
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
    

    private void fillDO(DO digObj) {
        digObj.setFree_text_description("description");
        Person creator = new DefaultPerson();
        creator.setName_and_surname("Luca");
        digObj.setHas_creator(creator);
        digObj.setLast_mod_date("last mod date");
        digObj.setPubblication_date("Publication date");
         
        /*****
         * TEST INSERT SYNTACTICALLY RELATED
         */
        DO_with_Confidence dowc1 = new DefaultDO_with_Confidence();
        dowc1.setConfidence(50);
        dowc1.setSource_rel_DO("related to");
        DefaultDO doRw1 = new DefaultDO();
        doRw1.setObject_ID(3);
        dowc1.setRelated_DO(doRw1);
        
        DO_with_Confidence dowc2 = new DefaultDO_with_Confidence();
        dowc2.setConfidence(50);
        dowc2.setSource_rel_DO("related to");
        DefaultDO doRw2 = new DefaultDO();
        doRw2.setObject_ID(4);
        dowc2.setRelated_DO(doRw2);
        
        List listSiRW = (List) new ArrayList();
        listSiRW.add(dowc1);
        listSiRW.add(dowc2);
        digObj.setSyntactically_related_to(listSiRW);
        
        /*****
         * TEST INSERT IS PART OF
         */
        
        
        digObj.setIs_part_of(doRw1);
        
        /***
         * TEST INSERT  SEMANTICALLY RELATED
         */
        DO_with_Confidence dowc3 = new DefaultDO_with_Confidence();
        dowc3.setConfidence(50);
        dowc3.setSource_rel_DO("related to");
        DefaultDO doRw3 = new DefaultDO();
        doRw3.setObject_ID(4);
        dowc3.setRelated_DO(doRw3);
        List listSeRW = (List) new ArrayList();
        listSeRW.add(dowc3);
        digObj.setSemantically_related_to(listSeRW);
        /***
         * TEST INSERT INTERPRETATION
         */
        DefaultPastoral_Scene dps = new DefaultPastoral_Scene();
        DefaultCalculated_Interpretation dci = new DefaultCalculated_Interpretation();
        dci.setIdentified_interpretation(dps);
        dci.setConfidence(50);
        dci.setSource("source");
        List list = (List) new ArrayList();
        list.add(dci);
        digObj.setHas_interpretations(list);
        
        
        /**
         * TEST INSERT URI
         */
        digObj.setUri("URI");
        
        /***
         * TEST INSERT GPS 
         */
        GPS gps = new DefaultGPS();
        gps.setEast(20);
        gps.setNorth(40);
        gps.setElevation(60);
        gps.setProjection("pippo");
        gps.setUnit("pluto");
        gps.setZunit(10);
        gps.setGps_name("pippo");
 //       digObj.setHas_GPS(gps);
        
    }
    
    private void fillText(DefaultText text) {
        text.addKeywords("keyword1");
        text.addKeywords("keywords2");
        
        Person author1 = new DefaultPerson();
        author1.setName_and_surname("pipp pluto");
        text.addAuthors(author1);
        Person author2 = new DefaultPerson();
        author2.setName_and_surname("paperino topolino");
        text.addAuthors(author2);
        
        text.addTitle("title1");
        text.addTitle("title2");
        
        text.addText_abstract("abstract1");
        text.addText_abstract("abstract2");
        
        text.setType(Type.JOURNAL.getValue());
      }
    
//    @Test
//    public void insertDOTest() throws Exception {
//        DefaultDO digObj =  new DefaultDO();
//        fillDO(digObj);
//        layerDSpace.insert("pippo", digObj, null);
//   }
//    
//    @Test
//    public void insertTextTest() throws Exception {
//        DefaultText digObj =  new DefaultText();
//        fillDO(digObj);
//        fillText(digObj);
//        layerDSpace.insert("pippo", digObj, null);
//   }
//    
//    @Test
//    public void insertDefaultImageTest() throws Exception {
//        DefaultImage digObj =  new DefaultImage();
//        
//        fillDO(digObj);
//        layerDSpace.insert("pippo", digObj, null);
//   }
//
//
//    @Test
//    public void insertBicknellTracingImageTest() throws Exception {
//        DefaultBicknell_Tracing digObj = new DefaultBicknell_Tracing();
//        fillDO(digObj);
//        fillBicknell(digObj);
//        layerDSpace.insert("pippo", digObj, null);
//    }
//
//    private void fillBicknell(DefaultBicknell_Tracing digObj) {
//        digObj.setPrimary_roll_sheet(2);
//        digObj.setSecondary_roll_sheet(3);
//        digObj.setNote("note");
//        digObj.setSheet_dimension("dimension");
//        digObj.setHDL("HDL");
//        digObj.setCluster("cluster");
//        
//    }
//    
//    @Test
//    public void insertDaniela() throws Exception {
//        Image  doObject2 = new DefaultImage();
//        doObject2.setFree_text_description("prova da MAS 1_26666");
//        doObject2.addTitle("MAS 1_2666");
//        doObject2.addKeywords("MAS666");
//        doObject2.addKeywords("image");
//        
//        java.util.ArrayList<String> mfiles= new java.util.ArrayList<String>();
//        //layer.insert("2", doObject, mfiles);
//        layerDSpace.insert("2", doObject2, mfiles);
//        Text  doObject = new DefaultText();
//        doObject.setFree_text_description("prova da MAS 1_16666");
//        doObject.addTitle("MAS 1_1666");
//        doObject.addKeywords("MAS6666");
//        doObject.addKeywords("text");
//   }

//    @Test
//    public void insertDaniela2() throws Exception {
////    Text  doObject = new DefaultText();
////    doObject.setFree_text_description("prova da salerno TESTO9");
////    doObject.addTitle("MAS salerno TESTO9");
////    doObject.addKeywords("MAS salerno TESTO9");
////    doObject.addKeywords("text");
//    Image  doObject2 = new DefaultImage();
//    doObject2.setFree_text_description("prova da MAS salerno immagine 14");
//    doObject2.addTitle("MAS salerno immagine 15");
//    doObject2.addKeywords("MAS salerno immagine 154");
//    doObject2.addKeywords("image");
//    doObject2.addHas_ages("age1");
//    java.util.ArrayList<String> mfiles= new java.util.ArrayList<String>();
//    java.util.ArrayList<String> mfiles2= new java.util.ArrayList<String>();
//    mfiles2.add("C:\\Users\\Luca\\Desktop\\images.jpg");
////    layerDSpace.insert("2", doObject, mfiles);
//    layerDSpace.insert("2", doObject2, mfiles2);
//    }
//    
//    
//    @Test
//    public void insertDaniela3() throws Exception {
//       // Domain_Specific_Image  doObject2 = new DefaultDomain_Specific_Image();
//        DefaultBicknell_Tracing doObject2 = new DefaultBicknell_Tracing();
//    doObject2.setFree_text_description("prova da MAS ge immagine 6");
//    doObject2.addTitle("prova da MAS ge immagine 6");
//    doObject2.addKeywords("ge");
//    doObject2.addKeywords("image");
//    doObject2.addHas_ages("age1");
//    
//    Calculated_Classification it= new DefaultCalculated_Classification();
//     it.setConfidence(95);
//     it.setSource("IR");
//     it.setIdentified_classification(new DefaultSinuous_Line());
//      
//     DefaultCalculated_Interpretation it2= new DefaultCalculated_Interpretation();
//     it2.setConfidence(95);
//     it2.setSource("IR");
//     it2.setIdentified_interpretation(new DefaultPastoral_Scene());
//    
//     doObject2.addHas_classifications(it);
//     doObject2.addHas_interpretations(it2);
//     
//     doObject2.addHas_regions(new DefaultMonteBeigua());
//     doObject2.setHas_coordinates("1245");
//     User p = new DefaultUser();
//     p.setName_and_surname("Daniela Briola ****");
//     doObject2.setHas_creator(p);
//     doObject2.setHas_dimensions("10;10");
//     
//     GPS gps= new DefaultGPS();
//     gps.setEast(2); gps.setElevation(9);
//     doObject2.setHas_GPS(gps);
//     doObject2.setHas_owner(p);
//     doObject2.setId_rectangle(4);
//     doObject2.setIs_scene(false);
//     DefaultVelina velina = new DefaultVelina();
//     doObject2.setSupport(velina);
//     doObject2.setPrimary_roll_sheet(1);
//     doObject2.setSecondary_roll_sheet(2);
//     doObject2.setNote("Note");
//     doObject2.setSheet_dimension("4x4");
//     doObject2.setHDL("HDL");
//     doObject2.setCluster("Cluster");
//     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//     Calendar cal = Calendar.getInstance();
//     
//     
//     
//    // doObject2.setScene_composition("Many petro");
//     doObject2.setLast_mod_date(dateFormat.format(cal.getTime()));
//     doObject2.setPubblication_date(dateFormat.format(cal.getTime()));
//   java.util.ArrayList<String> mfiles= new java.util.ArrayList<String>();
//   java.util.ArrayList<String> mfiles2= new java.util.ArrayList<String>();
//   mfiles2.add("C:\\Users\\Luca\\Desktop\\images.jpg");
//     layerDSpace.insert("2", doObject2, mfiles2);
//}
    
    @Test
    public void insertDaniela4() throws Exception {
    Bicknell_Tracing doObject2 = new DefaultBicknell_Tracing();
    doObject2.setFree_text_description("prova da MAS ge immagine 9");
    doObject2.addTitle("prova da MAS ge immagine 9");
    doObject2.addKeywords("ge");
    doObject2.addKeywords("image");
    doObject2.addHas_ages("age1");
//    DefaultVelina velina = new DefaultVelina();
//  doObject2.setSupport(velina);
    Calculated_Classification it= new DefaultCalculated_Classification();
     it.setConfidence(95);
     it.setSource("IR");
     it.setIdentified_classification(new DefaultSinuous_Line());
     doObject2.setScene_composition("Many petro");
     DefaultCalculated_Interpretation it2= new DefaultCalculated_Interpretation();
     it2.setConfidence(95);
     it2.setSource("IR");
     it2.setIdentified_interpretation(new DefaultPastoral_Scene());
    
     doObject2.addHas_classifications(it);
     doObject2.addHas_interpretations(it2);
     
     doObject2.addHas_regions(new DefaultMonteBeigua());
     doObject2.setHas_coordinates("12;45");
     User p = new DefaultUser();
     p.setName_and_surname("Daniela Briola");
     doObject2.setHas_creator(p);
     doObject2.setHas_dimensions("10;10");
     
     GPS gps= new DefaultGPS();
     gps.setEast(2); gps.setElevation(9);
     doObject2.setHas_GPS(gps);
     doObject2.setHas_owner(p);
     doObject2.setId_rectangle(4);
     doObject2.setIs_scene(false);
     
     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
     Calendar cal = Calendar.getInstance();
     
     doObject2.setLast_mod_date(dateFormat.format(cal.getTime()));
     doObject2.setPubblication_date(dateFormat.format(cal.getTime()));
    
     // solo per bicknell, la parte sopra era generica per ogni immagine
     /*
     doObject2.setSupport(new DefaultVelina());
     doObject2.setHDL("HDL1234");
     doObject2.setPrimary_roll_sheet(11);
     doObject2.setSecondary_roll_sheet(1);
     doObject2.setNote("valcamnica rocks");
     doObject2.setSheet_dimension("130 x 12");
     doObject2.setCluster("rotolo 23");
     doObject2.setScene_composition("Many petro");
     */
     //
    // OCCHIO, NON USARE LO STESSO VETTORE PERCHe' ESSEND PER RIFERIMENTO, NELLA INSERT CI VIENE INSERITO PACKAGE.XML,
    // QUINDI SE LO RIUSCI DOPO DA' ERRORE PERCHe' LO REINSERISCE E SE LO TROVA GIA' DEDNTRO... USARE UN NUOVO VETTORE PER OGNI NUOVO do
    java.util.ArrayList<String> mfiles= new java.util.ArrayList<String>();
    java.util.ArrayList<String> mfiles2= new java.util.ArrayList<String>();

    mfiles2.add("src/test/resources/download.jpg");
    //layer.insert("2", doObject, mfiles);
    int itemId = layerDSpace.insert("5", doObject2, mfiles2);
    System.out.println("item id "+itemId);
    }
}


package it.unisa.indianamas.dspace;

import it.unisa.dspace.entities.request.items.Bundle;
import it.unisa.dspace.entities.request.items.Bundles;
import it.unisa.dspace.entities.request.items.Field;
import it.unisa.dspace.entities.request.items.InsertItemRequest;
import it.unisa.dspace.entities.response.items.Item;
import it.unisa.dspace.entities.response.items.Metadata;
import it.unisa.indianamas.dspace.enumerate.ImageType;
import it.unisa.indianamas.dspace.enumerate.MetadataTag;
import it.unisa.indianamas.dspace.enumerate.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;











import org.apache.commons.collections.MultiHashMap;
import org.apache.log4j.Logger;

import it.unige.indianamas.ontology.BW_Image;
import it.unige.indianamas.ontology.Bicknell_Support;
import it.unige.indianamas.ontology.Bicknell_Tracing;
import it.unige.indianamas.ontology.Calculated_Classification_Or_Interpretation;
import it.unige.indianamas.ontology.Carving_Picture;
import it.unige.indianamas.ontology.Classification;
import it.unige.indianamas.ontology.DO;
import it.unige.indianamas.ontology.Domain_Specific_Image;
import it.unige.indianamas.ontology.Image;
import it.unige.indianamas.ontology.Interpretation;
import it.unige.indianamas.ontology.Person;
import it.unige.indianamas.ontology.Region;
import it.unige.indianamas.ontology.Sketch;
import it.unige.indianamas.ontology.Text;
import it.unige.indianamas.ontology.User;
import it.unige.indianamas.ontology.impl.DefaultBW_Image;
import it.unige.indianamas.ontology.impl.DefaultBicknell_Support;
import it.unige.indianamas.ontology.impl.DefaultBicknell_Tracing;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Classification;
import it.unige.indianamas.ontology.impl.DefaultCalculated_Interpretation;
import it.unige.indianamas.ontology.impl.DefaultCarving_Picture;
import it.unige.indianamas.ontology.impl.DefaultDO;
import it.unige.indianamas.ontology.impl.DefaultDO_with_Confidence;
import it.unige.indianamas.ontology.impl.DefaultDomain_Specific_Image;
import it.unige.indianamas.ontology.impl.DefaultGPS;
import it.unige.indianamas.ontology.impl.DefaultGeneric_Image;
import it.unige.indianamas.ontology.impl.DefaultImage;
import it.unige.indianamas.ontology.impl.DefaultPerson;
import it.unige.indianamas.ontology.impl.DefaultSketch;
import it.unige.indianamas.ontology.impl.DefaultText;
import it.unige.indianamas.ontology.impl.DefaultUser;

public class IM2DSpaceConverter {
     private List<Field> metadata = new ArrayList<Field>();
     private MultiHashMap hashMap = new MultiHashMap();
     private InsertItemRequest item = new InsertItemRequest();
     
     private final static String packagename = "it.unige.indianamas.ontology.impl.";
    
     private Logger logger = Logger.getLogger("it.unisa.indianamas.dspace.IM2DSpaceConverter");

     public DO fromDSpace(Item item) {
         logger.debug("------------------------------------------------");
         logger.debug("Starting Item conversion. Item Id :"+item.getId());
         int id = Integer.parseInt(item.getId());
         List<Metadata> metadata = item.getMetadata();
         hashMap= new MultiHashMap();
         fillMultiHashMap(item);
         String imageType = "";
         List<String> listImageType = (List<String>)hashMap.get(MetadataTag.IMAGETYPE.getValue());
         if (listImageType!=null &&  listImageType.size()>0) { 
             imageType = listImageType.get(0);
         }
         String documentType = "";
         List<String> listDocumentType = (List<String>)hashMap.get(MetadataTag.TYPE.getValue());
         if (listDocumentType!=null &&  listDocumentType.size()>0) { 
             documentType = listDocumentType.get(0);
         }
         if (imageType.equals(ImageType.GENERICIMAGE.getValue())){
             
             logger.debug("Generic Image recognized");
             DefaultGeneric_Image output = new DefaultGeneric_Image();
             output.setObject_ID(id);
             fillDOFields(output);
             fillImageFields(output);
             return output;
         }
         if (imageType.equals(ImageType.SKETCH.getValue())){
             logger.debug("Sketch Image recognized");
             DefaultSketch output = new DefaultSketch();
             output.setObject_ID(id);
             fillDOFields(output);
             fillImageFields(output);
             fillSceneCompositionField(output);
             return output;
         }

         if (imageType.equals(ImageType.BWIMAGE.getValue())){
             logger.debug("BW Image recognized");
             DefaultBW_Image output = new DefaultBW_Image();
             output.setObject_ID(id);
             fillDOFields(output);
             fillImageFields(output);
             fillSceneCompositionField(output);
             return output;
         }
         if (imageType.equals(ImageType.BICKNELLTRACING.getValue())){
             logger.debug("Bicknell Tracing recognized");
             DefaultBicknell_Tracing output = new DefaultBicknell_Tracing();
             output.setObject_ID(id);
             fillDOFields(output);
             fillImageFields(output);
             fillSceneCompositionField(output);
             fillBicknellFields(output);
             return output;
         }
         if (imageType.equals(ImageType.CARVING_PICTURE.getValue())){
             logger.debug("Carving Picture recognized");
             DefaultCarving_Picture output = new DefaultCarving_Picture();
             output.setObject_ID(id);
             fillDOFields(output);
             fillImageFields(output);
             fillSceneCompositionField(output);
             return output;
         }
         
         if (documentType.equals(Type.JOURNAL.getValue())){
             logger.debug("Journal text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.JOURNAL.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         if (documentType.equals(Type.CONFERENCE.getValue())){
             logger.debug("Conference text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.CONFERENCE.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         if (documentType.equals(Type.BOOK.getValue())){
             logger.debug("Book text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.BOOK.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         if (documentType.equals(Type.CHAPTER.getValue())){
             logger.debug("Chapter text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.CHAPTER.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         if (documentType.equals(Type.THESIS.getValue())){
             logger.debug("Thesis text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.THESIS.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         if (documentType.equals(Type.UNPUBLISHED.getValue())){
             logger.debug("Unpublished text recognized");
             DefaultText output = new DefaultText();
             output.setObject_ID(id);
             output.setType(Type.UNPUBLISHED.getValue());
             fillDOFields(output);
             fillTextFields(output);
             return output;
         }
         /**
          * this is the case when no specific Digital Object are specified
          */
         logger.debug("Simple Default Object recognized");
         DefaultDO output = new DefaultDO();
         output.setObject_ID(id);
         fillDOFields(output);
         return output;

     }

     
     private void fillImageFields(Image output){
         logger.debug("Filling Image fields");
         /***
          * INSERT OF ID_RECTANGLE METADATA INTO IMAGE 
          */
         List<String> idrectangles = (List<String>)hashMap.get(MetadataTag.ID_RECTANGLE.getValue());

         if (idrectangles!=null &&  idrectangles.size()>0) {
             logger.debug("id rectangle found : "+idrectangles.get(0));   
             output.setId_rectangle(Integer.parseInt(idrectangles.get(0)));
         }

         /***
          * INSERT OF HAS_COORDINATE METADATA INTO IMAGE 
          */
         List<String> listHasCoordinates = (List<String>)hashMap.get(MetadataTag.HAS_COORDINATES.getValue());

         if (listHasCoordinates!=null &&  listHasCoordinates.size()>0) {
             logger.debug("coordinates found : "+listHasCoordinates.get(0));   
             output.setHas_coordinates(listHasCoordinates.get(0));
         }

         /***
          * INSERT OF HAS DIMENSION TO METADATA INTO IMAGE 
          */
         List<String> listHasDimension = (List<String>)hashMap.get(MetadataTag.HAS_DIMENSION.getValue());

         if (listHasDimension!=null &&  listHasDimension.size()>0) {
             logger.debug("Dimension list size : "+listHasDimension.size());
             for(String dimension:listHasDimension) {
                output.setHas_dimensions(dimension);
             }
         }
             logger.debug("Image fields completed");
     }
     
     public InsertItemRequest toDSpace(DO doObj, int collectionId) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException {
         logger.debug("Digital Object class : "+doObj.getClass());
         
         metadata = new ArrayList<Field>();
         item = new InsertItemRequest();
         Class[] paramClasses = new Class[1]; 
         paramClasses[0] = doObj.getClass();
         Method method = this.getClass().getDeclaredMethod("toDSpace", paramClasses);
         InsertItemRequest item = (InsertItemRequest) method.invoke(this, doObj);
    
         item.setCollectionId(collectionId);
         return item;
     }

     
     private InsertItemRequest toDSpace(DefaultDO digitalObject) {
         parseDO(digitalObject);
         
         item.setMetadata(metadata);
         List<Bundle> bundles = new ArrayList<Bundle>();
         item.setBundles(bundles);
         for (Field field:metadata) {
             logger.debug("Field : "+field.getName()+", Value : "+field.getValue());
         }
         return item;
     }

     private InsertItemRequest toDSpace(Sketch digitalImage) {

         parse(digitalImage);
         item.setMetadata(metadata);
         return item;
     }

     private InsertItemRequest toDSpace(Bicknell_Tracing digitalImage) {

         parse(digitalImage);
         item.setMetadata(metadata);
         return item;
     }

     private InsertItemRequest toDSpace(BW_Image digitalImage) {

         parse(digitalImage);
         item.setMetadata(metadata);
         return item;
     }

     private InsertItemRequest toDSpace(Carving_Picture digitalImage) {
         parse(digitalImage);
         item.setMetadata(metadata);
         return item;
     }

     private InsertItemRequest toDSpace(DefaultImage digitalImage) {
   //      metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.GENERICIMAGE.getValue()));
         parse(digitalImage);
         item.setMetadata(metadata);
         return item;
     }
     
     private InsertItemRequest toDSpace(DefaultBicknell_Tracing digitalImage) {
 
         parse(digitalImage);
         item.setMetadata(metadata);
         for (Field field:metadata) {
             logger.debug("Field : "+field.getName()+", Value : "+field.getValue());
         }
         return item;
     }

     private InsertItemRequest toDSpace(DefaultText digitalText) {

         parse(digitalText);
         item.setMetadata(metadata);
         for (Field field:metadata) {
             logger.debug("Field : "+field.getName()+", Value : "+field.getValue());
         }
         return item;
     }



     private void fillTextFields(Text text) {
         logger.debug("Filling Text fields");
         /***
          * INSERT OF AUTHOR METADATA INTO TEXT 
          */
         List<String> listAuthors = (List<String>)hashMap.get(MetadataTag.AUTHORS.getValue());
         if (listAuthors!=null &&  listAuthors.size()>0) {
             for(String author:listAuthors) {
                 Person person= new DefaultPerson();
                 person.setName_and_surname(author);
                 text.addAuthors(person);
             }
         }




         /***
          * INSERT OF LANGUAGE METADATA INTO TEXT 
          */
         List<String> listLanguage = (List<String>)hashMap.get(MetadataTag.LANGUAGE.getValue());
         if (listLanguage!=null &&  listLanguage.size()>0)
             for(String language:listLanguage) {
                 text.addLanguage(language);
             }

         /***
          * INSERT OF ABSTRACT METADATA INTO TEXT 
          */
         List<String> listAbstract = (List<String>)hashMap.get(MetadataTag.ABSTRACT.getValue());
         if (listAbstract!=null &&  listAbstract.size()>0)
             for(String abstracts:listAbstract) {
                 text.addLanguage(abstracts);
             }

         /***
          * INSERT OF SUMMARY METADATA INTO TEXT 
          */
         List<String> listSummary = (List<String>)hashMap.get(MetadataTag.SUMMARY.getValue());
         if (listSummary!=null &&  listSummary.size()>0)
             for(String summary:listSummary) {
                 text.addSummary(summary);
             }


     }

     private void fillDOFields(DO doObj)    {
         logger.debug("Filling DO fields");
         /***
          * INSERT OF DESCRIPTION METADATA INTO DO 
          */
         List<String> list = (List<String>)hashMap.get(MetadataTag.DESCRIPTION.getValue());
         if (list!=null &&  list.size()>0) {
             logger.debug("Description found : "+list.get(0));
             doObj.setFree_text_description(list.get(0));
         }



         try {
             /***
              * INSERT OF INTERPRETATION METADATA INTO DO 
              */
             List<String> metadataInterpretations = (List<String>) hashMap.get(MetadataTag.INTERPRETATION.getValue());

             if(metadataInterpretations!=null && metadataInterpretations.size()>0) {
                 logger.debug("Interpretation list size : "+metadataInterpretations.size());
                 jade.util.leap.List interpretations = toDefaultInterpreations(metadataInterpretations);
                 doObj.setHas_interpretations(interpretations);
             }

             /***
              * INSERT OF CLASSIFICATION METADATA INTO DO 
              */
             List<String> metadataClassifications = (List<String>) hashMap.get(MetadataTag.CLASSIFICATION.getValue());
             if(metadataClassifications!=null && metadataClassifications.size()>0) {
                 logger.debug("Classification list size : "+metadataClassifications.size());
                 jade.util.leap.List classifications = toDefaultClassifications(metadataClassifications);
                 doObj.setHas_classifications(classifications);
             }
             logger.debug("DO fields completed");
         }
         catch(ClassNotFoundException e) {
             logger.info("The metadata contins a class name that cannot be found");
         }
         catch(InstantiationException e) {
             logger.info("The metadata contins a class name that cannot be instantied");
         }
         catch(IllegalAccessException e) {
             logger.info("IllegalAccessException");
         }
         
         
         
         /***
          * INSERT OF ISSUED METADATA INTO DO 
          */
         List<String> listDateIssued = (List<String>)hashMap.get(MetadataTag.ISSUED.getValue());
         if (listDateIssued!=null &&  listDateIssued.size()>0) {
             logger.debug("Date Issued found : "+listDateIssued.get(0));
             doObj.setIssue_date(listDateIssued.get(0));
         }

         /***
          * INSERT OF GPS METADATA INTO DO 
          */
         List<String> listGPS = (List<String>)hashMap.get(MetadataTag.GPS.getValue());
         if (listGPS!=null &&  listGPS.size()>0) {
             logger.debug("GPS found : "+listGPS.get(0));
             doObj.setHas_GPS(MetadataToGPS(listGPS.get(0)));
         }

         /***
          * INSERT OF CREATOR METADATA INTO DO 
          */
         List<String> listCreator = (List<String>)hashMap.get(MetadataTag.CREATOR.getValue());
         if (listCreator!=null &&  listCreator.size()>0) {
             logger.debug("Creator found : "+listCreator.get(0));
             Person person= new DefaultPerson();
             person.setName_and_surname(listCreator.get(0));
             doObj.setHas_creator(person);
         }

         /***
          * INSERT OF URI METADATA INTO DO 
          */
         List<String> listURI = (List<String>)hashMap.get(MetadataTag.URI.getValue());
         if (listURI!=null &&  listURI.size()>0) {
             logger.debug("URI found : "+listURI.get(0));
             doObj.setUri(listURI.get(0));
         }

         /***
          * INSERT OF FORMAT METADATA INTO DO 
          
         List<String> listFormat = (List<String>)hashMap.get(MetadataTag.FORMAT.getValue());
         if (listFormat!=null &&  listFormat.size()>0) {
             logger.debug("Format found : "+listFormat.get(0));
             doObj.setHas_format(listFormat.get(0));
         }*/

         /***
          * INSERT OF OWNER METADATA INTO DO 
          */
         List<String> listOwner = (List<String>)hashMap.get(MetadataTag.OWNER.getValue());
         if (listOwner!=null &&  listOwner.size()>0) {
             User user = new DefaultUser();
             user.setName_and_surname(listOwner.get(0));
             logger.debug("Owner found : "+listOwner.get(0));
             doObj.setHas_owner(user);
         }

         /***
          * INSERT OF PUBLICATION_DATE METADATA INTO DO 
          */
         List<String> listPublicationDate = (List<String>)hashMap.get(MetadataTag.PUBLICATION_DATE.getValue());
         if (listPublicationDate!=null &&  listPublicationDate.size()>0) {
             logger.debug("Publication Date found : "+listPublicationDate.get(0));
             doObj.setPubblication_date(listPublicationDate.get(0));
         }

         /***
          * INSERT OF LAST_MOD_DATE METADATA INTO DO 
          */
         List<String> listModDate = (List<String>)hashMap.get(MetadataTag.LAST_MOD_DATE.getValue());
         if (listModDate!=null &&  listModDate.size()>0) {
             logger.debug("Mod Date found : "+listModDate.get(0));
             doObj.setLast_mod_date(listModDate.get(0));
         }

         /***
          * INSERT OF SEMANTICALLY RELATED TO METADATA INTO DO 
          */
         List<String> listSemanticallyRelated = (List<String>)hashMap.get(MetadataTag.SEMANTICALLY_RELATED_WITH.getValue());
         if (listSemanticallyRelated!=null &&  listSemanticallyRelated.size()>0)
             for(String semanticallyRelated:listSemanticallyRelated) {
                 logger.debug("Semantically Related list size : "+listSemanticallyRelated.size());
                 DefaultDO_with_Confidence elem = relatedTo(semanticallyRelated);
                 doObj.addSemantically_related_to(elem);
             }

         /***
          * INSERT OF SINTACTICALLY RELATED TO METADATA INTO DO 
          */
         List<String> listSintacticallyRelated = (List<String>)hashMap.get(MetadataTag.SINTACTICALLY_RELATED_WITH.getValue());
         if (listSintacticallyRelated!=null &&  listSintacticallyRelated.size()>0) {
             logger.debug("Sintactically Related list size : "+listSintacticallyRelated.size());
             for(String sintacticallyRelated:listSintacticallyRelated) {
                 DefaultDO_with_Confidence elem = relatedTo(sintacticallyRelated);
                 doObj.addSemantically_related_to(elem);
             }
         }

         /***
          * INSERT OF IS_PARTOF METADATA INTO DO 
          */

         List<String> listIsPartOf = (List<String>)hashMap.get(MetadataTag.IS_PARTOF.getValue());
         if (listIsPartOf!=null &&  listIsPartOf.size()>0) {
             logger.debug("Is part Of found : "+listIsPartOf.get(0));
             DefaultDO  doDummy = new DefaultDO();
   
             doDummy.setObject_ID(Integer.parseInt(listIsPartOf.get(0)));
             doObj.setIs_part_of(doDummy);
         }
         
         
         /***
          * INSERT OF TITLE METADATA INTO DO 
          */
         List<String> listTitle = (List<String>)hashMap.get(MetadataTag.TITLE.getValue());
         if (listTitle!=null &&  listTitle.size()>0)
             for(String title:listTitle) {
                 doObj.addTitle(title);          
             }
         
         
         /***
          * INSERT OF KEYWORDS METADATA INTO DO 
          */
         List<String> listKeywords = (List<String>)hashMap.get(MetadataTag.KEYWORDS.getValue());
         if (listKeywords!=null &&  listKeywords.size()>0)
             for(String keyword:listKeywords) {
                 doObj.addKeywords(keyword);
                 logger.debug(" Keyword added "+keyword);
             }
         
         
         /***
          * INSERT OF REGIONS METADATA INTO DO 
          */
         List<String> listRegions = (List<String>)hashMap.get(MetadataTag.REGIONS.getValue());
         if (listRegions!=null &&  listRegions.size()>0)
             for(String region:listRegions) {
                 doObj.addHas_regions((Region) instanceFromClassName(region));
                 logger.debug(" Region added "+region);
             }
         
         
         /***
          * INSERT OF SUBPARTS METADATA INTO DO 
          */
         List<String> listSubparts = (List<String>)hashMap.get(MetadataTag.HAS_SUBPARTS.getValue());
         if (listSubparts!=null &&  listSubparts.size()>0)
             for(String subpartID:listSubparts) {
                 logger.debug(" Adding subpart"+subpartID);
                 DefaultDO  doDummy = new DefaultDO();
                 doDummy.setObject_ID(Integer.parseInt(subpartID));
                 doObj.addHas_subpart(doDummy);
             }
         
         /***
          * INSERT OF AGES METADATA INTO DO 
          */
         List<String> listAges = (List<String>)hashMap.get(MetadataTag.AGES.getValue());
         if (listAges!=null &&  listAges.size()>0)
             for(String age:listAges) {
                 logger.debug(" Adding ages "+age);
                 doObj.addHas_ages(age);
             }
         
         /***
          * INSERT OF HAS_SCENE_PART METADATA INTO DO 
          */
         List<String> listHasScenePart = (List<String>)hashMap.get(MetadataTag.HAS_SCENE_PART.getValue());
         if (listHasScenePart!=null &&  listHasScenePart.size()>0)
             for(String scenePartID:listHasScenePart) {
                 logger.debug(" Adding has scene part "+scenePartID);
                 DefaultDO  doDummy = new DefaultDO();
                 doDummy.setObject_ID(Integer.parseInt(scenePartID));
                 doObj.addHas_scene_part(doDummy);
             }
         
         
         /***
          * INSERT OF IS_SCENE_PART METADATA INTO DO 
          */
         List<String> listIsPartOfScene = (List<String>)hashMap.get(MetadataTag.IS_PARTOF_SCENE.getValue());
         if (listIsPartOfScene!=null &&  listIsPartOfScene.size()>0)
             for(String partOfSceneID:listIsPartOfScene) {
                 logger.debug(" Adding IS scene part "+partOfSceneID);
                 DefaultDO  doDummy = new DefaultDO();
                 doDummy.setObject_ID(Integer.parseInt(partOfSceneID));
                 doObj.setIs_part_of_scene(doDummy);
             }
         
     }

     private void fillSceneCompositionField(Domain_Specific_Image image) {
         logger.debug("Filling Scene Composition field");
         /***
          * INSERT OF SCENE_COMPOSITION METADATA INTO DOMAIN_SPECIFIC_IMAGE 
          */
         List<String> listSceneComposition = (List<String>)hashMap.get(MetadataTag.SCENE_COMPOSITION.getValue());
         if (listSceneComposition!=null &&  listSceneComposition.size()>0) {
             logger.debug("Scene Composition found : "+listSceneComposition.get(0));   
             image.setScene_composition(listSceneComposition.get(0));
         }

     }

     private void fillBicknellFields(DefaultBicknell_Tracing image) {
         logger.debug("Filling Bicknell fields");
         /***
          * INSERT OF SUPPORT METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listSupport = (List<String>)hashMap.get(MetadataTag.SUPPORT.getValue());
         if (listSupport!=null &&  listSupport.size()>0) {
             image.setSupport((Bicknell_Support) instanceFromClassName(listSupport.get(0)));
         }

         /***
          * INSERT OF PRIMARY_ROLL_SHEET METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listPrimaryRoll = (List<String>)hashMap.get(MetadataTag.PRIMARY_ROLL_SHEET.getValue());
         if (listPrimaryRoll!=null &&  listPrimaryRoll.size()>0) {
             logger.debug("Primary Roll found : "+listPrimaryRoll.get(0));
             int primaryRoll = Integer.parseInt(listPrimaryRoll.get(0));
             image.setPrimary_roll_sheet(primaryRoll);
         }

         /***
          * INSERT OF SECONDARY_ROLL_SHEET METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listSecondaryRoll = (List<String>)hashMap.get(MetadataTag.SECONDARY_ROLL_SHEET.getValue());
         if (listSecondaryRoll!=null &&  listSecondaryRoll.size()>0) {
             logger.debug("Secondary Roll found : "+listSecondaryRoll.get(0));
             int secondaryRoll = Integer.parseInt(listSecondaryRoll.get(0));
             image.setSecondary_roll_sheet(secondaryRoll);
         }

         /***
          * INSERT OF NOTE METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listNote = (List<String>)hashMap.get(MetadataTag.NOTE.getValue());
         if (listNote!=null &&  listNote.size()>0) {
             logger.debug("Note found : "+listNote.get(0));
             image.setNote(listNote.get(0));
         }


         /***
          * INSERT OF SHEET_DIMENSION METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listDimension = (List<String>)hashMap.get(MetadataTag.SHEET_DIMENSION.getValue());
         if (listDimension!=null &&  listDimension.size()>0) {
             logger.debug("Dimension found : "+listDimension.get(0));
             image.setSheet_dimension(listNote.get(0));
         }


         /***
          * INSERT OF HDL METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listHDL = (List<String>)hashMap.get(MetadataTag.HDL.getValue());
         if (listHDL!=null &&  listHDL.size()>0) {
             logger.debug("HDL found : "+listHDL.get(0));
             image.setHDL(listHDL.get(0));
         }


         /***
          * INSERT OF CLUSTER METADATA INTO DEFAULT_BICKNELL_TRACING 
          */
         List<String> listCluster = (List<String>)hashMap.get(MetadataTag.CLUSTER.getValue());
         if (listCluster!=null &&  listCluster.size()>0) {
             logger.debug("Cluster found : "+listCluster.get(0));
             image.setCluster(listCluster.get(0)); 
         }

     }

     /***
      * transform the Item metadata in a MultiHashMap. This is for convenience aim because it is more confortable to use
      * @param item the Item containing metadata
      */
     private void fillMultiHashMap(Item item) {
         List<Metadata> metadata = item.getMetadata();
         logger.debug("HashMap Content");
         for (Metadata p: metadata)   {
             hashMap.put(p.fullName(), p.getValue()); 
             logger.debug("--> Element : "+p.fullName()+", Value : "+p.getValue());
         }
         logger.debug("End HashMap Content");
     }

     private jade.util.leap.List toDefaultInterpreations(List<String> metadataList) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
         jade.util.leap.List d = new jade.util.leap.ArrayList();
         for (String metadataValue:metadataList) {
             logger.debug("********************");
             StringTokenizer st = new StringTokenizer(metadataValue,":;");
             ArrayList<String> tokens = new ArrayList<String>();  
             while ( st.hasMoreTokens() ) {  
                 tokens.add(st.nextToken());  
             }
             logger.debug("TOKEN SIZE : "+tokens.size());
             DefaultCalculated_Interpretation classOrDef = new DefaultCalculated_Interpretation();

             if (tokens.size() < 2) continue;
             String interpretationClass = (String) tokens.get(1);
             String classe = packagename+interpretationClass;
             try {
                 logger.debug("---> Class : "+classe);
                 Class cls1 = Class.forName(classe);
                 Interpretation obj = (Interpretation)cls1.newInstance();
                 classOrDef.setIdentified_interpretation(obj);
                 if (tokens.size() < 4) continue;
                 String confidenceValue = (String) tokens.get(3);
                 logger.debug("---> Confidence : "+confidenceValue);
                 classOrDef.setConfidence(Integer.parseInt(confidenceValue));
                 if (tokens.size() < 6) continue;
                 String sourceValue =(String) tokens.get(5);
                 logger.debug("---> Source : "+sourceValue);
                 classOrDef.setSource(sourceValue);
                 d.add(classOrDef);
             } catch(Exception e) {
                 logger.error("Skipping intepretation read on class "+classe);
             }


         }

         return d;
     }

     private jade.util.leap.List toDefaultClassifications(List<String> metadataList) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
         jade.util.leap.List d = new jade.util.leap.ArrayList();

         for (String metadataValue:metadataList) {
             ArrayList<String> tokens = new ArrayList<String>();  
             StringTokenizer st = new StringTokenizer(metadataValue,":;");
             while ( st.hasMoreTokens() ) {  
                 tokens.add(st.nextToken());  
             }
             DefaultCalculated_Classification classOrDef = new DefaultCalculated_Classification();
             String interpretationClass = (String) tokens.get(1);
             String classe = packagename+interpretationClass;
             Class cls = Class.forName(classe);
             Classification obj = (Classification)cls.newInstance();
             classOrDef.setIdentified_classification(obj);
             String confidenceValue = (String) tokens.get(3);
             classOrDef.setConfidence(Integer.parseInt(confidenceValue));
             String sourceValue =(String) tokens.get(5);
             classOrDef.setSource(sourceValue);
             d.add(classOrDef);
         }

         return d;
     }

     private  List<Field> parseImage(Image digitalImage) {
         metadata = parseDO(digitalImage);

         if (digitalImage.getId_rectangle()!=0)
             metadata.add(new Field(MetadataTag.ID_RECTANGLE.getValue(),digitalImage.getId_rectangle()+""));

         
         if (digitalImage.getHas_coordinates()!=null)
             metadata.add(new Field(MetadataTag.HAS_COORDINATES.getValue(),digitalImage.getHas_coordinates()));

     
         if (digitalImage.getHas_dimensions()!=null)
             metadata.add(new Field(MetadataTag.HAS_DIMENSION.getValue(),digitalImage.getHas_dimensions()));

         
         
  /*       if (digitalImage.getCaption()!=null) 
             metadata.add(new Field(MetadataTag.CAPTION.getValue(),digitalImage.getCaption()));
             */
         return metadata;
     }

     private  List<Field> parseDomainSpecificImage(Domain_Specific_Image digitalImage) {
         metadata = parseImage(digitalImage);
         if (digitalImage.getScene_composition()!=null)
             metadata.add(new Field(MetadataTag.SCENE_COMPOSITION.getValue(),digitalImage.getScene_composition()));
         return metadata; 
     }

     private  List<Field> parse(Sketch digitalImage) {
         metadata = parseDomainSpecificImage(digitalImage);
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.SKETCH.getValue()));
         return metadata; 
     }

     private  List<Field> parse(Bicknell_Tracing digitalImage) {
         System.out.println("SONO IN PARSEEEEE");
         metadata = parseDomainSpecificImage(digitalImage);
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.BICKNELLTRACING.getValue()));
         metadata.add(new Field(MetadataTag.PRIMARY_ROLL_SHEET.getValue(),digitalImage.getPrimary_roll_sheet()+""));
         metadata.add(new Field(MetadataTag.SECONDARY_ROLL_SHEET.getValue(),digitalImage.getSecondary_roll_sheet()+""));
         metadata.add(new Field(MetadataTag.NOTE.getValue(),digitalImage.getNote()));
         metadata.add(new Field(MetadataTag.SHEET_DIMENSION.getValue(),digitalImage.getSheet_dimension()));
         metadata.add(new Field(MetadataTag.HDL.getValue(),digitalImage.getHDL()));
         metadata.add(new Field(MetadataTag.CLUSTER.getValue(),digitalImage.getCluster()));
         if (digitalImage.getSupport()!=null) metadata.add(new Field(MetadataTag.SUPPORT.getValue(),digitalImage.getSupport().getClass().getSimpleName()));
         return metadata; 
     }

     private  List<Field> parse(BW_Image digitalImage) {
         metadata = parseDomainSpecificImage(digitalImage);
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.BWIMAGE.getValue()));
         return metadata; 
     }

     private  List<Field> parse(Carving_Picture digitalImage) {
         metadata = parseDomainSpecificImage(digitalImage);
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.CARVING_PICTURE.getValue()));
         return metadata; 
     }

     private  List<Field> parse(Text digitalText) {
         metadata = parseDO(digitalText);

         if (digitalText.getType()!=null) 
             metadata.add(new Field(MetadataTag.TYPE.getValue(),digitalText.getType())); 
         



         
         if (digitalText.getAllSummary()!=null)  {
             Iterator summaries = digitalText.getAllSummary();
             while (summaries.hasNext()) {
                 String summary = (String) summaries.next();
                 metadata.add(new Field(MetadataTag.SUMMARY.getValue(),summary)); 
             }

         }
         
         if (digitalText.getAllText_abstract()!=null)  {
             Iterator textAbstracts = digitalText.getAllText_abstract();
             while (textAbstracts.hasNext()) {
                 String textAbstract = (String) textAbstracts.next();
                 metadata.add(new Field(MetadataTag.ABSTRACT.getValue(),textAbstract)); 
             }

         }


         if (digitalText.getAllAuthors()!=null)  {
             Iterator authors = digitalText.getAllAuthors();
             while (authors.hasNext()) {
                 Person author = (Person) authors.next();
                 metadata.add(new Field(MetadataTag.AUTHORS.getValue(),author.getName_and_surname())); 
             }
         }
         
         
         if (digitalText.getAllLanguage()!=null)  {
             Iterator languages = digitalText.getAllLanguage();
             while (languages.hasNext()) {
                 metadata.add(new Field(MetadataTag.LANGUAGE.getValue(),languages.next()+"")); 
             }
         }

         return metadata;

     }

     private  List<Field> parse(DefaultImage digitalImage) {
         metadata = parseImage(digitalImage);
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.GENERICIMAGE.getValue()));
         return metadata; 
     }

     
     private  List<Field> parse(DefaultDomain_Specific_Image digitalImage) {
         metadata = parseImage(digitalImage);
         metadata.add(new Field(MetadataTag.SCENE_COMPOSITION.getValue(),digitalImage.getScene_composition()));
         metadata.add(new Field(MetadataTag.IMAGETYPE.getValue(),ImageType.DOMAIN_SPECIFIC_IMAGE.getValue()));
         
         return metadata; 
     }
    
    private InsertItemRequest toDSpace(DefaultDomain_Specific_Image digitalImage) {
        parse(digitalImage);
        item.setMetadata(metadata);
        return item;
    }
     
     private  List<Field> parseDO(DO digitalObject) {

        
         metadata.add(new Field(MetadataTag.IS_SCENE.getValue(),digitalObject.getIs_scene()+""));
         
         if (digitalObject.getFree_text_description()!=null)      metadata.add(new Field(MetadataTag.DESCRIPTION.getValue(),digitalObject.getFree_text_description()));
         if (digitalObject.getHas_owner()!=null)                  metadata.add(new Field(MetadataTag.OWNER.getValue(),digitalObject.getHas_owner().getName_and_surname()));      
         if (digitalObject.getHas_creator()!=null)                metadata.add(new Field(MetadataTag.CREATOR.getValue(),digitalObject.getHas_creator().getName_and_surname()));
         if (digitalObject.getIs_part_of() != null)               metadata.add(new Field(MetadataTag.IS_PARTOF.getValue(),digitalObject.getIs_part_of().getObject_ID()+""));
         if (digitalObject.getPubblication_date() != null)        metadata.add(new Field(MetadataTag.PUBLICATION_DATE.getValue(),digitalObject.getPubblication_date()));
         if (digitalObject.getLast_mod_date() != null)            metadata.add(new Field(MetadataTag.LAST_MOD_DATE.getValue(),digitalObject.getLast_mod_date() ));
         if (digitalObject.getHas_GPS() != null)                  metadata.add(new Field(MetadataTag.GPS.getValue(),GPStoMetadata(digitalObject)));
         if (digitalObject.getUri() != null)                      metadata.add(new Field(MetadataTag.URI.getValue(),digitalObject.getUri()));
         if (digitalObject.getIs_part_of_scene() != null)         metadata.add(new Field(MetadataTag.IS_PARTOF_SCENE.getValue(),digitalObject.getIs_part_of_scene().getObject_ID()+""));
         if (digitalObject.getIssue_date() != null)               metadata.add(new Field(MetadataTag.ISSUED.getValue(),digitalObject.getIssue_date()));
              
         
         
        //      if (digitalObject.getHas_format() !=null )               metadata.add(new Field(MetadataTag.FORMAT.getValue(),digitalObject.getHas_format()));

         
         Iterator hasAges = digitalObject.getAllHas_ages();
         if (hasAges != null) {
             while (hasAges.hasNext()) {
                  metadata.add(new Field(MetadataTag.AGES.getValue(), (String) hasAges.next()));
                 
             }
         }
         
         Iterator hasRegions = digitalObject.getAllHas_regions();
         if (hasRegions != null) {
             while (hasRegions.hasNext()) {
                  metadata.add(new Field(MetadataTag.REGIONS.getValue(), hasRegions.next().getClass().getSimpleName()));
                 
             }
         }

         Iterator sceneParts = digitalObject.getAllHas_scene_part();
         if (sceneParts != null) {
             while (sceneParts.hasNext()) {
                  metadata.add(new Field(MetadataTag.HAS_SCENE_PART.getValue(), ((DO)sceneParts.next()).getObject_ID()+""));
                 
             }
         }
         
         if (digitalObject.getAllKeywords()!=null)  {
             Iterator keywords = digitalObject.getAllKeywords();
             while (keywords.hasNext()) {
                 String keyword = (String) keywords.next();
                 metadata.add(new Field(MetadataTag.KEYWORDS.getValue(),keyword)); 
             }

         }
         
         Iterator semanticallyRelated = digitalObject.getAllSemantically_related_to();
         if (semanticallyRelated != null) {
             while (semanticallyRelated.hasNext()) {
                 DefaultDO_with_Confidence c = (DefaultDO_with_Confidence) semanticallyRelated.next();
                 metadata.add(new Field(MetadataTag.SEMANTICALLY_RELATED_WITH.getValue(), relatedToToMetadata(c)));
                 
             }
         }
         
         
         
         Iterator titles = digitalObject.getAllTitle();
         if (titles != null) {
             while (titles.hasNext()) {
                  metadata.add(new Field(MetadataTag.TITLE.getValue(), (String)titles.next()));
                 
             }
         }

         
         Iterator subparts = digitalObject.getAllHas_subpart();
         if (subparts != null) {
             while (subparts.hasNext()) {
                  metadata.add(new Field(MetadataTag.HAS_SUBPARTS.getValue(), ((DO)subparts.next()).getObject_ID()+""));
                 
             }
         }

         Iterator sintacticallyRelated = digitalObject.getAllSyntactically_related_to();
         if (sintacticallyRelated != null) {
             while (sintacticallyRelated.hasNext()) {
                 DefaultDO_with_Confidence c = (DefaultDO_with_Confidence) sintacticallyRelated.next();
                 metadata.add(new Field(MetadataTag.SINTACTICALLY_RELATED_WITH.getValue(), relatedToToMetadata(c)));
                 
             }
         }

         Iterator classifications = digitalObject.getAllHas_classifications();
         if (classifications != null ) {
             while (classifications.hasNext()) {
                 DefaultCalculated_Classification c = (DefaultCalculated_Classification) classifications.next();
                 metadata.add(new Field(MetadataTag.CLASSIFICATION.getValue(), classificationToMetadata(c)));
                 
             }
         }

         Iterator interpretations = digitalObject.getAllHas_interpretations();
         if (interpretations != null ) {
             while (interpretations.hasNext()) {
                 DefaultCalculated_Interpretation c = (DefaultCalculated_Interpretation) interpretations.next();
                 metadata.add(new Field(MetadataTag.INTERPRETATION.getValue(), interpretationToMetadata(c)));
             }
         }
         return metadata;
     }

     private  String GPStoMetadata(DO digitalObject) {
         return "east=" + digitalObject.getHas_GPS().getEast() + ";north=" + digitalObject.getHas_GPS().getNorth() + ";elevation="
                 + digitalObject.getHas_GPS().getElevation() + ";units=" + digitalObject.getHas_GPS().getUnit() + ";zunits=" + digitalObject.getHas_GPS().getZunit()
                 + ";projection=" + digitalObject.getHas_GPS().getProjection()+ ";name=" + digitalObject.getHas_GPS().getGps_name();
     }

     private  DefaultGPS MetadataToGPS(String metadata) {
         ArrayList<String> tokens = new ArrayList<String>();  
         DefaultGPS gps = new DefaultGPS();
         StringTokenizer st = new StringTokenizer(metadata,"=;");
         while ( st.hasMoreTokens() ) {  
             tokens.add(st.nextToken());  
         }
         gps.setEast(Float.parseFloat(tokens.get(1)));
         gps.setNorth(Float.parseFloat(tokens.get(3)));
         gps.setElevation(Float.parseFloat(tokens.get(5)));
         gps.setUnit(tokens.get(7));
         gps.setZunit(Float.parseFloat(tokens.get(9)));
         gps.setProjection(tokens.get(11));
         gps.setGps_name(tokens.get(13));
         return gps;
     }

     private  DefaultDO_with_Confidence relatedTo(String metadata) {
         logger.debug("***************************");
         ArrayList<String> tokens = new ArrayList<String>();  
         DefaultDO_with_Confidence object = new DefaultDO_with_Confidence();
         StringTokenizer st = new StringTokenizer(metadata,":;");
         while ( st.hasMoreTokens() ) {  
             tokens.add(st.nextToken());  
         }
         DefaultDO digObj = new DefaultDO();
         digObj.setObject_ID(Integer.parseInt(tokens.get(1)));
         logger.debug("---> Object ID : "+digObj.getObject_ID());
         object.setRelated_DO(digObj);
         object.setConfidence(Integer.parseInt(tokens.get(3)));
         logger.debug("---> Confidence : "+tokens.get(3));
         object.setSource_rel_DO(tokens.get(5));
         logger.debug("---> Source : "+tokens.get(5));
         return object;
     }
     
     private String relatedToToMetadata(DefaultDO_with_Confidence c) {
         String output="";
         output+="related_DO:"+c.getRelated_DO().getObject_ID()+";";
         output+="confidence:"+c.getConfidence()+";";
         output+="source:"+c.getSource_rel_DO();
         return output;
     }
     

     
     
     private String interpretationToMetadata(DefaultCalculated_Interpretation c) {
         String output="";
         output+="interpretation:"+c.getIdentified_interpretation().getClass().getSimpleName()+";";
         output+="confidence:"+c.getConfidence()+";";
         output+="source:"+c.getSource();
         return output;
     }
     
     private String classificationToMetadata(DefaultCalculated_Classification c) {
         String output="";
         output+="classification:"+c.getIdentified_classification().getClass().getSimpleName()+";";
         output+="confidence:"+c.getConfidence()+";";
         output+="source:"+c.getSource();
         
         return output;
         
     }

     private Metadata metadataBuilder(String fullname, String value) {
         Metadata metadata = new Metadata();
         String str = fullname;
         StringTokenizer st = new StringTokenizer(str,".");
         if (st.hasMoreElements()) metadata.setSchema(st.nextToken());
         if (st.hasMoreElements()) metadata.setElement(st.nextToken());
         if (st.hasMoreElements()) metadata.setQualifier(st.nextToken());   
         if (value !=null) metadata.setValue(value);
         return metadata;
     }
     
     private Object instanceFromClassName(String className) {
         className=packagename+className;
        
         Constructor<?> cons;
         Object object = null;
        try {
            Class<?> c = Class.forName(className);
            cons = c.getConstructor();
            object = cons.newInstance();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
     }

}

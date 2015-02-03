package it.unisa.indianamas.dspace;

import it.unisa.dspace.Exception.WrongTypeException;
import it.unisa.dspace.entities.request.items.InsertItemRequest;
import it.unisa.dspace.entities.response.items.Item;
import it.unisa.dspace.entities.response.items.Items;
import it.unisa.dspace.rest.ItemCalls;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.springframework.beans.factory.annotation.Autowired;

import it.unige.indianamas.ontology.Attached_File;
import it.unige.indianamas.ontology.DO;
import it.unige.indianamas.ontology.impl.DefaultAttached_File;


public class IM2DSpaceLayer {
	private String uri;
	private String user;
	private String pwd;
	

	private IM2DSpaceConverter converter = new IM2DSpaceConverter();
    private Logger logger = Logger.getLogger("edu.unisa.indianamas.dspace.IM2DSpaceLayer");
    public void update(DO doObj) throws WrongTypeException, JAXBException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        int id = doObj.getObject_ID();
        InsertItemRequest item = converter.toDSpace(doObj,0);
        ItemCalls itemCalls = new ItemCalls(uri, user, pwd);
        itemCalls.update(item.getMetadata(), id);
    }

    public int  insert(String collectionID, DO doObj, ArrayList<String> filenames) {//throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        int collectionId = 1;
        try { 
            InsertItemRequest item = converter.toDSpace(doObj,Integer.parseInt(collectionID));
            try {
                ItemCalls itemCalls = new ItemCalls(uri, user, pwd);
                return itemCalls.insert(item, filenames);
            } catch(Exception e) {
                e.printStackTrace();
                return -1;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
     
    public void delete(int id) {
        ItemCalls itemCalls = new ItemCalls(uri, user, pwd);
        itemCalls.deleteItem(id);
    }
    
    public ArrayList<DO> getAll() throws JAXBException {
        ArrayList<DO> outputList = new ArrayList<DO>();
        ItemCalls itemCalls = new ItemCalls(uri, user, pwd);
        ArrayList<Item> items = (ArrayList<Item>) ((Items)itemCalls.get()).getItems();
        logger.debug("Item List size : "+items.size());
        for (Item item: items) {
             outputList.add(converter.fromDSpace(item));
        }

        
        return outputList;
    }
    
    

    
    public DO getById(int id) throws JAXBException {
        System.out.println(uri+" "+user+" "+pwd);
        ItemCalls itemCalls = new ItemCalls(uri, user, pwd);
        Item item = (Item) itemCalls.getById(id);


        DO output = converter.fromDSpace(item);
        
        output.setHas_attached_file(getImagepathsByItemID(id));
    
        return output;
    }

    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }


    public String getPwd() {
        return pwd;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public IM2DSpaceConverter getLayer() {
        return converter;
    }


    public void setLayer(IM2DSpaceConverter layer) {
        this.converter = layer;
    }
	
	private jade.util.leap.List getImagepathsByItemID(int id) {
	    ConfigurationManager.loadConfig("/opt/dspace/config/dspace.cfg");
        String prop = ConfigurationManager.getProperty("db.url");
        
        String query="select bitstream.bitstream_id, name,internal_id from item2bundle inner join bundle2bitstream on item2bundle.bundle_id=bundle2bitstream.bundle_id inner join bitstream on bitstream.bitstream_id=bundle2bitstream.bitstream_id where item_id= ?";

        try {
            Context context = new Context();
            System.out.println(context.getDBConnection() + "dopo context");
            //DatabaseManager db= new DatabaseManager();
            Object[] p= new Integer[1];
            p[0]=Integer.valueOf(id);
            TableRowIterator tr1=DatabaseManager.query(context,query,p);
            jade.util.leap.List listAttachedFiles = new jade.util.leap.ArrayList();
            while  (tr1.hasNext()) {
            	TableRow tr = tr1.next();
            	Attached_File attached = new DefaultAttached_File();
            	attached.setFile_name(tr.getStringColumn("name"));
            	attached.setFile_path(tr.getStringColumn("internal_id"));
            	attached.setFile_order(tr.getIntColumn("bitstream_id"));
            	listAttachedFiles.add(attached);
            	
            }
            
            return listAttachedFiles;
        
            
            
            
            // c'e' il campo che ci serve, slo che non so bene come recuperare 183, direi che prima serve un'altra chiamata rest al boundle
    //        TableRow tr=DatabaseManager.find(context, "bitstream", 183);
     //       System.out.println(tr.toString());
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("dopo context");
        return null;
	}
	
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author niccher
 */
public class Drugs {
    ResultSet rs=null;
    Connection Conn=null;
    PreparedStatement pst=null;
    Statement smt;
    
    ArrayList<String> listing= new ArrayList<String>();
    
    public Drugs() {
        Conn=(Connection) Dbs.InitDb();
    }

    public void DrugInfo(int drug_id){}
    
    public void DrugCount(int drug_id){}
    
    public ArrayList<String> DrugList(){
        try {
            String cops4= "SELECT * FROM `tbl_Stock`";
            pst = ((PreparedStatement)Conn.prepareStatement(cops4));
            rs = pst.executeQuery();
            //Dr1.removeAllItems();
            //Dr1.addItem("<Select>");
            while (rs.next()) {
                String dr=rs.getString("Name");
                listing.add(dr);
                //Dr1.addItem(dr);
                //d6=rs.getInt("StockID");
                
            }
            
            return  listing;
            //System.out.println("Modes are "+listing);
            
        } catch (Exception e) {
            System.out.println("ArrayList<String> DrugList() "+e.getMessage());
            return listing;
        }
        
    }
    
}

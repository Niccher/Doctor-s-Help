/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author nicch
 */
public class Dbs {
    private Connection conn=null;
    
        public static Connection InitDb() {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","");

                PreparedStatement dbs = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS `HighwayMed`");
                dbs.execute();

               Statement stt = conn.createStatement();
               stt.execute("USE HighwayMed");
               
               String Std = "CREATE TABLE IF NOT EXISTS `tbl_Staff` (`Count` INT AUTO_INCREMENT UNIQUE, `Name` VARCHAR(35) NOT NULL , `NationalID` INT(8) NOT NULL , `StaffID` INT(6) NOT NULL PRIMARY KEY , `Username` VARCHAR(20) NOT NULL , `Password` VARCHAR(20) NOT NULL ,`Contacts` INT(10) NOT NULL ,`Level` VARCHAR(10) NOT NULL, `Avatar` LONGBLOB)";
               PreparedStatement pst1 = conn.prepareStatement(Std);
               pst1.execute();
               
                try {
                    String Std2 = "INSERT INTO `tbl_Staff` (`Count`, `Name`, `NationalID` , `StaffID` , `Username` , `Password` ,`Contacts` ,`Level`,`Avatar`) VALUES (NULL,'SuperUser',101010,01,'admin','admin',07123456,'Admin',NULL)";
                    PreparedStatement pst2 = conn.prepareStatement(Std2);
                    pst2.executeUpdate();  
                } catch (Exception e) {
                }  
               
               String Std3 = "CREATE TABLE IF NOT EXISTS `tbl_Clients` ( `Count` INT AUTO_INCREMENT UNIQUE,`Name` VARCHAR(35) NOT NULL , `NationalID` INT(8) NOT NULL, `ClientID` INT(6) NOT NULL PRIMARY KEY,`Region` VARCHAR(20) NOT NULL,`Date` VARCHAR(15) NOT NULL,`Contacts` INT(10) NOT NULL, `Avatar` LONGBLOB NOT NULL )";
               PreparedStatement pst3 = conn.prepareStatement(Std3);
               pst3.execute();
               
               String Std4 = "CREATE TABLE IF NOT EXISTS `tbl_Stock` ( `Count` INT AUTO_INCREMENT UNIQUE, `Name` VARCHAR(10) NOT NULL , `StockDate` VARCHAR(16) NOT NULL ,`StockTime` VARCHAR(10) NOT NULL ,`Company` VARCHAR(20) NOT NULL,`Unit` VARCHAR(20) NOT NULL,`UnitPrice` INT(5) NOT NULL,`StockID` INT(10) NOT NULL,`StockDesc` VARCHAR(40) NOT NULL ,`StockScope` VARCHAR(25) NOT NULL,`SideEffect` VARCHAR(25) NOT NULL)";
               PreparedStatement pst4 = conn.prepareStatement(Std4);
               pst4.execute();
               
               String Std5 = "CREATE TABLE IF NOT EXISTS `tbl_Logs` ( `Count` INT AUTO_INCREMENT UNIQUE, `Date` VARCHAR(16) NOT NULL ,`Time` VARCHAR(10) NOT NULL ,`Username` VARCHAR(20) NOT NULL ,`Action` VARCHAR(50)  NOT NULL ,`Correct` VARCHAR(10) NOT NULL)";
               PreparedStatement pst5 = conn.prepareStatement(Std5);
               pst5.execute();
               
               String Std6 = "CREATE TABLE IF NOT EXISTS `tbl_Admine` ( `Count` INT AUTO_INCREMENT UNIQUE, `AcDate` VARCHAR(16) NOT NULL ,`AcTime` VARCHAR(16) NOT NULL ,`Task` VARCHAR(30) NOT NULL ,`Output` VARCHAR(10)  NOT NULL)";
               PreparedStatement pst6 = conn.prepareStatement(Std6);
               pst6.execute();
               
               String Std7 = "CREATE TABLE IF NOT EXISTS `tbl_Treats` (`Count` INT AUTO_INCREMENT UNIQUE ,`Name` VARCHAR(20) NOT NULL ,`NationalID`  VARCHAR(20) NOT NULL ,`ClientID` INT(10) NOT NULL ,`Date` VARCHAR(16) NOT NULL ,`Time` VARCHAR(10) NOT NULL ,`Conditn` VARCHAR(30) NOT NULL ,`Diagnosis` VARCHAR(30) NOT NULL ,`Lab` VARCHAR(30) NOT NULL ,`Drugs`  VARCHAR(255) NOT NULL )";
               PreparedStatement pst7 = conn.prepareStatement(Std7);
               pst7.execute();
               
               String Std8 = "CREATE TABLE IF NOT EXISTS `tbl_Drugs`  (`Count` INT AUTO_INCREMENT UNIQUE ,`ClientID` INT(10) NOT NULL ,`Date` VARCHAR(16) NOT NULL ,`Time` VARCHAR(10) NOT NULL ,`StockID` INT(10) NOT NULL ,`Drug` VARCHAR(30) NOT NULL ,`Dosage` VARCHAR(4) NOT NULL ,`Duration`  INT(2) NOT NULL )";
               PreparedStatement pst8 = conn.prepareStatement(Std8);
               pst8.execute();
               
               String Std9 = "CREATE TABLE IF NOT EXISTS `tbl_StockLevel`  (`Count` INT AUTO_INCREMENT UNIQUE ,`Name` VARCHAR(30) NOT NULL ,`StockID` INT(10) NOT NULL ,`Avail` INT(5) NOT NULL)";
               PreparedStatement pst9 = conn.prepareStatement(Std9);
               pst9.execute();
               
               String Std10 = "CREATE TABLE IF NOT EXISTS `tbl_StockMorn`  (`Count` INT AUTO_INCREMENT UNIQUE ,`Name` VARCHAR(30) NOT NULL ,`StockID` INT(10) NOT NULL ,`Avail` INT(5) NOT NULL,`Date` VARCHAR(30) NOT NULL)";
               PreparedStatement pst10 = conn.prepareStatement(Std10);
               pst10.execute();
               
               String Std11 = "CREATE TABLE IF NOT EXISTS `tbl_StockLow`  (`Count` INT AUTO_INCREMENT UNIQUE ,`Name` VARCHAR(30) NOT NULL ,`StockID` INT(10) NOT NULL ,`Lows` INT(5) NOT NULL,`Date` VARCHAR(30) NOT NULL)";
               PreparedStatement pst11 = conn.prepareStatement(Std11);
               pst11.execute();

                Statement stmt=conn.createStatement();
                return conn;

            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"\nDatabase  Initialisation Error");
                return  null;
            }
            //return null;
        }
        
        public static void main(String[] args) {
        new Dbs();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author nicch
 */
public class HomePag extends javax.swing.JFrame {
    ResultSet rs=null;
    Connection Conn=null;
    PreparedStatement pst=null;
    Statement smt;
    TimeGenerate timgen = null;

    String Patt,riu,thaa,lo,lo2,tbl,sav2,sav3,sed;
    Calendar cal =new GregorianCalendar();
    int mont,dayt,ya,hr,min,sec,d4,d5,d6,totdrg,stats,lvpr;
    
    File f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12;
    File ff=new File("MornStock.csv");
    
    String day,hours;
    long daty = Long.valueOf(System.currentTimeMillis());

    /**
     * Creates new form HomePag
     */
    public HomePag() {
        initComponents();
        Conn=(Connection) Dbs.InitDb();
        timgen = new TimeGenerate();
        this.setIconImage(new ImageIcon(getClass().getResource("/Imgs/blobwars.png")).getImage());
        Dimension dim=getToolkit().getScreenSize();
        int jframWidth=this.getSize().width;
        int jframHeight=this.getSize().height;
        int locationX=(dim.width-jframWidth)/2;
        int locationY=(dim.height-jframHeight)/2;
        this.setLocation(locationX, locationY);

        setTitle("Doctor's Help");
        //setExtendedState(MAXIMIZED_BOTH);
        Alta();
    }

    private void Alta(){
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Nott.setText(null);
        SysDash.setEnabled(Boolean.FALSE);
        
        IntOnly();
        Poplt();
        Refsh();
        Tara();
        Netts();
        CmpDt();
    }
       
    public String getTime(){
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(daty);
        hours=sdf1.format(resultdate);
        
        return hours;
    } 
    
    public String getDate(){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MMM dd");
        Date resultdate2 = new Date(daty);
        day=sdf2.format(resultdate2);
        
        return day;
    } 

    private void IntOnly(){
        Thread ihinda=new Thread(){
            public void run(){
                for (;;) {
                    dayt=cal.get(Calendar.DAY_OF_MONTH);
                    mont=cal.get(Calendar.MONTH)+1;
                    ya=cal.get(Calendar.YEAR);

                    sec=cal.get(Calendar.SECOND);
                    min=cal.get(Calendar.MINUTE);
                    hr=cal.get(Calendar.HOUR_OF_DAY);

                    riu= dayt+"/"+mont+"/"+ya;
                    thaa= hr+":"+min+":"+sec;

                    StockTm.setText(thaa);
                    StockDt.setText(riu);
                    ClientDt.setText(riu);
                    DDate.setText(riu);
                    
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                    }
                    
                }
            }
        };        
        ihinda.start();
    }
    
    private void Poplt(){
        try{
              String cops = "SELECT `tbl_Stock`.Name,StockDate AS Date,Company,Unit,`tbl_Stock`.StockID,`tbl_StockLevel`.Avail AS Remaining,StockDesc,StockScope,SideEffect FROM `tbl_Stock`,`tbl_StockLevel` WHERE `tbl_Stock`.StockID=`tbl_StockLevel`.StockID";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
              rs = this.pst.executeQuery();
              StockCurr.setModel(DbUtils.resultSetToTableModel(rs));
              
              String cops1 = "SELECT Name,StockDate AS Date,StockTime AS Time,Company,StockID,StockDesc,SideEffect FROM `tbl_Stock`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops1));
              rs = this.pst.executeQuery();
              StockList.setModel(DbUtils.resultSetToTableModel(rs));
              
              String cops11 = "SELECT Name,StockID AS UniqueID,UnitPrice FROM `tbl_Stock`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops11));
              rs = this.pst.executeQuery();
              tbl_Costos.setModel(DbUtils.resultSetToTableModel(rs));              
              
              String cops2 = "SELECT Name,NationalID,ClientID,Date,Region FROM `tbl_Clients`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops2));
              rs = this.pst.executeQuery();
              ClientList.setModel(DbUtils.resultSetToTableModel(rs));
              
              String cops21 = "SELECT Name,NationalID,ClientID,Date,Region FROM `tbl_Clients`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops21));
              rs = this.pst.executeQuery();
              tbl_ClientEdit.setModel(DbUtils.resultSetToTableModel(rs));
              
              String cops3= "SELECT Name,ClientID,Date,Conditn AS Conditions,Diagnosis,Lab,Drugs FROM `tbl_Treats`";
              pst = ((PreparedStatement)Conn.prepareStatement(cops3));
              rs = pst.executeQuery();
              TreatsList.setModel(DbUtils.resultSetToTableModel(rs));
              
              
              String cops5= "SELECT Name,NationalID,StaffID,Username,Password,Contacts,Level FROM `tbl_Staff`";
              pst = ((PreparedStatement)Conn.prepareStatement(cops5));
              rs = pst.executeQuery();
              tbl_Staff.setModel(DbUtils.resultSetToTableModel(rs));
              
              String cops6="SELECT * FROM `tbl_StockLevel` ";
              pst=(PreparedStatement) Conn.prepareStatement(cops6);
              rs=pst.executeQuery();
              tbl_RfList.setModel(DbUtils.resultSetToTableModel(this.rs));
              
              String cops4= "SELECT * FROM `tbl_Stock`";
              pst = ((PreparedStatement)Conn.prepareStatement(cops4));
              rs = pst.executeQuery();
              //Dr1.removeAllItems();
              /*while (rs.next()) {
                String dr=rs.getString("Name");
                Dr1.addItem(dr);}*/
            }
        catch (Exception e){
              JOptionPane.showMessageDialog(null, e + "\nAction Not Allowed Please Pol");
            }
        ComitDrug.setEnabled(Boolean.FALSE);
    }
    
    private void CompDrug(){
        int a1s,b2s,c3s;
        String nm;
        a1s=Integer.parseInt(a1.getText().toString());
        b2s=Integer.parseInt(a2.getText().toString());
        c3s=Integer.parseInt(b1.getText().toString());
        totdrg=a1s*b2s*c3s;
        int sum1=totdrg*d4;

        nm=(Dr1.getSelectedItem().toString());

        lo="Drug\t"+nm+"\n"+"Dosage\t"+a1s +" X "+b2s+"\n"+"Duration\t"+c3s+"\n";
        lo2="Drug\t"+nm+"\n"+"Dosage\t"+a1s +" X "+b2s+"\nDuration\t"+c3s+"\nTotal Tablets \t"+totdrg+"\nUnit Cost\t "+d4+"\n"+"Total Price\t"+sum1;
    }
    
    private void DrgInsrt(){
        
        Drugs dr = new Drugs();
        
        
        long daty = Long.valueOf(System.currentTimeMillis());
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(daty);
        hours=sdf1.format(resultdate);
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MMM dd");
        Date resultdate2 = new Date(daty);
        day=sdf2.format(resultdate2);
        
        int drgcount=0;
        
        int ci=Integer.parseInt(ClienID.getText().toString());
        int dy=Integer.parseInt(b1.getText().toString());
        String dos=a1.getText().toString()+"X"+a2.getText().toString();
        String drg=Dr1.getSelectedItem().toString();

        try {
            String sav="SELECT * FROM tbl_StockLevel WHERE (StockID = '"+d6+"') ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                drgcount=rs.getInt("Avail");
            }
            //JOptionPane.showMessageDialog(null, drgcount+" Drug Count\nDrug Offered "+totdrg);
            
            if (drgcount > totdrg || drgcount == totdrg ) {
                String lv="INSERT INTO `tbl_Drugs` (Count,`ClientID`,`Date`,`Time`,`StockID`,`Drug`,`Dosage`,`Duration`) VALUES (NULL,?,?,?,?,?,?,?)";
                pst= (PreparedStatement) Conn.prepareStatement(lv);
                pst.setInt(1, ci);
                pst.setString(2, day);
                pst.setString(3, hours);
                pst.setInt(4, d5);
                pst.setString(5, drg);
                pst.setString(6, dos);
                pst.setInt(7, dy);

                pst.executeUpdate();
                
                    try {
                        int curr=0,avl=0;
                        String cops4= "SELECT * FROM `tbl_StockLevel` WHERE `StockID` ='"+d5+"'";
                        pst = ((PreparedStatement)Conn.prepareStatement(cops4));
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            curr=rs.getInt("Avail");
                        }
                        avl=curr-totdrg;


                        String Kret="UPDATE `tbl_StockLevel` SET Avail= '"+avl+"' WHERE StockID='"+d5+"'";
                        pst= (PreparedStatement) Conn.prepareStatement(Kret);

                        pst.execute();

                        JOptionPane.showMessageDialog(null, "Drugs Commited Succesfully");
                        Cmmit.setEnabled(Boolean.TRUE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "\nStock Deduction Error");
                    }
            }
            if (totdrg > drgcount) {
                JOptionPane.showMessageDialog(null, "Drugs Indexed Have Been Surpassed\nKindly Restock To Continue Or Lower The Drug Count");
                DruCorna.setText("Changes Not Saved; Refill To Proceed\n\n");
                Cmmit.setEnabled(Boolean.FALSE);
            
            }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "\nDrug Entry Error");
            }
        
        Poplt();
    }
    
    private void Refsh(){
        try {
            String cops = "SELECT tbl_Stock.Name,tbl_Stock.StockID,tbl_Stock.Unit,tbl_Stock.UnitPrice,tbl_StockLevel.Avail AS Remaining,tbl_StockLow.Lows AS Low FROM tbl_Stock,tbl_StockLevel,tbl_StockLow WHERE (tbl_Stock.StockID =tbl_StockLevel.StockID AND tbl_Stock.StockID = tbl_StockLow.StockID AND tbl_StockLevel.StockID = tbl_StockLow.StockID)";
            pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
            rs = this.pst.executeQuery();
            tbl_StLvs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"\n111 111");
        }
    }
    
    private  void Tara(){
        StckLv.setVisible(Boolean.FALSE);
        StckC.setVisible(Boolean.FALSE);
        RefshDet.setVisible(Boolean.FALSE);
        try {
            String cops = "SELECT * FROM tbl_StockLevel,tbl_StockLow WHERE ( tbl_StockLevel.StockID = tbl_StockLow.StockID)";
            pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
            rs = this.pst.executeQuery();
            int hw=0;
            while (rs.next()) {
                String n=rs.getString("tbl_StockLevel.Name");
                int lv=rs.getInt("tbl_StockLevel.Avail");
                int lvlw=rs.getInt("tbl_StockLow.Lows");
                if (lv == lvlw || lv < lvlw) {
                    hw=hw+1;
                    StckLv.setVisible(Boolean.TRUE);
                    StckLv.setText(n+" Is Below Allowed Level "+lv+"\\"+lvlw);
                } 
            }
            if (hw==0) {
                StckLv.setVisible(Boolean.FALSE);
                StckC.setVisible(Boolean.FALSE);
                RefshDet.setVisible(Boolean.FALSE);
            }
            if (hw>0) {
                StckC.setVisible(Boolean.TRUE);
                StckC.setText(hw+" Of All Indexed Stock Needs Refill");
                RefshDet.setVisible(Boolean.TRUE);
                
            }
        } catch (Exception e) {

        }
    }
    
    private void Netts(){
        Integer ki=ConView.getSelectedIndex();
        new Thread(new Runnable(){
            public void run(){
                Socket ss=new Socket();
                InetSocketAddress add=new InetSocketAddress("www.google.com",80);
                try {
                    ss.connect(add,8080);
                    ConView.setSelectedIndex(0);
                } catch (Exception e) {
                    ConView.setSelectedIndex(1);
                }
                
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
            }
        }).start();
        
    }
    
    private void Cmp(){
        f1=new File("Baks/Dump Stock.csv");
        try {
            if (f1.createNewFile()) {
                ConDump.append("Stock Dump File Succesfully Created\n");
            }else{
                ConDump.append(" Stock Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Stock Dump File Exception "+ex+"\n");
        } 
        
        f2=new File("Baks/Dump Treats.csv");
        try {
            if (f2.createNewFile()) {
                ConDump.append("Treats Dump File Succesfully Created\n");
            }else{
                ConDump.append("Treats Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Treats Dump File Exception "+ex+"\n");
        }
        
        f3=new File("Baks/Dump Drugs.csv");
        try {
            if (f3.createNewFile()) {
                ConDump.append("Drug Dump File Succesfully Created\n");
            }else{
                ConDump.append("Drugs Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Drugs Dump File Exception "+ex+"\n");
        }
        
        f4=new File("Baks/Dump Client.csv");
        try {
            if (f4.createNewFile()) {
                ConDump.append("Clients Dump File Succesfully Created\n");
            }else{
                ConDump.append("CLients Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Clients Dump File Exception "+ex+"\n");
        }
        
        f5=new File("Baks/Dump Staff.csv");
        try {
            if (f5.createNewFile()) {
                ConDump.append("Staff Dump File Succesfully Created\n");
            }else{
                ConDump.append("Staff Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Staff Dump File Exception "+ex+"\n");
        }
        
        f6=new File("Baks/Dump Stock Start.csv");
        try {
            if (f6.createNewFile()) {
                ConDump.append("Stock Start Dump File Succesfully Created\n");
            }else{
                ConDump.append("Stock Start Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Stock Start Dump File Exception "+ex+"\n");
        }
        
        f7=new File("Baks/Dump Stock Level.csv");
        try {
            if (f7.createNewFile()) {
                ConDump.append("Stock Level Dump File Succesfully Created\n");
            }else{
                ConDump.append("Stock Level Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Stock Level Dump File Exception "+ex+"\n");
        }
        
        f8=new File("Baks/Dump Stock Low.csv");
        try {
            if (f8.createNewFile()) {
                ConDump.append("Stock Low Dump File Succesfully Created\n");
            }else{
                ConDump.append("Stock Low Dump File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Stock Low Dump File Exception "+ex+"\n");
        }
    }
    
    private void Jaz(){
        try {
            if (ff.createNewFile()) {
                try {
                    FileWriter fw=new FileWriter(ff);
                    String sql="SELECT tbl_StockLevel.Name,tbl_StockLevel.StockID,tbl_StockLevel.Avail  FROM `tbl_StockLevel` ";
                    Statement stmt=(Statement) Conn.createStatement();
                    rs=stmt.executeQuery(sql);
                    int jj=0;
                    while (rs.next()) {    
                        jj=jj+1;
                        fw.append(String.valueOf(jj));
                        fw.append(",");
                        fw.append(rs.getString(1));
                        fw.append(",");
                        fw.append(rs.getString(2));
                        fw.append(",");
                        fw.append(rs.getString(3));
                        fw.append(",");
                        fw.append(riu);
                        fw.append("\n");
                    }
                    fw.flush();
                    fw.close();
                    //ConDump.append("Stock Table Has Been Parsed Succesfully\n");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"\nStock Morning Parse Error");
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"\nFile Parse Error");
        } 
        
        try {
            String ln;
            BufferedReader br=new BufferedReader(new FileReader(ff));
            while ((ln=br.readLine())!=null) {
                String []vls=ln.split(",");
                String sql="INSERT INTO  `tbl_StockMorn` (Count,Name,StockID,Avail,Date) VALUES ('"+vls[0]+"','"+vls[1]+"','"+vls[2]+"','"+vls[3]+"','"+vls[4]+"')";
                pst= (PreparedStatement) Conn.prepareStatement(sql);
                pst.executeUpdate();
            }
            //JOptionPane.showMessageDialog(null,"Succesfully Parsed");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"\nMorning Stock Indexing Failed");
        }
    }
    
    private void CmpDt(){
        try {
            String sql="SELECT * FROM `tbl_StockMorn` LIMIT 1";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            if (rs.next()) {
                String um=rs.getString("Date");
                //JOptionPane.showMessageDialog(null,"Riu "+riu+"\nUm- "+um);
                if (um.equalsIgnoreCase(riu)) {
                    //JOptionPane.showMessageDialog(null,"Equals");
                }
                if (!(um.equalsIgnoreCase(riu))){
                    //JOptionPane.showMessageDialog(null,"Not Equal");
                    try {
                        String sav="DROP TABLE `tbl_StockMorn` ";
                        pst=(PreparedStatement) Conn.prepareStatement(sav);
                        pst.execute();
                    } catch (Exception e) {
                        //JOptionPane.showMessageDialog(null,"\n Indexing Stock Failed");
                    }
                    
                    try {
                        String Std10 = "CREATE TABLE IF NOT EXISTS `tbl_StockMorn`  (`Count` INT AUTO_INCREMENT UNIQUE ,`Name` VARCHAR(30) NOT NULL ,`StockID` INT(10) NOT NULL ,`Avail` INT(5) NOT NULL,`Date` VARCHAR(30) NOT NULL)";
                        java.sql.PreparedStatement pst10 = Conn.prepareStatement(Std10);
                        pst10.execute();
                    } catch (Exception e) {
                        //JOptionPane.showMessageDialog(null,e+"\n Creating Table Error");
                    }
                    Jaz();
                    //JOptionPane.showMessageDialog(null,"Mist Parsed");
                } 
                /*else {
                    JOptionPane.showMessageDialog(null,"Engaging Invalid Comparison");
                }*/
            }else {
                //JOptionPane.showMessageDialog(null,"Table Errors");
                Jaz();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"\n Indexing Stock Error");
        }
        try {
            if (ff.delete()) {
            }
        } catch (Exception e) {
        }
    }
    
    private void Rejoinder(){
        String Ine=String.valueOf(ya)+"-"+String.valueOf(mont)+"-"+String.valueOf(dayt)+"-Dump";
        f9=new File("Baks/"+Ine+".zip");
        try {
            if (f9.createNewFile()) {
                ConDump.append("Zip Wrapper File Succesfully Created\n");
            }else{
                ConDump.append("Zip Wrapper File Creation Error\n");
            }
        } catch (IOException ex) {
            ConDump.append("Zip Wrapper File Exception "+ex+"\n");
        } 
        try {
            ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(f9));
            
            FileInputStream fis1=new FileInputStream(f1);
            zos.putNextEntry(new ZipEntry("Dump Stock.csv"));
            
            byte[] Bufa=new byte[1024];
            int bytr;
            while (((bytr=fis1.read(Bufa))>0)) {                
                zos.write(Bufa,0,bytr);
            }
            
            FileInputStream fis2=new FileInputStream(f2);            
            zos.putNextEntry(new ZipEntry("Dump Treats.csv"));
            
            byte[] Buf2=new byte[1024];
            int bytr2;
            while (((bytr2=fis2.read(Buf2))>0)) {                
                zos.write(Buf2,0,bytr2);
            }
            
            FileInputStream fis3=new FileInputStream(f3);            
            zos.putNextEntry(new ZipEntry("Dump Drugs.csv"));
            
            byte[] Buf1=new byte[1024];
            int bytr1;
            while (((bytr1=fis3.read(Buf1))>0)) {                
                zos.write(Buf1,0,bytr1);
            }
            
            FileInputStream fis4=new FileInputStream(f4);            
            zos.putNextEntry(new ZipEntry("Dump Clients.csv"));
            
            byte[] Buf4=new byte[1024];
            int bytr4;
            while (((bytr4=fis4.read(Buf4))>0)) {                
                zos.write(Buf4,0,bytr4);
            }
            
            FileInputStream fis5=new FileInputStream(f5);            
            zos.putNextEntry(new ZipEntry("Dump Staff.csv"));
            
            byte[] Buf5=new byte[1024];
            int bytr5;
            while (((bytr5=fis5.read(Buf5))>0)) {                
                zos.write(Buf5,0,bytr5);
            }
            
            FileInputStream fis6=new FileInputStream(f6);            
            zos.putNextEntry(new ZipEntry("Dump Stock Start.csv"));
            
            byte[] Buf6=new byte[1024];
            int bytr6;
            while (((bytr6=fis6.read(Buf6))>0)) {                
                zos.write(Buf6,0,bytr6);
            }
            
            FileInputStream fis7=new FileInputStream(f7);            
            zos.putNextEntry(new ZipEntry("Dump Stock Level.csv"));
            
            byte[] Buf7=new byte[1024];
            int bytr7;
            while (((bytr7=fis7.read(Buf7))>0)) {                
                zos.write(Buf7,0,bytr7);
            }
            
            FileInputStream fis8=new FileInputStream(f8);            
            zos.putNextEntry(new ZipEntry("Dump Stock Low.csv"));
            
            byte[] Buf8=new byte[1024];
            int bytr8;
            while (((bytr8=fis8.read(Buf8))>0)) {                
                zos.write(Buf8,0,bytr8);
            }
            
            ConDump.append("Zip Wraping Of Back-Up Is Finished Succesfully\n");
            zos.closeEntry();
            zos.close();
            
            fis1.close();
            fis2.close();
            fis3.close();
            fis4.close();
            fis5.close();
            fis6.close();
            fis7.close();
            fis8.close();
            
        } catch (Exception e) {
            ConDump.append("Zip Wraping Of Back-Up Files Terminated With An Error "+e+"\n");
        }
        
        try {
            if (f1.delete()) {
            }
            if (f2.delete()) {
            }
            if (f3.delete()) {
            }
            if (f4.delete()) {
            }
            if (f5.delete()) {
            }
            if (f6.delete()) {
            }
            if (f7.delete()) {
            }
            if (f8.delete()) {
            }
        } catch (Exception e) {
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        Centa = new javax.swing.JPanel();
        Plogo = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        StckC = new javax.swing.JLabel();
        StckLv = new javax.swing.JLabel();
        RefshDet = new javax.swing.JLabel();
        Plogic = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LogLev = new javax.swing.JComboBox<>();
        Usrnm = new javax.swing.JTextField();
        Pwd = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ProcidLog = new javax.swing.JButton();
        Pan_Config = new javax.swing.JPanel();
        Adminey = new javax.swing.JTabbedPane();
        Stocks = new javax.swing.JPanel();
        Stockies = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        StockIDSrch = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        StockNmSrch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        StockCurr = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        StockID = new javax.swing.JTextField();
        StockComp = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        StockUnit = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        StockEff = new javax.swing.JTextField();
        StockDt = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        StockNm = new javax.swing.JTextField();
        StockTm = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        StockAdd = new javax.swing.JButton();
        StockEdit = new javax.swing.JButton();
        StockDel = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        StockDesc = new javax.swing.JTextArea();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        StockScope = new javax.swing.JTextArea();
        jLabel75 = new javax.swing.JLabel();
        StockUPrice = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        StockList = new javax.swing.JTable();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        tbl_RfSerch = new javax.swing.JTable();
        jLabel98 = new javax.swing.JLabel();
        RfSerch = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        tbl_RfList = new javax.swing.JTable();
        RfNam = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        RfStckID = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        RfAlble = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        RfBy = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        RfNwLv = new javax.swing.JTextField();
        RfADD = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        tbl_Statis = new javax.swing.JTable();
        StatCrit = new javax.swing.JComboBox<>();
        StatParam = new javax.swing.JComboBox<>();
        StatAsses = new javax.swing.JButton();
        UsrStting = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        StaffLvl1 = new javax.swing.JComboBox<>();
        StaffCont1 = new javax.swing.JTextField();
        StaffID1 = new javax.swing.JTextField();
        StaffPwd1 = new javax.swing.JTextField();
        StaffUsr1 = new javax.swing.JTextField();
        StaffIDNo1 = new javax.swing.JTextField();
        StaffNm1 = new javax.swing.JTextField();
        StaffUpdt = new javax.swing.JButton();
        StaffDel = new javax.swing.JButton();
        jScrollPane21 = new javax.swing.JScrollPane();
        tbl_Staff = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        ClientNm1 = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        ClientIDNat1 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        ClientID1 = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        ClientReg1 = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        ClientCont1 = new javax.swing.JTextField();
        ClienUpdt = new javax.swing.JButton();
        ClientDel = new javax.swing.JButton();
        Bck10 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        tbl_ClientEdit = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        tbl_Logs = new javax.swing.JTable();
        Action_Adm = new javax.swing.JRadioButton();
        Action_Staf = new javax.swing.JRadioButton();
        Otherrs = new javax.swing.JPanel();
        Others = new javax.swing.JTabbedPane();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane29 = new javax.swing.JScrollPane();
        tbl_StLvs = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        StLvNum = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        StLvApply = new javax.swing.JButton();
        StLvRfsh = new javax.swing.JLabel();
        StLvSID = new javax.swing.JTextField();
        StLvName = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        ConView = new javax.swing.JComboBox<>();
        jLabel109 = new javax.swing.JLabel();
        ConCheck = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        ConDump = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        NwStaff = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        StaffNm = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        StaffID = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        ImgLab1 = new javax.swing.JLabel();
        ImgPat = new javax.swing.JTextField();
        NwUsr = new javax.swing.JButton();
        StaffUsr = new javax.swing.JTextField();
        StaffPwd = new javax.swing.JTextField();
        StaffLvl = new javax.swing.JComboBox<>();
        Bck2 = new javax.swing.JButton();
        StaffIDNo = new javax.swing.JTextField();
        InsrtImg1 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        StaffCont = new javax.swing.JTextField();
        NwClient = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        ClientNm = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        ClientIDNat = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        ClientID = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        ClientReg = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        ClientDt = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        ImgLab2 = new javax.swing.JLabel();
        ImgPat2 = new javax.swing.JTextField();
        ClientAdd = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        ClientCont = new javax.swing.JTextField();
        InsrtImg3 = new javax.swing.JLabel();
        Pan_Home = new javax.swing.JPanel();
        Cates = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        RedConsl = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        RedInj = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        RedLab = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        RedPharm = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        RedMore = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Pan_Consult = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        ClientNmSrch = new javax.swing.JTextField();
        ClientSerch = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        RfshClient = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ClientList = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TrtCond = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        TrtDiag = new javax.swing.JTextArea();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TrtLab = new javax.swing.JTextArea();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TrtDrug = new javax.swing.JTextArea();
        Bck1 = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jDesktopPane5 = new javax.swing.JDesktopPane();
        ClinImg = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        ClientNam = new javax.swing.JTextField();
        ClienID = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        ClientNat = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        ClieCond = new javax.swing.JTextArea();
        jLabel50 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        ClieDaig = new javax.swing.JTextArea();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        ClieMed = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        LogBar = new javax.swing.JTextArea();
        Cmmit = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        ClieLab = new javax.swing.JTextArea();
        jLabel61 = new javax.swing.JLabel();
        DDate = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        Pan_Doctor = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        TreatsList = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        ClHist1 = new javax.swing.JRadioButton();
        Cl1 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        PharmSerchID = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        ClDrg1 = new javax.swing.JRadioButton();
        RstTbl = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        ClID = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        Cldat = new javax.swing.JTextField();
        ClTim = new javax.swing.JTextField();
        ClNam = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        ClCond = new javax.swing.JTextArea();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        ClDiag = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        PhrmView = new javax.swing.JTextArea();
        PharmTerm = new javax.swing.JButton();
        Bck3 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane23 = new javax.swing.JScrollPane();
        tbl_Costos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        Pan_Lab = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        Pan_OfferDrugs = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        Medico = new javax.swing.JTextArea();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        b1 = new javax.swing.JTextField();
        ComitDrug = new javax.swing.JButton();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        a1 = new javax.swing.JTextField();
        a2 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        DruCorna = new javax.swing.JTextArea();
        BckDoc = new javax.swing.JButton();
        Dr1 = new javax.swing.JComboBox<>();
        DrList = new javax.swing.JLabel();
        WrtFile = new javax.swing.JLabel();
        Nott = new javax.swing.JLabel();
        Heda = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        SysLgut = new javax.swing.JLabel();
        SysDash = new javax.swing.JLabel();
        Pano = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Plogo.setBackground(new java.awt.Color(204, 255, 204));

        jLabel77.setText("jLabel77");

        jLabel78.setText("jLabel78");

        jLabel79.setText("jLabel79");

        StckC.setText("jLabel80");

        StckLv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/dialog-close-2.png"))); // NOI18N
        StckLv.setText("Levels");

        RefshDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/view-refresh-4.png"))); // NOI18N
        RefshDet.setToolTipText("Recheck Level");
        RefshDet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RefshDetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PlogoLayout = new javax.swing.GroupLayout(Plogo);
        Plogo.setLayout(PlogoLayout);
        PlogoLayout.setHorizontalGroup(
            PlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StckLv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
            .addGroup(PlogoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlogoLayout.createSequentialGroup()
                        .addGroup(PlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77)
                            .addComponent(jLabel78)
                            .addComponent(jLabel79))
                        .addContainerGap(294, Short.MAX_VALUE))
                    .addGroup(PlogoLayout.createSequentialGroup()
                        .addComponent(RefshDet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StckC, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        PlogoLayout.setVerticalGroup(
            PlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlogoLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel77)
                .addGap(39, 39, 39)
                .addComponent(jLabel78)
                .addGap(29, 29, 29)
                .addComponent(jLabel79)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(StckLv, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(StckC)
                    .addComponent(RefshDet))
                .addGap(36, 36, 36))
        );

        jLabel1.setBackground(new java.awt.Color(255, 51, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/14.png"))); // NOI18N

        jLabel2.setText("Login as");

        LogLev.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<-Select->", "Admin", "Doctor", "Pharmacist" }));

        Usrnm.setToolTipText("");

        Pwd.setToolTipText("");

        jLabel3.setText("Username");

        jLabel4.setText("Password");

        ProcidLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/go-next-5.png"))); // NOI18N
        ProcidLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcidLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PlogicLayout = new javax.swing.GroupLayout(Plogic);
        Plogic.setLayout(PlogicLayout);
        PlogicLayout.setHorizontalGroup(
            PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlogicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlogicLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1)
                        .addGap(71, 71, 71)
                        .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LogLev, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PlogicLayout.createSequentialGroup()
                        .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(46, 46, 46)
                        .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Usrnm)
                            .addComponent(Pwd, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))))
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlogicLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProcidLog, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlogicLayout.setVerticalGroup(
            PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlogicLayout.createSequentialGroup()
                .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlogicLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LogLev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PlogicLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Usrnm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(28, 28, 28)
                .addGroup(PlogicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(54, 54, 54)
                .addComponent(ProcidLog, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        Pan_Config.setBackground(new java.awt.Color(204, 204, 204));
        Pan_Config.setMaximumSize(null);
        Pan_Config.setMinimumSize(new java.awt.Dimension(879, 406));
        Pan_Config.setName(""); // NOI18N
        Pan_Config.setPreferredSize(new java.awt.Dimension(879, 406));
        Pan_Config.setRequestFocusEnabled(false);

        Adminey.setBackground(new java.awt.Color(228, 232, 201));
        Adminey.setMaximumSize(null);
        Adminey.setMinimumSize(new java.awt.Dimension(879, 406));
        Adminey.setName(""); // NOI18N
        Adminey.setPreferredSize(new java.awt.Dimension(879, 406));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Search By:"));

        jLabel28.setText("Name");

        StockIDSrch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StockIDSrchKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                StockIDSrchKeyReleased(evt);
            }
        });

        jLabel27.setText("Stock ID");

        StockNmSrch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                StockNmSrchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(44, 44, 44)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(StockIDSrch)
                    .addComponent(StockNmSrch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(StockIDSrch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(StockNmSrch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        StockCurr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(StockCurr);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        Stockies.addTab("Current Stock", jPanel8);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock Info"));

        jLabel30.setText("Date");

        StockID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StockIDMouseClicked(evt);
            }
        });
        StockID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockIDActionPerformed(evt);
            }
        });

        jLabel29.setText("Name");

        jLabel35.setText("Side Effects");

        jLabel31.setText("Time");

        StockDt.setEditable(false);
        StockDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockDtActionPerformed(evt);
            }
        });

        jLabel32.setText("Company");

        StockTm.setEditable(false);

        jLabel33.setText("Unit");

        jLabel34.setText("Stock ID");

        StockAdd.setText("Add");
        StockAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockAddActionPerformed(evt);
            }
        });

        StockEdit.setText("Edit");
        StockEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockEditActionPerformed(evt);
            }
        });

        StockDel.setText("Delete");
        StockDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockDelActionPerformed(evt);
            }
        });

        StockDesc.setColumns(20);
        StockDesc.setRows(5);
        jScrollPane15.setViewportView(StockDesc);

        jLabel63.setText("Drug Description");

        jLabel64.setText("Drug Scope");

        StockScope.setColumns(20);
        StockScope.setRows(5);
        jScrollPane16.setViewportView(StockScope);

        jLabel75.setText("Unit Price");

        StockUPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StockUPriceKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(StockAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StockEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StockDel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StockEff)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(StockNm, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(StockComp, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(StockTm)
                                    .addComponent(StockDt, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(StockUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(StockID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(StockUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel63)
                            .addComponent(jLabel64)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                                    .addComponent(jScrollPane15))
                                .addGap(3, 3, 3))))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(StockNm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(StockDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(StockTm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(StockComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(StockUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel75)
                            .addComponent(StockUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(StockID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel64)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(StockEff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StockAdd)
                    .addComponent(StockEdit)
                    .addComponent(StockDel)))
        );

        StockList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        StockList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StockListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(StockList);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Stockies.addTab("Regista Stock", jPanel9);

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Search By"));

        tbl_RfSerch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_RfSerch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_RfSerchMouseClicked(evt);
            }
        });
        jScrollPane26.setViewportView(tbl_RfSerch);

        jLabel98.setText("StockID");

        RfSerch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                RfSerchKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RfSerchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel98)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RfSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RfSerch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel99.setText("Name");

        tbl_RfList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_RfList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_RfListMouseClicked(evt);
            }
        });
        jScrollPane27.setViewportView(tbl_RfList);

        RfNam.setEditable(false);
        RfNam.setEnabled(false);

        jLabel100.setText("StockID");

        RfStckID.setEditable(false);
        RfStckID.setEnabled(false);

        jLabel101.setText("Available");

        RfAlble.setEditable(false);
        RfAlble.setEnabled(false);

        jLabel102.setText("Increase By");

        RfBy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                RfByKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RfByKeyReleased(evt);
            }
        });

        jLabel103.setText("Stock Level");

        RfNwLv.setEditable(false);
        RfNwLv.setEnabled(false);

        RfADD.setText("Add");
        RfADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RfADDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel99)
                            .addComponent(RfNam, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel100)
                            .addComponent(RfStckID, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel101)
                            .addComponent(RfAlble, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel102)
                            .addComponent(RfBy, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel103)
                            .addComponent(RfNwLv, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(RfADD)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel99)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RfNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel100)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RfStckID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel101)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RfAlble, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel102)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RfBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel103)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RfNwLv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RfADD)
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        Stockies.addTab("Refills", jPanel34);

        jLabel97.setText("Sort By:");

        tbl_Statis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane25.setViewportView(tbl_Statis);

        StatCrit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<Select>>", "Most Sold", "Most Profitable" }));
        StatCrit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatCritActionPerformed(evt);
            }
        });

        StatParam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<Select>>", "Week", "Today" }));
        StatParam.setEnabled(false);

        StatAsses.setText("Assess");
        StatAsses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatAssesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane25, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel97)
                .addGap(26, 26, 26)
                .addComponent(StatCrit, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StatParam, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(StatAsses)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(StatCrit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StatParam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StatAsses))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
        );

        Stockies.addTab("Statistics", jPanel30);

        javax.swing.GroupLayout StocksLayout = new javax.swing.GroupLayout(Stocks);
        Stocks.setLayout(StocksLayout);
        StocksLayout.setHorizontalGroup(
            StocksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Stockies)
        );
        StocksLayout.setVerticalGroup(
            StocksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Stockies, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        Adminey.addTab("Stock Info", Stocks);

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Staff Info"));

        jLabel85.setText("Name");

        jLabel86.setText("ID No");

        jLabel87.setText("Username");

        jLabel88.setText("Password");

        jLabel89.setText("Staff ID");

        jLabel90.setText("Contact");

        jLabel91.setText("Staff Level");

        StaffLvl1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Doctor", "Pharmacist", "Admin" }));

        StaffCont1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffCont1KeyTyped(evt);
            }
        });

        StaffID1.setEditable(false);
        StaffID1.setToolTipText("");
        StaffID1.setEnabled(false);
        StaffID1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffID1KeyTyped(evt);
            }
        });

        StaffPwd1.setToolTipText("");

        StaffUsr1.setToolTipText("");

        StaffIDNo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffIDNo1KeyTyped(evt);
            }
        });

        StaffUpdt.setText("Update");
        StaffUpdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StaffUpdtActionPerformed(evt);
            }
        });

        StaffDel.setText("Delete");
        StaffDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StaffDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel91)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(StaffLvl1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel90)
                                .addGap(43, 43, 43)
                                .addComponent(StaffCont1))
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(2, 2, 2))
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)))
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(StaffPwd1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(StaffUsr1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StaffIDNo1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StaffNm1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StaffID1)))))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(StaffUpdt)
                        .addGap(29, 29, 29)
                        .addComponent(StaffDel)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(StaffNm1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(StaffIDNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(StaffUsr1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(StaffPwd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(StaffID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(StaffCont1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StaffLvl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StaffUpdt)
                    .addComponent(StaffDel))
                .addContainerGap())
        );

        tbl_Staff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_Staff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_StaffMouseClicked(evt);
            }
        });
        jScrollPane21.setViewportView(tbl_Staff);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Users", jPanel26);

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Details"));

        jLabel92.setText("Name");

        jLabel93.setText("ID No");

        ClientIDNat1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientIDNat1KeyTyped(evt);
            }
        });

        jLabel94.setText("Client ID");

        ClientID1.setEditable(false);
        ClientID1.setToolTipText("");
        ClientID1.setEnabled(false);
        ClientID1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientID1KeyTyped(evt);
            }
        });

        jLabel95.setText("Region");

        ClientReg1.setToolTipText("");

        jLabel96.setText("Contacts");

        ClientCont1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientCont1KeyTyped(evt);
            }
        });

        ClienUpdt.setText("Update");
        ClienUpdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClienUpdtActionPerformed(evt);
            }
        });

        ClientDel.setText("Delete");
        ClientDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientDelActionPerformed(evt);
            }
        });

        Bck10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/arrow-turn-left.png"))); // NOI18N
        Bck10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Bck10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(ClientNm1))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ClientIDNat1))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClientCont1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ClientID1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ClientReg1, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ClienUpdt)
                .addGap(18, 18, 18)
                .addComponent(ClientDel)
                .addGap(78, 78, 78)
                .addComponent(Bck10)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(ClientNm1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(ClientIDNat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(ClientID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(ClientReg1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(ClientCont1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ClienUpdt)
                        .addComponent(ClientDel))
                    .addComponent(Bck10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbl_ClientEdit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_ClientEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ClientEditMouseClicked(evt);
            }
        });
        jScrollPane22.setViewportView(tbl_ClientEdit);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
            .addComponent(jScrollPane22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Clients", jPanel27);

        tbl_Logs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane30.setViewportView(tbl_Logs);

        buttonGroup3.add(Action_Adm);
        Action_Adm.setText("Admin");
        Action_Adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Action_AdmActionPerformed(evt);
            }
        });

        buttonGroup3.add(Action_Staf);
        Action_Staf.setText("Staff");
        Action_Staf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Action_StafActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(Action_Adm)
                .addGap(46, 46, 46)
                .addComponent(Action_Staf)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Action_Adm)
                    .addComponent(Action_Staf))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Activities", jPanel5);

        javax.swing.GroupLayout UsrSttingLayout = new javax.swing.GroupLayout(UsrStting);
        UsrStting.setLayout(UsrSttingLayout);
        UsrSttingLayout.setHorizontalGroup(
            UsrSttingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        UsrSttingLayout.setVerticalGroup(
            UsrSttingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        Adminey.addTab("User Settings", UsrStting);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 869, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );

        Others.addTab("Appointment", jPanel32);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("Set Low Stock Levels");

        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel105.setText("Once The Stock Reaches This Level You Will Be Notified");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("All Drugs Indexed"));

        tbl_StLvs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_StLvs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_StLvsMouseClicked(evt);
            }
        });
        jScrollPane29.setViewportView(tbl_StLvs);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        jLabel106.setText("Name");

        jLabel110.setText("Refresh");
        jLabel110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel110MouseClicked(evt);
            }
        });

        jLabel108.setText("Set Level");

        StLvApply.setText("Apply");
        StLvApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StLvApplyActionPerformed(evt);
            }
        });

        StLvRfsh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/view-refresh-4.png"))); // NOI18N
        StLvRfsh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StLvRfshMouseClicked(evt);
            }
        });

        StLvSID.setEditable(false);

        StLvName.setEditable(false);

        jLabel107.setText("Stock ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StLvName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(StLvSID)
                    .addComponent(StLvNum)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(StLvApply, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(StLvRfsh)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel110))
                                    .addComponent(jLabel106)
                                    .addComponent(jLabel107)
                                    .addComponent(jLabel108))
                                .addGap(0, 4, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel106)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StLvName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel107)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StLvSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel108)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StLvNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(StLvApply)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(StLvRfsh)
                    .addComponent(jLabel110))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(22, 22, 22))
            .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel105)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );

        Others.addTab("Notifications", jPanel33);

        jLabel81.setText("Internet Connection");

        ConView.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "True", "False" }));
        ConView.setEnabled(false);
        ConView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ConViewMouseEntered(evt);
            }
        });

        jLabel109.setText("Dump The Database");

        ConCheck.setText("Start");
        ConCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConCheckActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));

        ConDump.setEditable(false);
        ConDump.setColumns(20);
        ConDump.setRows(5);
        jScrollPane24.setViewportView(ConDump);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton4.setText("jButton4");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel109, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ConView, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ConCheck)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jButton4)))
                .addGap(0, 39, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(ConView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel109)
                    .addComponent(ConCheck))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jButton4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );

        Others.addTab("Back Up", jPanel4);

        javax.swing.GroupLayout OtherrsLayout = new javax.swing.GroupLayout(Otherrs);
        Otherrs.setLayout(OtherrsLayout);
        OtherrsLayout.setHorizontalGroup(
            OtherrsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Others)
        );
        OtherrsLayout.setVerticalGroup(
            OtherrsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Others)
        );

        Adminey.addTab("Others", Otherrs);

        jLabel20.setText("Name");

        jLabel21.setText("ID No");

        jLabel22.setText("Username");

        jLabel23.setText("Password");

        jLabel24.setText("Staff ID");

        StaffID.setToolTipText("");
        StaffID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StaffIDMouseClicked(evt);
            }
        });
        StaffID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StaffIDActionPerformed(evt);
            }
        });
        StaffID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffIDKeyTyped(evt);
            }
        });

        jLabel25.setText("Staff Level");

        jDesktopPane1.setBackground(new java.awt.Color(153, 255, 153));

        ImgLab1.setToolTipText("");

        jDesktopPane1.setLayer(ImgLab1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ImgLab1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ImgLab1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addContainerGap())
        );

        ImgPat.setToolTipText("");

        NwUsr.setText("Insert");
        NwUsr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NwUsrActionPerformed(evt);
            }
        });

        StaffUsr.setToolTipText("");

        StaffPwd.setToolTipText("");

        StaffLvl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Doctor", "Pharmacist", "Admin" }));

        Bck2.setText("Back");
        Bck2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bck2ActionPerformed(evt);
            }
        });

        StaffIDNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffIDNoKeyTyped(evt);
            }
        });

        InsrtImg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/edit-user.png"))); // NOI18N
        InsrtImg1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InsrtImg1MouseClicked(evt);
            }
        });

        jLabel26.setText("Contact");

        StaffCont.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StaffContKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout NwStaffLayout = new javax.swing.GroupLayout(NwStaff);
        NwStaff.setLayout(NwStaffLayout);
        NwStaffLayout.setHorizontalGroup(
            NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwStaffLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(NwStaffLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(NwUsr)
                        .addGap(18, 18, 18)
                        .addComponent(Bck2))
                    .addGroup(NwStaffLayout.createSequentialGroup()
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(NwStaffLayout.createSequentialGroup()
                                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, NwStaffLayout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addGap(43, 43, 43)
                                        .addComponent(StaffCont)
                                        .addGap(3, 3, 3))
                                    .addGroup(NwStaffLayout.createSequentialGroup()
                                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwStaffLayout.createSequentialGroup()
                                                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(2, 2, 2))
                                            .addGroup(NwStaffLayout.createSequentialGroup()
                                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)))
                                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(StaffNm, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(StaffUsr)
                                                .addComponent(StaffID, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                                .addComponent(StaffPwd, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(StaffIDNo)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(NwStaffLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(23, 23, 23)
                                .addComponent(StaffLvl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(20, 20, 20)
                                .addComponent(ImgPat, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(InsrtImg1)
                        .addGap(58, 58, 58)))
                .addGap(26, 26, 26))
        );
        NwStaffLayout.setVerticalGroup(
            NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NwStaffLayout.createSequentialGroup()
                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NwStaffLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(StaffNm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(StaffIDNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(StaffUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(StaffPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(StaffID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(StaffCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwStaffLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(InsrtImg1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ImgPat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(StaffLvl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)))
                .addGap(18, 18, 18)
                .addGroup(NwStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NwUsr)
                    .addComponent(Bck2))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        Adminey.addTab("Add Users", NwStaff);

        jLabel45.setText("Name");

        jLabel46.setText("ID No");

        ClientIDNat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientIDNatKeyTyped(evt);
            }
        });

        jLabel48.setText("Client ID");

        ClientID.setToolTipText("");
        ClientID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientIDMouseClicked(evt);
            }
        });
        ClientID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientIDKeyTyped(evt);
            }
        });

        jLabel58.setText("Region");

        ClientReg.setToolTipText("");

        jLabel59.setText("Date");

        ClientDt.setEditable(false);
        ClientDt.setToolTipText("");
        ClientDt.setEnabled(false);

        jLabel60.setText("Contacts");

        jDesktopPane2.setBackground(new java.awt.Color(153, 255, 153));

        jDesktopPane2.setLayer(ImgLab2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ImgLab2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ImgLab2, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        ClientAdd.setText("Add Client");
        ClientAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientAddActionPerformed(evt);
            }
        });

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("Client Info");

        ClientCont.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientContKeyTyped(evt);
            }
        });

        InsrtImg3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/edit-user.png"))); // NOI18N
        InsrtImg3.setToolTipText("Add Image");
        InsrtImg3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InsrtImg3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout NwClientLayout = new javax.swing.GroupLayout(NwClient);
        NwClient.setLayout(NwClientLayout);
        NwClientLayout.setHorizontalGroup(
            NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwClientLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ClientAdd)
                .addGap(156, 156, 156))
            .addGroup(NwClientLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(NwClientLayout.createSequentialGroup()
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(ClientNm))
                    .addGroup(NwClientLayout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ClientIDNat))
                    .addGroup(NwClientLayout.createSequentialGroup()
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClientID, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ClientReg, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ClientDt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(NwClientLayout.createSequentialGroup()
                                .addComponent(ClientCont, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwClientLayout.createSequentialGroup()
                .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(NwClientLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ImgPat2, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(InsrtImg3)))
                .addGap(28, 28, 28))
        );
        NwClientLayout.setVerticalGroup(
            NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NwClientLayout.createSequentialGroup()
                .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NwClientLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(ClientNm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(ClientIDNat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(ClientID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(ClientReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel59)
                            .addComponent(ClientDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel60)
                            .addComponent(ClientCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(NwClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(InsrtImg3)
                    .addComponent(ImgPat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(ClientAdd)
                .addContainerGap())
        );

        Adminey.addTab("Add Client", NwClient);

        javax.swing.GroupLayout Pan_ConfigLayout = new javax.swing.GroupLayout(Pan_Config);
        Pan_Config.setLayout(Pan_ConfigLayout);
        Pan_ConfigLayout.setHorizontalGroup(
            Pan_ConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Adminey, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Pan_ConfigLayout.setVerticalGroup(
            Pan_ConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Adminey, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Pan_Home.setMaximumSize(null);
        Pan_Home.setMinimumSize(new java.awt.Dimension(879, 406));
        Pan_Home.setName(""); // NOI18N

        Cates.setBackground(new java.awt.Color(204, 204, 204));
        Cates.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Category To Proceed", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N
        Cates.setMaximumSize(null);
        Cates.setMinimumSize(new java.awt.Dimension(879, 406));
        Cates.setName(""); // NOI18N
        Cates.setPreferredSize(new java.awt.Dimension(857, 342));

        RedConsl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/utilities-antivirus.png"))); // NOI18N
        RedConsl.setToolTipText("Doctoring");
        RedConsl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedConslActionPerformed(evt);
            }
        });

        jLabel5.setText("Consultation");

        jLabel7.setText("The Doctor");

        jLabel6.setText("One On One With ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RedConsl, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RedConsl, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        jLabel9.setText("Intraveneous Dru-");

        RedInj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/virus.png"))); // NOI18N
        RedInj.setToolTipText("Intravenous Drugs");
        RedInj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedInjActionPerformed(evt);
            }
        });

        jLabel10.setText("gs");

        jLabel8.setText("Injection Room");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RedInj, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RedInj, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jLabel11.setText("Laboratory");

        jLabel13.setText("jLabel7");

        RedLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/applications-science-2.png"))); // NOI18N
        RedLab.setToolTipText("Client Tests");
        RedLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedLabActionPerformed(evt);
            }
        });

        jLabel12.setText("Specimen Analysis");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RedLab, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RedLab, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel13)))
                .addContainerGap())
        );

        jLabel15.setText("Offer Drugs Recom-");

        RedPharm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/mail-mark-task.png"))); // NOI18N
        RedPharm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedPharmActionPerformed(evt);
            }
        });

        jLabel14.setText("Pharmacy");

        jLabel16.setText("mended By Doctor");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RedPharm, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RedPharm, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addContainerGap())
        );

        jLabel19.setText("tion");

        RedMore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/system-run-3.png"))); // NOI18N
        RedMore.setToolTipText("Pending");
        RedMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedMoreActionPerformed(evt);
            }
        });

        jLabel17.setText("More");

        jLabel18.setText("System Configura-");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RedMore, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RedMore, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        javax.swing.GroupLayout CatesLayout = new javax.swing.GroupLayout(Cates);
        Cates.setLayout(CatesLayout);
        CatesLayout.setHorizontalGroup(
            CatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CatesLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        CatesLayout.setVerticalGroup(
            CatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Pan_HomeLayout = new javax.swing.GroupLayout(Pan_Home);
        Pan_Home.setLayout(Pan_HomeLayout);
        Pan_HomeLayout.setHorizontalGroup(
            Pan_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Cates, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
        );
        Pan_HomeLayout.setVerticalGroup(
            Pan_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan_HomeLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(Cates, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        Pan_Consult.setBackground(new java.awt.Color(204, 204, 204));
        Pan_Consult.setToolTipText("");

        jTabbedPane1.setBackground(new java.awt.Color(228, 232, 201));

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Search By"));

        jLabel49.setText("Name");

        jLabel47.setText("Client ID");

        ClientSerch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ClientSerchKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ClientSerchKeyReleased(evt);
            }
        });

        jLabel65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/view-choose-2.png"))); // NOI18N

        RfshClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/view-refresh-4.png"))); // NOI18N
        RfshClient.setToolTipText("List Clients");
        RfshClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RfshClientMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel49))
                .addGap(37, 37, 37)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ClientNmSrch, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(ClientSerch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RfshClient, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(35, 35, 35))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(ClientSerch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RfshClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel49)
                        .addComponent(ClientNmSrch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel65))
                .addGap(0, 24, Short.MAX_VALUE))
        );

        ClientList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        ClientList.setMaximumSize(new java.awt.Dimension(300, 64));
        ClientList.setMinimumSize(new java.awt.Dimension(300, 64));
        ClientList.setName(""); // NOI18N
        ClientList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientListMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ClientListMouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(ClientList);

        TrtCond.setEditable(false);
        TrtCond.setColumns(20);
        TrtCond.setRows(5);
        TrtCond.setEnabled(false);
        jScrollPane4.setViewportView(TrtCond);

        TrtDiag.setEditable(false);
        TrtDiag.setColumns(20);
        TrtDiag.setRows(5);
        TrtDiag.setEnabled(false);
        jScrollPane5.setViewportView(TrtDiag);

        jLabel38.setText("Lab Report");

        TrtLab.setEditable(false);
        TrtLab.setColumns(20);
        TrtLab.setRows(5);
        TrtLab.setEnabled(false);
        jScrollPane6.setViewportView(TrtLab);

        jLabel39.setText("Prescribed Drugs");

        TrtDrug.setEditable(false);
        TrtDrug.setColumns(20);
        TrtDrug.setRows(5);
        TrtDrug.setEnabled(false);
        jScrollPane7.setViewportView(TrtDrug);

        Bck1.setText("Back");
        Bck1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bck1ActionPerformed(evt);
            }
        });

        jLabel66.setText("Diagnosis");

        jLabel36.setText("Condition");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(Bck1)
                                .addGap(21, 21, 21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(0, 74, Short.MAX_VALUE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Bck1)))
                        .addContainerGap())
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Client History", jPanel16);

        jDesktopPane5.setBackground(new java.awt.Color(204, 255, 204));

        ClinImg.setMaximumSize(new java.awt.Dimension(214, 210));
        ClinImg.setMinimumSize(new java.awt.Dimension(214, 210));
        ClinImg.setName(""); // NOI18N
        ClinImg.setPreferredSize(new java.awt.Dimension(214, 210));

        jDesktopPane5.setLayer(ClinImg, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane5Layout = new javax.swing.GroupLayout(jDesktopPane5);
        jDesktopPane5.setLayout(jDesktopPane5Layout);
        jDesktopPane5Layout.setHorizontalGroup(
            jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClinImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane5Layout.setVerticalGroup(
            jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClinImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel41.setText("Name");

        jLabel42.setText("Client ID");

        ClientNam.setEditable(false);
        ClientNam.setEnabled(false);

        ClienID.setEditable(false);
        ClienID.setEnabled(false);

        jLabel43.setText("Nat. ID");

        ClientNat.setEditable(false);
        ClientNat.setEnabled(false);

        jLabel44.setText(" Condition (Now)");

        ClieCond.setColumns(20);
        ClieCond.setRows(5);
        jScrollPane8.setViewportView(ClieCond);

        jLabel50.setText("Diagnosis");

        ClieDaig.setColumns(20);
        ClieDaig.setRows(5);
        jScrollPane9.setViewportView(ClieDaig);

        jLabel51.setText("Lab Report");

        jLabel52.setText("Medication");

        ClieMed.setEditable(false);
        ClieMed.setColumns(20);
        ClieMed.setRows(5);
        ClieMed.setEnabled(false);
        ClieMed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClieMedMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(ClieMed);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary"));

        LogBar.setColumns(20);
        LogBar.setRows(5);
        LogBar.setEnabled(false);
        jScrollPane12.setViewportView(LogBar);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        Cmmit.setText("Commit");
        Cmmit.setEnabled(false);
        Cmmit.setName(""); // NOI18N
        Cmmit.setPreferredSize(new java.awt.Dimension(86, 30));
        Cmmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmmitActionPerformed(evt);
            }
        });

        ClieLab.setColumns(20);
        ClieLab.setRows(5);
        jScrollPane10.setViewportView(ClieLab);

        jLabel61.setText("Date");

        DDate.setEditable(false);
        DDate.setEnabled(false);

        jButton2.setText("More");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDesktopPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel43))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel61)
                                .addGap(43, 43, 43)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(DDate, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(ClientNat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(ClientNam, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClienID, javax.swing.GroupLayout.Alignment.LEADING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel50)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(19, 19, 19)
                        .addComponent(Cmmit, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(4, 4, 4))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel52)
                                .addGap(17, 17, 17)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane10)
                            .addComponent(jScrollPane11))))
                .addGap(32, 32, 32)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jDesktopPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(ClientNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(ClienID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(ClientNat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel61)
                            .addComponent(DDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addComponent(Cmmit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Diagnose", jPanel17);

        javax.swing.GroupLayout Pan_ConsultLayout = new javax.swing.GroupLayout(Pan_Consult);
        Pan_Consult.setLayout(Pan_ConsultLayout);
        Pan_ConsultLayout.setHorizontalGroup(
            Pan_ConsultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        Pan_ConsultLayout.setVerticalGroup(
            Pan_ConsultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        Pan_Doctor.setBackground(new java.awt.Color(204, 204, 204));
        Pan_Doctor.setMinimumSize(new java.awt.Dimension(947, 425));
        Pan_Doctor.setName(""); // NOI18N
        Pan_Doctor.setPreferredSize(new java.awt.Dimension(947, 425));

        jTabbedPane2.setBackground(new java.awt.Color(228, 232, 201));
        jTabbedPane2.setMinimumSize(new java.awt.Dimension(947, 425));
        jTabbedPane2.setName(""); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(947, 425));

        TreatsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TreatsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TreatsListMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(TreatsList);

        buttonGroup1.add(ClHist1);
        ClHist1.setText("History");

        buttonGroup1.add(Cl1);
        Cl1.setText("Client List");

        jTextField2.setToolTipText("");

        jLabel83.setText("Name");

        PharmSerchID.setToolTipText("");
        PharmSerchID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PharmSerchIDKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                PharmSerchIDKeyReleased(evt);
            }
        });

        jLabel82.setText("Client ID");

        buttonGroup1.add(ClDrg1);
        ClDrg1.setText("Drugs");

        RstTbl.setText("Reset");
        RstTbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RstTblActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel82)
                    .addComponent(jLabel83))
                .addGap(47, 47, 47)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2)
                    .addComponent(PharmSerchID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addComponent(Cl1)
                .addGap(34, 34, 34)
                .addComponent(ClHist1)
                .addGap(18, 18, 18)
                .addComponent(ClDrg1)
                .addGap(18, 18, 18)
                .addComponent(RstTbl)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel82)
                            .addComponent(PharmSerchID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel83)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RstTbl)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Cl1)
                            .addComponent(ClHist1)
                            .addComponent(ClDrg1))
                        .addGap(16, 16, 16)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 214, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Search", jPanel22);

        jLabel56.setText("Time");

        jLabel55.setText("Date");

        ClID.setEditable(false);
        ClID.setToolTipText("");
        ClID.setEnabled(false);

        jLabel53.setText("Name");

        Cldat.setEditable(false);
        Cldat.setToolTipText("");
        Cldat.setEnabled(false);

        ClTim.setEditable(false);
        ClTim.setToolTipText("");
        ClTim.setEnabled(false);

        ClNam.setEditable(false);
        ClNam.setToolTipText("");
        ClNam.setEnabled(false);

        jLabel54.setText("Client ID");

        jLabel57.setText("Condition");

        ClCond.setEditable(false);
        ClCond.setColumns(20);
        ClCond.setRows(5);
        ClCond.setEnabled(false);
        jScrollPane19.setViewportView(ClCond);

        jLabel84.setText("Diagnosis");

        ClDiag.setEditable(false);
        ClDiag.setColumns(20);
        ClDiag.setRows(5);
        ClDiag.setEnabled(false);
        jScrollPane20.setViewportView(ClDiag);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel84))
                .addGap(4, 4, 4)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20)
                    .addComponent(ClID, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClNam, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Cldat, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClTim, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(ClNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(ClID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(Cldat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(ClTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));

        PhrmView.setColumns(20);
        PhrmView.setRows(5);
        PhrmView.setEnabled(false);
        jScrollPane14.setViewportView(PhrmView);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14)
        );

        PharmTerm.setText("Terminate");

        Bck3.setText("Back");
        Bck3.setEnabled(false);
        Bck3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bck3ActionPerformed(evt);
            }
        });

        jButton3.setText("Next");

        tbl_Costos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane23.setViewportView(tbl_Costos);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(PharmTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(Bck3)
                        .addContainerGap())
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Bck3)
                    .addComponent(jButton3)
                    .addComponent(PharmTerm))
                .addContainerGap())
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Doctor View", jPanel23);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane28.setViewportView(jTable1);

        buttonGroup4.add(jRadioButton1);
        jRadioButton1.setText("Doctor");

        buttonGroup4.add(jRadioButton2);
        jRadioButton2.setText("Pharmacist");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane28)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(jRadioButton1)
                .addGap(33, 33, 33)
                .addComponent(jRadioButton2)
                .addContainerGap(458, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Drugs", jPanel1);

        javax.swing.GroupLayout Pan_DoctorLayout = new javax.swing.GroupLayout(Pan_Doctor);
        Pan_Doctor.setLayout(Pan_DoctorLayout);
        Pan_DoctorLayout.setHorizontalGroup(
            Pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Pan_DoctorLayout.setVerticalGroup(
            Pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("Lab Section");

        javax.swing.GroupLayout Pan_LabLayout = new javax.swing.GroupLayout(Pan_Lab);
        Pan_Lab.setLayout(Pan_LabLayout);
        Pan_LabLayout.setHorizontalGroup(
            Pan_LabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
        );
        Pan_LabLayout.setVerticalGroup(
            Pan_LabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan_LabLayout.createSequentialGroup()
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 394, Short.MAX_VALUE))
        );

        Medico.setEditable(false);
        Medico.setColumns(20);
        Medico.setRows(5);
        Medico.setEnabled(false);
        Medico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MedicoMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(Medico);

        jLabel70.setText("Medicine");

        jLabel71.setText("Dosage");

        jLabel72.setText("Duration");

        b1.setToolTipText("");
        b1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                b1KeyTyped(evt);
            }
        });

        ComitDrug.setText("Commit");
        ComitDrug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComitDrugActionPerformed(evt);
            }
        });

        jLabel73.setText("Days");

        jLabel74.setText("x");

        a1.setToolTipText("");
        a1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                a1KeyTyped(evt);
            }
        });

        a2.setToolTipText("");
        a2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                a2KeyTyped(evt);
            }
        });

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Drugs Corner"));

        DruCorna.setEditable(false);
        DruCorna.setColumns(20);
        DruCorna.setRows(5);
        DruCorna.setEnabled(false);
        jScrollPane18.setViewportView(DruCorna);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane18)
        );

        BckDoc.setText("Back");
        BckDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BckDocActionPerformed(evt);
            }
        });

        Dr1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Select>" }));
        Dr1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Dr1MouseEntered(evt);
            }
        });
        Dr1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Dr1ActionPerformed(evt);
            }
        });

        DrList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/align-vertical-center.png"))); // NOI18N
        DrList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DrListMouseClicked(evt);
            }
        });

        WrtFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/documentation.png"))); // NOI18N
        WrtFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WrtFileMouseClicked(evt);
            }
        });

        Nott.setText("jLabel84");

        javax.swing.GroupLayout Pan_OfferDrugsLayout = new javax.swing.GroupLayout(Pan_OfferDrugs);
        Pan_OfferDrugs.setLayout(Pan_OfferDrugsLayout);
        Pan_OfferDrugsLayout.setHorizontalGroup(
            Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                        .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70)
                            .addComponent(jLabel71)
                            .addComponent(jLabel72))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                        .addComponent(a1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel74))
                                    .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(a2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel73))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE))
                            .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                            .addComponent(Dr1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(36, 36, 36)
                                            .addComponent(DrList))
                                        .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                            .addComponent(BckDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(WrtFile))
                                        .addComponent(Nott, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(ComitDrug, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        Pan_OfferDrugsLayout.setVerticalGroup(
            Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DrList)
                            .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel70)
                                .addComponent(Dr1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pan_OfferDrugsLayout.createSequentialGroup()
                                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(a1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(a2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel71))
                                .addGap(18, 18, 18)
                                .addGroup(Pan_OfferDrugsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel72)
                                    .addComponent(jLabel73))
                                .addGap(32, 32, 32)
                                .addComponent(ComitDrug)
                                .addGap(18, 18, 18)
                                .addComponent(BckDoc))
                            .addComponent(WrtFile))
                        .addGap(18, 18, 18)
                        .addComponent(Nott)
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout CentaLayout = new javax.swing.GroupLayout(Centa);
        Centa.setLayout(CentaLayout);
        CentaLayout.setHorizontalGroup(
            CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CentaLayout.createSequentialGroup()
                .addComponent(Plogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Plogic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 106, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Config, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Consult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Doctor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Lab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_OfferDrugs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CentaLayout.setVerticalGroup(
            CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Plogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(CentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Plogic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Config, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CentaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Pan_Home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Consult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Doctor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_Lab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Pan_OfferDrugs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(Centa, java.awt.BorderLayout.CENTER);

        Heda.setBackground(new java.awt.Color(204, 204, 255));

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Someone Here");

        jLabel40.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Specific Here");

        SysLgut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/system-shutdown-3.png"))); // NOI18N
        SysLgut.setToolTipText("Log Out");
        SysLgut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SysLgutMouseClicked(evt);
            }
        });

        SysDash.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imgs/go-home-4.png"))); // NOI18N
        SysDash.setToolTipText("DashBoard");
        SysDash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SysDashMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout HedaLayout = new javax.swing.GroupLayout(Heda);
        Heda.setLayout(HedaLayout);
        HedaLayout.setHorizontalGroup(
            HedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
            .addGroup(HedaLayout.createSequentialGroup()
                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(SysLgut)
                .addGap(18, 18, 18)
                .addComponent(SysDash)
                .addContainerGap())
        );
        HedaLayout.setVerticalGroup(
            HedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HedaLayout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(SysLgut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SysDash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(Heda, java.awt.BorderLayout.PAGE_START);

        Pano.setBackground(new java.awt.Color(204, 204, 255));
        Pano.setPreferredSize(new java.awt.Dimension(879, 60));

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("Specific Here");

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Contact Developers");

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("0727614575//0786119199");

        javax.swing.GroupLayout PanoLayout = new javax.swing.GroupLayout(Pano);
        Pano.setLayout(PanoLayout);
        PanoLayout.setHorizontalGroup(
            PanoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
            .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanoLayout.setVerticalGroup(
            PanoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanoLayout.createSequentialGroup()
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel68)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel69))
        );

        getContentPane().add(Pano, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProcidLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcidLogActionPerformed
        // TODO add your handling code here:
        String Usr,pwd,lvl;
        Usr=Usrnm.getText().toString();
        pwd=Pwd.getText().toString();
        lvl=LogLev.getSelectedItem().toString();
        
        String sav="SELECT * FROM `tbl_Staff` WHERE `Username`= '"+Usr+"' AND `Password`= '"+pwd+"' AND `Level`= '"+lvl+"' ";
               
        try {
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                SysDash.setEnabled(Boolean.TRUE);
                stats=1;
                if (LogLev.getSelectedIndex()==1) {
                    lvpr=1;
                    Pan_Config.setVisible(Boolean.FALSE);
                    Plogo.setVisible(Boolean.FALSE);
                    Plogic.setVisible(Boolean.FALSE);
                    Pan_Home.setVisible(Boolean.TRUE);
                    Pan_Consult.setVisible(Boolean.FALSE);
                    Pan_Doctor.setVisible(Boolean.FALSE);
                    Pan_Lab.setVisible(Boolean.FALSE);
                    Pan_OfferDrugs.setVisible(Boolean.FALSE);
                    
                    RedConsl.setEnabled(Boolean.TRUE);
                    RedInj.setEnabled(Boolean.TRUE);
                    RedLab.setEnabled(Boolean.TRUE);
                    RedPharm.setEnabled(Boolean.TRUE);
                    RedMore.setEnabled(Boolean.TRUE);
                }
                if (LogLev.getSelectedIndex()==2) {
                    lvpr=2;
                    Pan_Home.setVisible(Boolean.FALSE);
                    Plogo.setVisible(Boolean.FALSE);
                    Plogic.setVisible(Boolean.FALSE);
                    Pan_Config.setVisible(Boolean.FALSE);
                    Pan_Consult.setVisible(Boolean.TRUE);
                    Pan_Doctor.setVisible(Boolean.FALSE);
                    Pan_Lab.setVisible(Boolean.FALSE);
                    Pan_OfferDrugs.setVisible(Boolean.FALSE);
                    
                    RedConsl.setEnabled(Boolean.TRUE);
                    RedInj.setEnabled(Boolean.FALSE);
                    RedLab.setEnabled(Boolean.FALSE);
                    RedPharm.setEnabled(Boolean.FALSE);
                    RedMore.setEnabled(Boolean.FALSE);
                }
                if (LogLev.getSelectedIndex()==3) {
                    lvpr=3;
                    Pan_Home.setVisible(Boolean.FALSE);
                    Plogo.setVisible(Boolean.FALSE);
                    Plogic.setVisible(Boolean.FALSE);
                    Pan_Config.setVisible(Boolean.FALSE);
                    Pan_Consult.setVisible(Boolean.FALSE);
                    Pan_Doctor.setVisible(Boolean.TRUE);
                    Pan_Lab.setVisible(Boolean.FALSE);
                    Pan_OfferDrugs.setVisible(Boolean.FALSE);
                    
                    RedConsl.setEnabled(Boolean.FALSE);
                    RedInj.setEnabled(Boolean.FALSE);
                    RedLab.setEnabled(Boolean.FALSE);
                    RedPharm.setEnabled(Boolean.TRUE);
                    RedMore.setEnabled(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            Pwd.setText(null);
            JOptionPane.showMessageDialog(null, "\nInvalid Credentials");
        }
        Pwd.setText(null);
    }//GEN-LAST:event_ProcidLogActionPerformed

    private void RedConslActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedConslActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.TRUE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_RedConslActionPerformed

    private void Bck2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bck2ActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.TRUE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_Bck2ActionPerformed

    private void Bck1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bck1ActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.TRUE);
        Plogic.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_Bck1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int cl =JOptionPane.showConfirmDialog(this,"Close HighWay Hospice System","Doctor's Help", JOptionPane.YES_NO_OPTION);
        if (cl==JOptionPane.YES_OPTION) {
            System.exit(0);
        }/*else{
            return;
        }*/
        if (cl==JOptionPane.NO_OPTION) {
            remove(cl);
        }
    }//GEN-LAST:event_formWindowClosing

    private void RedInjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedInjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RedInjActionPerformed

    private void RedLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedLabActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.TRUE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_RedLabActionPerformed

    private void RedMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedMoreActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.TRUE);
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_RedMoreActionPerformed

    private void InsrtImg1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InsrtImg1MouseClicked
        // TODO add your handling code here:
        try {
            JFileChooser prip=new JFileChooser();
            FileFilter flft=new FileNameExtensionFilter("Images Only", new String []{"jpg","png","jpeg"});

            prip.setFileFilter(flft);
            prip.addChoosableFileFilter(flft);
            int rtn=prip.showOpenDialog(null);

            if (rtn==JFileChooser.APPROVE_OPTION) {
                File f= new File(prip.getSelectedFile().getAbsolutePath());//prip.getSelectedFile();
                Patt=f.getAbsolutePath();
                ImgPat.setText(Patt);

                ImageIcon stV=new ImageIcon(Patt);
                Image Sd=stV.getImage().getScaledInstance(ImgLab1.getWidth(),ImgLab1.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon im=new ImageIcon(Sd);
                ImgLab1.setIcon(im);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Null or Invalid 'Image' File");
        }
    }//GEN-LAST:event_InsrtImg1MouseClicked

    private void InsrtImg3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InsrtImg3MouseClicked
        // TODO add your handling code here:
        try {
            JFileChooser prip=new JFileChooser();
            FileFilter flft=new FileNameExtensionFilter("Images Only", new String []{"jpg","png","jpeg"});

            prip.setFileFilter(flft);
            prip.addChoosableFileFilter(flft);
            int rtn=prip.showOpenDialog(null);

            if (rtn==JFileChooser.APPROVE_OPTION) {
                File f= new File(prip.getSelectedFile().getAbsolutePath());//prip.getSelectedFile();
                Patt=f.getAbsolutePath();
                ImgPat2.setText(Patt);

                ImageIcon stV=new ImageIcon(Patt);
                Image Sd=stV.getImage().getScaledInstance(ImgLab2.getWidth(),ImgLab2.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon im=new ImageIcon(Sd);
                ImgLab2.setIcon(im);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Null or Invalid 'Image' File");
        }
    }//GEN-LAST:event_InsrtImg3MouseClicked

    private void NwUsrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NwUsrActionPerformed
        // TODO add your handling code here:
        IntOnly();
        try {
            String Kret="INSERT INTO `tbl_Staff` (`Count`,`Name` ,`NationalID` ,`Username` ,`Password` ,`StaffID` ,`Contacts` ,`Level` ,`Avatar` ) VALUES (NULL,?,?,?,?,?,?,?,?)";
            File img=new File(Patt);
            FileInputStream fis=new FileInputStream(img);
            int len=(int)img.length();
            pst= (PreparedStatement) Conn.prepareStatement(Kret);

            pst.setString(1, StaffNm.getText().toString());
            pst.setString(2, StaffIDNo.getText().toString());
            pst.setString(3, StaffUsr.getText().toString());
            pst.setString(4, StaffPwd.getText().toString());
            pst.setString(5, StaffID.getText().toString());
            pst.setString(6, StaffCont.getText().toString());
            pst.setString(7, StaffLvl.getSelectedItem().toString());
            pst.setBinaryStream(8,fis, len);

            pst.executeUpdate();

            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Added New Staff");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }

            JOptionPane.showMessageDialog(null, "New Staff ( "+StaffNm.getText().toString()+" )Succesfully Inserted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "\nCreating NewUser Encounterd An Error");
        }
        
        Poplt();
    }//GEN-LAST:event_NwUsrActionPerformed

    private void StockAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockAddActionPerformed
        // TODO add your handling code here:      
        long daty = Long.valueOf(System.currentTimeMillis());
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(daty);
        hours=sdf1.format(resultdate);
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MMM dd");
        Date resultdate2 = new Date(daty);
        day=sdf2.format(resultdate2);
        
        try {
            String Kret="INSERT INTO `tbl_Stock` (`Count`,`Name` ,`StockDate` ,`StockTime` ,`Company` ,`Unit` ,`UnitPrice`,`StockID` ,`StockDesc` ,`StockScope`,`SideEffect` ) VALUES (NULL,?,?,?,?,?,?,?,?,?,?)";
            pst= (PreparedStatement) Conn.prepareStatement(Kret);

            pst.setString(1, StockNm.getText().toString());
            pst.setString(2, day);
            pst.setString(3, hours);
            pst.setString(4, StockComp.getText().toString());
            pst.setString(5, StockUnit.getText().toString());
            pst.setString(6, StockUPrice.getText().toString());
            pst.setString(7, StockID.getText().toString());
            pst.setString(8, StockDesc.getText().toString());
            pst.setString(9, StockScope.getText().toString());
            pst.setString(10, StockEff.getText().toString());

            pst.executeUpdate();
            
            String Cou="INSERT INTO `tbl_StockLevel` ( `Count`,`Name`,`StockID`,`Avail` ) VALUES (NULL,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(Cou);
                IntOnly();
                pst.setString(1, StockNm.getText().toString());
                pst.setString(2, StockID.getText().toString());
                pst.setInt(3, 0);
                
                pst.executeUpdate();
                
            String InLw="INSERT INTO `tbl_StockLow` ( `Count`,`Name`,`StockID`,`Lows`,`Date` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(InLw);
                IntOnly();
                pst.setString(1, StockNm.getText().toString());
                pst.setString(2, StockID.getText().toString());
                pst.setInt(3, 0);
                pst.setString(4, day);
                
                pst.executeUpdate();

            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Stock Refill");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "New Stock Registered");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            try{
              String cops1 = "SELECT Name,StockDate AS Date,StockTime AS Time,Company,StockID,StockDesc,SideEffect FROM `tbl_Stock`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops1));
              rs = this.pst.executeQuery();
              StockList.setModel(DbUtils.resultSetToTableModel(rs));
            }
        catch (Exception e){
              //JOptionPane.showMessageDialog(null, e + "\nAction Not Allowed Please");
            }

            JOptionPane.showMessageDialog(null, "New Stock ( "+StockNm.getText().toString()+" )Succesfully Inserted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex+"\nRegistering New Stock Element Encounterd An Error");
            Toolkit.getDefaultToolkit().beep();
        }
        
        Poplt();
    }//GEN-LAST:event_StockAddActionPerformed

    private void StockEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockEditActionPerformed
        // TODO add your handling code here:
        
        long daty = Long.valueOf(System.currentTimeMillis());
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(daty);
        hours=sdf1.format(resultdate);
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MMM dd");
        Date resultdate2 = new Date(daty);
        day=sdf2.format(resultdate);
        
        try {
            int ro=StockList.getSelectedRow();
            String cc=StockList.getModel().getValueAt(ro, 0).toString();
            
            String snm = StockNm.getText().toString();
            String scdt = day;
            String sctm = hours;
            String scom = StockComp.getText().toString();
            String scun = StockUnit.getText().toString();
            String scup = StockUPrice.getText().toString();
            String scdes = StockDesc.getText().toString();
            String scop = StockScope.getText().toString();
            String sdeff = StockEff.getText().toString();
            
            //Name,StockDate AS Date,StockTime AS Time,Company,Unit,StockID,StockDesc,StockScope,SideEffect
            String sav="UPDATE `tbl_Stock` SET `Name` = '"+snm+"',`StockDate` ='"+scdt+"',StockTime = '"+sctm+"',Company = '"+scom+"',Unit = '"+scun+"' , UnitPrice = '"+scup+"',StockDesc = '"+scdes+"' ,StockScope = '"+scop+"' ,SideEffect = '"+sdeff+"' WHERE `StockID`= '"+Integer.parseInt(StockID.getText().toString())+"' ";
            
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Stock Editting");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nStock Editing Has Exited Due To An Error");
        }
        
        Poplt();
    }//GEN-LAST:event_StockEditActionPerformed

    private void StockDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockDelActionPerformed
        // TODO add your handling code here:
        try {
            int ro=StockList.getSelectedRow();
            String cc=StockList.getModel().getValueAt(ro, 5).toString();
            String sav="DELETE FROM `tbl_Stock` WHERE `StockID`= ' "+cc+" '";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Stock Deleted");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                System.out.println("StockDelActionPerformed insert"+e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nDeleting The Selected Stock Element Has Exited Due To An Error");
            System.out.println("StockDelActionPerformed "+e.getMessage());
        }
        Poplt();
    }//GEN-LAST:event_StockDelActionPerformed

    private void ClientAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientAddActionPerformed
        // TODO add your handling code here:
        try {
            String Kret="INSERT INTO `tbl_Clients` (`Count`,`Name`,`NationalID`,`ClientID`,`Region`,`Date`,`Contacts`,`Avatar` ) VALUES (NULL,?,?,?,?,?,?,?)";
            File img=new File(Patt);
            FileInputStream fis=new FileInputStream(img);
            int len=(int)img.length();
            pst= (PreparedStatement) Conn.prepareStatement(Kret);

            pst.setString(1, ClientNm.getText().toString());
            pst.setString(2, ClientIDNat.getText().toString());
            pst.setString(3, ClientID.getText().toString());
            pst.setString(4, ClientReg.getText().toString());
            pst.setString(5, ClientDt.getText().toString());
            pst.setString(6, ClientCont.getText().toString());
            pst.setBinaryStream(7,fis, len);

            pst.executeUpdate();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Added New Client");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }

            JOptionPane.showMessageDialog(null, "New Client ( "+ClientNm.getText().toString()+" )Succesfully Inserted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "\nAction Add New Client Has Failed");
            Toolkit.getDefaultToolkit().beep();
        }
        
        Poplt();
    }//GEN-LAST:event_ClientAddActionPerformed

    private void StaffIDNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffIDNoKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_StaffIDNoKeyTyped

    private void StaffContKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffContKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_StaffContKeyTyped

    private void StaffIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffIDKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_StaffIDKeyTyped

    private void StockIDSrchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StockIDSrchKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_StockIDSrchKeyTyped

    private void ClientContKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientContKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_ClientContKeyTyped

    private void ClientIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientIDKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_ClientIDKeyTyped

    private void ClientIDNatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientIDNatKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_ClientIDNatKeyTyped

    private void StockListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StockListMouseClicked
        // TODO add your handling code here:
        try {
            int ro=StockList.getSelectedRow();
            String otrow=StockList.getModel().getValueAt(ro, 4).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Stock` WHERE `StockID`= '"+rws+"' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                String ssns,ssid,ssdt,sstm,sscm,sssc,ssdc,ssun,ssef;
                int sid;
                ssns=rs.getString("Name");
                StockNm.setText(ssns);
                sid=rs.getInt("StockID");
                StockID.setText(String.valueOf(sid));
                ssdt=rs.getString("StockDate");
                StockDt.setText(ssdt);
                sstm=rs.getString("StockTime");
                ssun=rs.getString("Unit");
                sssc=rs.getString("StockScope");
                ssdc=rs.getString("StockDesc");
                StockUnit.setText(ssun);
                StockScope.setText(sssc);
                StockDesc.setText(ssdc);
                sscm=rs.getString("Company");
                ssef=rs.getString("SideEffect");
                StockComp.setText(sscm);
                StockEff.setText(ssef);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nStock Enumeration Failed");
        }
    }//GEN-LAST:event_StockListMouseClicked

    private void StockIDSrchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StockIDSrchKeyReleased
        // TODO add your handling code here:
        Conn=(Connection) Dbs.InitDb();
        try {
            int h=Integer.parseInt(StockIDSrch.getText().toString());
            String sav="SELECT Name,StockDate AS Date,StockTime AS Time,Company,Unit,StockID,StockDesc,StockScope,SideEffect FROM `tbl_Stock` WHERE `StockID` LIKE '%"+h+"%' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            this.StockCurr.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nSearch Criteria Failed");
        }
    }//GEN-LAST:event_StockIDSrchKeyReleased

    private void StockNmSrchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StockNmSrchKeyReleased
        // TODO add your handling code here:
        try {
            String h=(StockNmSrch.getText().toString());
            String sav="SELECT Name,StockDate AS Date,StockTime AS Time,Company,Unit,StockID,StockDesc,StockScope,SideEffect FROM `tbl_Stock` WHERE `Name` LIKE '%"+h+"%' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            this.StockCurr.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nSearch Criteria Failed");
        }
    }//GEN-LAST:event_StockNmSrchKeyReleased

    private void ClientSerchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientSerchKeyReleased
        // TODO add your handling code here:
        try {
            int h=Integer.parseInt(ClientSerch.getText().toString());
            String sav="SELECT Name,NationalID,ClientID,Date,Region FROM `tbl_Clients`  WHERE `ClientID` LIKE '%"+h+"%' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            this.ClientList.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nSearch Criteria Failed");
        }
    }//GEN-LAST:event_ClientSerchKeyReleased

    private void RfshClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RfshClientMouseClicked
        // TODO add your handling code here:
        Poplt();
    }//GEN-LAST:event_RfshClientMouseClicked

    private void ClientListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientListMouseClicked
        // TODO add your handling code here:
        String ccnm,ssid,ssdt,sstm,sscm,sssc,ssdc,ssun,ssef;
        int sid;
        try {
            int ro=ClientList.getSelectedRow();
            String otrow=ClientList.getModel().getValueAt(ro, 2).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Clients`,`tbl_Treats` WHERE (`tbl_Clients`.`ClientID`= '"+rws+"' AND `tbl_Treats`.`ClientID`='"+rws+"') ORDER BY `tbl_Treats`.`Count` DESC";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                ccnm=rs.getString("tbl_Clients.Name");
                ClientNam.setText(ccnm);
                ClientNmSrch.setText(ccnm);
                sid=rs.getInt("tbl_Clients.NationalID");
                ClientNat.setText(String.valueOf(sid));
                ssdt=rs.getString("tbl_Clients.ClientID");
                ClientSerch.setText(ssdt);
                ClienID.setText(ssdt);
                //
                ssdt=rs.getString("tbl_Treats.Conditn");
                TrtCond.setText(ssdt+"\n\n\n");
                ssdt=rs.getString("tbl_Treats.Date");
                //TrtCond.append("(As Of"+ssdt+")");
                ssdt=rs.getString("tbl_Treats.Diagnosis");
                TrtDiag.setText(ssdt);
                ssdt=rs.getString("tbl_Treats.Lab");
                TrtLab.setText(ssdt);
                ssdt=rs.getString("tbl_Treats.Drugs");
                TrtDrug.setText(ssdt);
                byte [] imgdata=rs.getBytes("Avatar");
                imcn=new ImageIcon(imgdata);
                ClinImg.setIcon(imcn);               
            }else{
                int nm=ClientList.getSelectedRow();
                ccnm=ClientList.getModel().getValueAt(ro, 0).toString();
                int natid=Integer.parseInt( ClientList.getModel().getValueAt(ro, 1).toString());
                int clid=Integer.parseInt( ClientList.getModel().getValueAt(ro, 2).toString());
                ClientNam.setText(ccnm);
                ClientNat.setText(String.valueOf(natid));
                ClienID.setText(String.valueOf(clid));
                TrtCond.setText("# No Available Records");
                TrtDrug.setText("# No Available Records");
                TrtLab.setText("# No Available Records");
                TrtDiag.setText("# No Available Records");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nClient Enumeration Has Failed");
        }        
        try {
            String ssdt1=null;
            int ro=ClientList.getSelectedRow();
            String otrow=ClientList.getModel().getValueAt(ro, 2).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Treats` WHERE `ClientID`='"+rws+"' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            while (rs.next()) {
                ssdt1=rs.getString("tbl_Treats.Date");
                //TrtCond.append("(As Of"+ssdt1+")");              
            }
            TrtCond.append("(As Of "+ssdt1+" )"); 
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nAs Of Error");
        }
        
        Poplt();
    }//GEN-LAST:event_ClientListMouseClicked

    private void CmmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmmitActionPerformed
        // TODO add your handling code here:
        IntOnly();
        try {
            String Kret="INSERT INTO `tbl_Treats` (`Count`,`Name` ,`NationalID` ,`ClientID` ,`Date` ,`Time` ,`Conditn` ,`Diagnosis` ,`Lab`,`Drugs` ) VALUES (NULL,?,?,?,?,?,?,?,?,?)";
            pst= (PreparedStatement) Conn.prepareStatement(Kret);

            pst.setString(1, ClientNam.getText().toString());
            pst.setString(2, ClientNat.getText().toString());
            pst.setString(3, ClienID.getText().toString());
            pst.setString(4, DDate.getText().toString());
            pst.setString(5, thaa);
            pst.setString(6, ClieCond.getText().toString());
            pst.setString(7, ClieDaig.getText().toString());
            pst.setString(8, ClieLab.getText().toString());
            pst.setString(9, ClieMed.getText().toString());
            
            pst.executeUpdate();

            try {
                String lv="INSERT INTO `tbl_Logs` ( `Count`,`Date`,`Time`,`Username`,`Action`,`Correct` ) VALUES (NULL,?,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, Usrnm.getText().toString());
                pst.setString(4, "Treat Patient");
                pst.setString(5, "True");
                pst.executeUpdate();

            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, e+"\nLog Entry Error");
                Toolkit.getDefaultToolkit().beep();
            }
            
            String lg="Client Name "+ClientNam.getText().toString()+"<#> \n"+riu +" <#>"+thaa+"\n"+"Symptoms ->"+ClieCond.getText()+"\n"+"Diagnosis ->"+ClieDaig.getText()+"\n"+"Lab Report ->"+ClieLab.getText()+"\n"+"Drug Report ->\n"+ClieMed.getText()+"\n\n";
            LogBar.setText(lg);
            //JOptionPane.showMessageDialog(null, "Client ( "+ClientNam.getText().toString()+" )Succesfully Handled");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "\nPatient History Has Hasn't Been Saved ");
            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_CmmitActionPerformed

    private void ClientListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientListMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientListMouseEntered

    private void ClientSerchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientSerchKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_ClientSerchKeyTyped

    private void MedicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MedicoMouseClicked
        // TODO add your handling code here:
        //String gg=JOptionPane.showInputDialog(null,"Drug Name");
        //Medico.append("[ "+gg+ " ]\n");
    }//GEN-LAST:event_MedicoMouseClicked

    private void b1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_b1KeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_b1KeyTyped

    private void ComitDrugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComitDrugActionPerformed
        // TODO add your handling code here:
        CompDrug();
            Medico.setText(lo);
            ClieMed.append(lo+"\n");
            DruCorna.append(lo2+"\n\n");
            
            DrgInsrt();
            
        a1.setText("");
        a2.setText("");
        b1.setText("");
        
        try {
                String lv="INSERT INTO `tbl_Logs` ( `Count`,`Date`,`Time`,`Username`,`Action`,`Correct` ) VALUES (NULL,?,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, Usrnm.getText().toString());
                pst.setString(4, "Prescribe Drugs");
                pst.setString(5, "True");
                pst.executeUpdate();

            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, e+"\nLog Entry Error");
                Toolkit.getDefaultToolkit().beep();
            }
        
        Poplt();
    }//GEN-LAST:event_ComitDrugActionPerformed

    private void a1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_a1KeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_a1KeyTyped

    private void a2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_a2KeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_a2KeyTyped

    private void ClieMedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClieMedMouseClicked
        // TODO add your handling code here:
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_ClieMedMouseClicked

    private void BckDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BckDocActionPerformed
        // TODO add your handling code here:
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.TRUE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_BckDocActionPerformed

    private void Dr1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Dr1ActionPerformed
        // TODO add your handling code here:
        Drugs dr = new Drugs();
                
        Medico.setText("Description\t<Here>"+"\nDrug Scope\t<Here>"+"\nSide"
                + " Effects\t<Here>");
        
        //String nmm=Dr1.getSelectedItem().toString();
        Dr1.removeAllItems();
        
        ArrayList<String> druglisting= new ArrayList<String>();
        druglisting=dr.DrugList();
        
        for (int i = 0; i < druglisting.size(); i++) {
            Dr1.addItem(druglisting.get(i));
        }
    }//GEN-LAST:event_Dr1ActionPerformed

    private void DrListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DrListMouseClicked
        // TODO add your handling code here:
        String nmm=Dr1.getSelectedItem().toString();
        
        try{              
            String cops4= "SELECT * FROM `tbl_Stock` WHERE `Name` ='"+nmm+"'";
            pst = ((PreparedStatement)Conn.prepareStatement(cops4));
            rs = pst.executeQuery();
            if (rs.next()) {
                d5=rs.getInt("StockID");
                d4 =rs.getInt("UnitPrice");
                String d1=rs.getString("StockDesc");
                String d2=rs.getString("StockScope");
                String d3=rs.getString("SideEffect");
                
                Medico.setText("Description\t"+d1+"\nDrug Scope\t"+d2+"\nSide Effects\t"+d3+"\nUnit Price\t"+d4);
            }else{
                Medico.setText("Description\t<No Record Available>"+"\nDrug Scope\t<No Record Available>"+"\nSide"
                + " Effects\t<No Record Available>");
            }
            CompDrug();
        }catch (Exception e){
              //JOptionPane.showMessageDialog(null, e + "\nAction Not Allowed Please");
        }            //c1.setText("");
        ComitDrug.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_DrListMouseClicked

    private void StockUPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StockUPriceKeyTyped
        // TODO add your handling code here:
         char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_StockUPriceKeyTyped

    private void RedPharmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedPharmActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.TRUE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.FALSE);
        Plogic.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_RedPharmActionPerformed

    private void Bck3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bck3ActionPerformed
        // TODO add your handling code here:
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Plogo.setVisible(Boolean.TRUE);
        Plogic.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_Bck3ActionPerformed

    private void WrtFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WrtFileMouseClicked
        // TODO add your handling code here
        Nott.setVisible(Boolean.TRUE);
        String fnm=Dr1.getSelectedItem().toString()+".txt";
        try {
           File fl=new File(fnm);
            if (!fl.exists()) {
                fl.createNewFile();
            }
           FileWriter fw=new FileWriter(fl);
           DruCorna.write(fw);
           fw.close();
           Nott.setText("File Successfully Parsed");
           Thread.sleep(4000);
           Nott.setText(null);
        } catch (Exception e) {
            try {
                Nott.setText(e.getMessage());
                Thread.sleep(4000);
                Nott.setText(null);
            } catch (Exception ex) {
            }
            //JOptionPane.showMessageDialog(null, e+"\nWriting Error");
        }
    }//GEN-LAST:event_WrtFileMouseClicked

    private void PharmSerchIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PharmSerchIDKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_PharmSerchIDKeyTyped

    private void PharmSerchIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PharmSerchIDKeyReleased
        // TODO add your handling code here:
        Conn=(Connection) Dbs.InitDb();
        int h=Integer.parseInt(PharmSerchID.getText().toString());
        
        if (Cl1.isSelected()) {
            tbl="tbl_Clients";
            sav2="SELECT Name,NationalID,ClientID,Date,Region FROM `"+tbl+"`  WHERE `ClientID` LIKE '%"+h+"%' ";
        }
        if (ClHist1.isSelected()) {
            tbl="tbl_Treats";
            sav2="SELECT Name,ClientID,Date,Conditn AS Conditions,Diagnosis,Lab,Drugs FROM `"+tbl+"`  WHERE `ClientID` LIKE '%"+h+"%' ";
        }
        if (ClDrg1.isSelected()) {
            tbl="tbl_Drugs";
            sav2="SELECT ClientID,Date,Time,Drug,Dosage,Duration FROM `"+tbl+"`  WHERE `ClientID` LIKE '%"+h+"%' ";
        }
        try {
            pst=(PreparedStatement) Conn.prepareStatement(sav2);
            rs=pst.executeQuery();
            this.TreatsList.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nSearch Criteria Terminated With AN Error");
        }
    }//GEN-LAST:event_PharmSerchIDKeyReleased

    private void RstTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RstTblActionPerformed
        // TODO add your handling code here:
        if (Cl1.isSelected()) {
            tbl="tbl_Clients";
            sav3="SELECT Name,NationalID,ClientID,Date,Region FROM `"+tbl+"` ";
        }
        if (ClHist1.isSelected()) {
            tbl="tbl_Treats";
            sav3="SELECT Name,ClientID,Date,Conditn AS Conditions,Diagnosis,Lab,Drugs FROM `"+tbl+"` ";
        }
        if (ClDrg1.isSelected()) {
            tbl="tbl_Drugs";
            sav3="SELECT ClientID,Date,Time,Drug,Dosage,Duration FROM `"+tbl+"` ";
        }
        try {
            pst=(PreparedStatement) Conn.prepareStatement(sav3);
            rs=pst.executeQuery();
            this.TreatsList.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "\nSearch Criteria Failed");
        }
    }//GEN-LAST:event_RstTblActionPerformed

    private void TreatsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TreatsListMouseClicked
        // TODO add your handling code here:
        try {
            int ro=TreatsList.getSelectedRow();
            String Drg=TreatsList.getModel().getValueAt(ro, 6).toString();
            String nm=TreatsList.getModel().getValueAt(ro, 0).toString();
            String cid=TreatsList.getModel().getValueAt(ro, 1).toString();
            String dat=TreatsList.getModel().getValueAt(ro, 2).toString();
            String cond=TreatsList.getModel().getValueAt(ro, 3).toString();
            String diag=TreatsList.getModel().getValueAt(ro, 4).toString();
            PhrmView.setText(Drg); 
            ClNam.setText(nm); 
            ClID.setText(cid); 
            Cldat.setText(dat); 
            ClCond.setText(cond); 
            ClDiag.setText(diag); 
            PhrmView.setText(Drg); 
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nPharm View");
        }
    }//GEN-LAST:event_TreatsListMouseClicked

    private void Dr1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Dr1MouseEntered
        // TODO add your handling code here:
        /*try {
            String cops4= "SELECT * FROM `tbl_Stock`";
            pst = ((PreparedStatement)Conn.prepareStatement(cops4));
            rs = pst.executeQuery();
            Dr1.removeAllItems();
            Dr1.addItem("<Select>");
            while (rs.next()) {
                String dr=rs.getString("Name");
                Dr1.addItem(dr);
                d6=rs.getInt("StockID");
                
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e+"\nME error");
        }*/
    }//GEN-LAST:event_Dr1MouseEntered

    private void StaffIDNo1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffIDNo1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_StaffIDNo1KeyTyped

    private void StaffID1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffID1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_StaffID1KeyTyped

    private void StaffCont1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StaffCont1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_StaffCont1KeyTyped

    private void ClientIDNat1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientIDNat1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientIDNat1KeyTyped

    private void ClientID1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientID1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientID1KeyTyped

    private void ClientCont1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClientCont1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientCont1KeyTyped

    private void tbl_StaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_StaffMouseClicked
        // TODO add your handling code here:
        try {
            int ro=tbl_Staff.getSelectedRow();
            String otrow=tbl_Staff.getModel().getValueAt(ro, 2).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Staff` WHERE `StaffId`= '"+rws+"' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                String ssns,ssid,ssdt,sstm,sscm,sssc,ssdc,ssun,ssef;
                ssns=rs.getString("Name");
                StaffNm1.setText(ssns);
                ssid=rs.getString("NationalID");
                StaffIDNo1.setText(ssid);
                ssef=rs.getString("StaffId");
                StaffID1.setText(ssef);
                sstm=rs.getString("Username");
                ssun=rs.getString("Password");
                sssc=rs.getString("Contacts");
                ssdc=rs.getString("Level");
                StaffUsr1.setText(sstm);
                StaffCont1.setText(sssc);
                StaffPwd1.setText(ssun);
                StaffLvl1.setSelectedItem(ssdc); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nAn Error Occured While Processing Data");
        }
    }//GEN-LAST:event_tbl_StaffMouseClicked

    private void tbl_ClientEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ClientEditMouseClicked
        // TODO add your handling code here:
        try {
            int ro=tbl_ClientEdit.getSelectedRow();
            String otrow=tbl_ClientEdit.getModel().getValueAt(ro, 2).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Clients` WHERE `ClientID`= '"+rws+"' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                String ssns,ssid,ssdt,sstm,sscm,sssc,ssdc,ssun,ssef;
                int sid;
                ssns=rs.getString("Name");
                ClientNm1.setText(ssns);
                sid=rs.getInt("NationalID");
                ClientIDNat1.setText(String.valueOf(sid));
                ssdt=rs.getString("ClientID");
                ClientID1.setText(ssdt);
                sstm=rs.getString("Region");
                ssun=rs.getString("Contacts");
                StockUnit.setText(ssun);
                ClientReg1.setText(sstm);
                ClientCont1.setText(ssun);  
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nClient Data Fetch has Terminated With An Error");
        }
    }//GEN-LAST:event_tbl_ClientEditMouseClicked

    private void StaffUpdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StaffUpdtActionPerformed
        // TODO add your handling code here:
        try {
            int ro=tbl_Staff.getSelectedRow();
            String cc=tbl_Staff.getModel().getValueAt(ro, 0).toString();
            
            String snm = StaffNm1.getText().toString();
            String scdt = StaffIDNo1.getText().toString();
            String sctm = StaffUsr1.getText().toString();
            String scom = StaffPwd1.getText().toString();
            String scun = StaffCont1.getText().toString();
            String lv = StaffLvl1.getSelectedItem().toString();

            String sav="UPDATE `tbl_Staff` SET `Name` = '"+snm+"',`NationalID` ='"+scdt+"',Username = '"+sctm+"',Password = '"+scom+"' , Contacts = '"+scun+"',Level = '"+lv+"' WHERE `StaffID`= '"+Integer.parseInt(StaffID1.getText().toString())+"' ";
            
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv1="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv1);
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Staff Updated");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            Poplt();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nSorry, An Error Occured");
        }
    }//GEN-LAST:event_StaffUpdtActionPerformed

    private void StaffDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StaffDelActionPerformed
        // TODO add your handling code here:
        try {
            int ro=StockList.getSelectedRow();
            String cc=StockList.getModel().getValueAt(ro, 2).toString();
            String sav="DELETE FROM `tbl_Staff` WHERE `StaffID`= ' "+cc+" '";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Staff Deleted");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            Poplt();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nStaff Deletion Error");
        }
    }//GEN-LAST:event_StaffDelActionPerformed

    private void ClientDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientDelActionPerformed
        // TODO add your handling code here:
        try {
            int ro=StockList.getSelectedRow();
            String cc=StockList.getModel().getValueAt(ro, 2).toString();
            String sav="DELETE FROM `tbl_Clients` WHERE `ClientID`= ' "+cc+" '";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Client Deleted");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            
            Poplt();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nCleint Deletion Error");
        }
    }//GEN-LAST:event_ClientDelActionPerformed

    private void ClienUpdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClienUpdtActionPerformed
        // TODO add your handling code here:
        try {
            int ro=tbl_ClientEdit.getSelectedRow();
            String cc=tbl_ClientEdit.getModel().getValueAt(ro, 2).toString();
            
            String snm = ClientNm1.getText().toString();
            String scdt = ClientIDNat1.getText().toString();
            String sctm = ClientID1.getText().toString();
            String scom = ClientCont1.getText().toString();
            String scun = ClientReg1.getText().toString();
            
            String sav="UPDATE `tbl_Clients` SET `Name` = '"+snm+"',`NationalID` ='"+scdt+"',Contacts = '"+scom+"', Region = '"+scun+"' WHERE `ClientID`= '"+Integer.parseInt(ClientID1.getText().toString())+"' ";
            
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Client Update");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            Poplt();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nClient Update Has Been Unsuccesfull");
        }
    }//GEN-LAST:event_ClienUpdtActionPerformed

    private void StockDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockDtActionPerformed
        // TODO add your handling code here:
        //IntOnly();
    }//GEN-LAST:event_StockDtActionPerformed

    private void RfSerchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RfSerchKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_RfSerchKeyTyped

    private void RfByKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RfByKeyTyped
        // TODO add your handling code here:
        char cc=evt.getKeyChar();
        if(!(Character.isDigit(cc) || cc==KeyEvent.VK_BACK_SPACE || cc==KeyEvent.VK_DELETE )){
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_RfByKeyTyped

    private void tbl_RfListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_RfListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_RfListMouseClicked

    private void tbl_RfSerchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_RfSerchMouseClicked
        // TODO add your handling code here:
        try {
            int ro=tbl_RfSerch.getSelectedRow();
            String otrow=tbl_RfSerch.getModel().getValueAt(ro, 1).toString();
            int rws=Integer.parseInt(otrow);
            String sav="SELECT * FROM `tbl_Stock`,tbl_StockLevel WHERE (tbl_StockLevel.`StockID` = '"+rws+"' AND tbl_Stock.`StockID` = '"+rws+"' )";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                String ssns,ssid;
                ssns=rs.getString("tbl_Stock.Name");
                RfNam.setText(ssns);
                ssid=rs.getString("tbl_Stock.StockID");
                RfStckID.setText(ssid);
                sed=rs.getString("tbl_StockLevel.Avail");
                RfAlble.setText(sed);
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nMirror 1");
        }
        Poplt();
    }//GEN-LAST:event_tbl_RfSerchMouseClicked

    private void RfSerchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RfSerchKeyReleased
        // TODO add your handling code here:
        Conn=(Connection) Dbs.InitDb();
        try {
            int h=Integer.parseInt(RfSerch.getText().toString());
            String sav="SELECT Name,StockID FROM `tbl_Stock` WHERE `StockID` LIKE '%"+h+"%' ";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            this.tbl_RfSerch.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nSearch Criteria Failed");
        }
    }//GEN-LAST:event_RfSerchKeyReleased

    private void RfByKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RfByKeyReleased
        // TODO add your handling code here:
        int ss=Integer.parseInt(RfBy.getText().toString());
        int s=Integer.parseInt(sed);
        int sum=ss+s;
        RfNwLv.setText(String.valueOf(sum));
    }//GEN-LAST:event_RfByKeyReleased

    private void Bck10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Bck10MouseClicked
        // TODO add your handling code here:
        Plogo.setVisible(Boolean.TRUE);
        Plogic.setVisible(Boolean.TRUE);
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
    }//GEN-LAST:event_Bck10MouseClicked

    private void StatAssesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatAssesActionPerformed
        // TODO add your handling code here:
        String riu1,riu2,riu3,riu4,riu5,riu6,riu7;
        int dayt1=dayt-1;
        
        dayt=cal.get(Calendar.DAY_OF_MONTH);
        mont=cal.get(Calendar.MONTH)+1;
        ya=cal.get(Calendar.YEAR);
        
        riu1= dayt1+"/"+mont+"/"+ya;
        riu2= (dayt1-1)+"/"+mont+"/"+ya;
        riu3= (dayt1-2)+"/"+mont+"/"+ya;
        riu4= (dayt1-3)+"/"+mont+"/"+ya;
        riu5= (dayt1-4)+"/"+mont+"/"+ya;
        riu6= (dayt1-5)+"/"+mont+"/"+ya;
        riu7= (dayt1-6)+"/"+mont+"/"+ya;
        
        if (StatCrit.getSelectedIndex()==1) {
            
            if (StatParam.getSelectedIndex()==1) {
                try {
                    String cops = "SELECT tbl_Stock.Name,tbl_Stock.StockID,tbl_Stock.UnitPrice,tbl_StockMorn.Avail AS Openning,tbl_StockLevel.Avail AS Current,(tbl_StockMorn.Avail-tbl_StockLevel.Avail) AS Sold,((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) AS Income FROM tbl_Stock,tbl_StockLevel,tbl_StockMorn WHERE (tbl_Stock.StockID =tbl_StockLevel.StockID AND tbl_Stock.StockID = tbl_StockMorn.StockID AND tbl_StockLevel.StockID = tbl_StockMorn.StockID AND (tbl_StockMorn.Date='"+riu+"' OR tbl_StockMorn.Date='"+riu1+"' OR tbl_StockMorn.Date='"+riu2+"' OR tbl_StockMorn.Date='"+riu3+"' OR tbl_StockMorn.Date='"+riu4+"' OR tbl_StockMorn.Date='"+riu5+"' OR tbl_StockMorn.Date='"+riu6+"' OR tbl_StockMorn.Date='"+riu7+"')) ORDER BY (tbl_StockMorn.Avail-tbl_StockLevel.Avail) DESC";
                    pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
                    rs = this.pst.executeQuery();
                    tbl_Statis.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e) {
                    //JOptionPane.showMessageDialog(null, e+"\n111 111");
                }
            
            }
            if (StatParam.getSelectedIndex()==2) {
                try {
                    String cops = "SELECT tbl_Stock.Name,tbl_Stock.StockID,tbl_Stock.UnitPrice,tbl_StockMorn.Avail AS Openning,tbl_StockLevel.Avail AS Current,(tbl_StockMorn.Avail-tbl_StockLevel.Avail) AS Sold,((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) AS Income FROM tbl_Stock,tbl_StockLevel,tbl_StockMorn WHERE (tbl_Stock.StockID =tbl_StockLevel.StockID AND tbl_Stock.StockID = tbl_StockMorn.StockID AND tbl_StockLevel.StockID = tbl_StockMorn.StockID AND tbl_StockMorn.Date='"+riu+"') ORDER BY (tbl_StockMorn.Avail-tbl_StockLevel.Avail) DESC";
                    pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
                    rs = this.pst.executeQuery();
                    tbl_Statis.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e) {
                    //JOptionPane.showMessageDialog(null, e+"\n111 222");
                }
            }
            
        }
        if (StatCrit.getSelectedIndex()==2) {
            if (StatParam.getSelectedIndex()==1) {
                try {
                    try {
                    String cops = "SELECT tbl_Stock.Name,tbl_Stock.StockID,tbl_Stock.UnitPrice,tbl_StockMorn.Avail AS Openning,tbl_StockLevel.Avail AS Current,(tbl_StockMorn.Avail-tbl_StockLevel.Avail) AS Sold,((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) AS Income FROM tbl_Stock,tbl_StockLevel,tbl_StockMorn WHERE (tbl_Stock.StockID =tbl_StockLevel.StockID AND tbl_Stock.StockID = tbl_StockMorn.StockID AND tbl_StockLevel.StockID = tbl_StockMorn.StockID AND (tbl_StockMorn.Date='"+riu+"' OR tbl_StockMorn.Date='"+riu1+"' OR tbl_StockMorn.Date='"+riu2+"' OR tbl_StockMorn.Date='"+riu3+"' OR tbl_StockMorn.Date='"+riu4+"' OR tbl_StockMorn.Date='"+riu5+"' OR tbl_StockMorn.Date='"+riu6+"' OR tbl_StockMorn.Date='"+riu7+"') ) ORDER BY ((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) DESC";
                    pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
                    rs = this.pst.executeQuery();
                    tbl_Statis.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e) {
                    //JOptionPane.showMessageDialog(null, e+"\n222 111");
                }
                } catch (Exception e) {
                }
            
            }
            if (StatParam.getSelectedIndex()==2) {
                try {
                    String cops = "SELECT tbl_Stock.Name,tbl_Stock.StockID,tbl_Stock.UnitPrice,tbl_StockMorn.Avail AS Openning,tbl_StockLevel.Avail AS Current,(tbl_StockMorn.Avail-tbl_StockLevel.Avail) AS Sold,((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) AS Income FROM tbl_Stock,tbl_StockLevel,tbl_StockMorn WHERE (tbl_Stock.StockID =tbl_StockLevel.StockID AND tbl_Stock.StockID = tbl_StockMorn.StockID AND tbl_StockLevel.StockID = tbl_StockMorn.StockID AND tbl_StockMorn.Date='"+riu+"' ) ORDER BY ((tbl_StockMorn.Avail-tbl_StockLevel.Avail)*tbl_Stock.UnitPrice) DESC";
                    pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
                    rs = this.pst.executeQuery();
                    tbl_Statis.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e) {
                    //JOptionPane.showMessageDialog(null, e+"\n222 222");
                }
            }
            
        }
    }//GEN-LAST:event_StatAssesActionPerformed

    private void StatCritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatCritActionPerformed
        // TODO add your handling code here:
        StatParam.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_StatCritActionPerformed

    private void RfADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RfADDActionPerformed
        // TODO add your handling code here:
        long daty = Long.valueOf(System.currentTimeMillis());
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(daty);
        hours=sdf1.format(resultdate);
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MMM dd");
        Date resultdate2 = new Date(daty);
        day=sdf2.format(resultdate);
        
        try {
            String sav="UPDATE `tbl_StockLevel` SET `Avail` = '"+RfNwLv.getText()+"' WHERE `StockID`= '"+Integer.parseInt(RfStckID.getText().toString())+"' ";
            
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            pst.execute();
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Stock Refill");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "\nStock Refill Error");
                Toolkit.getDefaultToolkit().beep();
            }
        Poplt();
    }//GEN-LAST:event_RfADDActionPerformed

    private void StockIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StockIDMouseClicked
        // TODO add your handling code here:
        try {
            String sav="SELECT * FROM `tbl_Stock` ORDER BY `tbl_Stock`.`Count` DESC";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                int snid=rs.getInt("tbl_Stock.StockID");
                int su=snid+1;
                StockID.setText(String.valueOf(su));
                StockID.setEnabled(Boolean.FALSE);
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nAction Not Allowed Please");
        }
    }//GEN-LAST:event_StockIDMouseClicked

    private void StaffIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StaffIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StaffIDActionPerformed

    private void StockIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StockIDActionPerformed

    private void StaffIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StaffIDMouseClicked
        // TODO add your handling code here:
        try {
            String sav="SELECT * FROM `tbl_Staff` ORDER BY `Count` DESC";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                int snid=rs.getInt("StaffID");
                int su=snid+1;
                StaffID.setText(String.valueOf(su));
                StaffID.setEnabled(Boolean.FALSE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\nVariables Assignment Has Failed");
        }
    }//GEN-LAST:event_StaffIDMouseClicked

    private void ClientIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientIDMouseClicked
        // TODO add your handling code here:
        try {
            String sav="SELECT * FROM `tbl_Clients` ORDER BY `Count` DESC";
            pst=(PreparedStatement) Conn.prepareStatement(sav);
            rs=pst.executeQuery();
            if (rs.next()) {
                int snid=rs.getInt("ClientID");
                int su=snid+1;
                ClientID.setText(String.valueOf(su));
                ClientID.setEnabled(Boolean.FALSE);
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nAction Not Allowed Please");
        }
    }//GEN-LAST:event_ClientIDMouseClicked

    private void StLvApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StLvApplyActionPerformed
        // TODO add your handling code here:
        int slv=Integer.parseInt(StLvSID.getText().toString());
        int lw=Integer.parseInt(StLvNum.getText().toString());
        try {
            String lv="UPDATE tbl_StockLow SET Lows='"+lw+"' WHERE StockID='"+slv+"'";
            pst=(PreparedStatement) Conn.prepareStatement(lv);
            pst.execute();
            
            Refsh();

        } catch (Exception e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, e+"\nUpdate Failed");
        }
    }//GEN-LAST:event_StLvApplyActionPerformed

    private void StLvRfshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StLvRfshMouseClicked
        // TODO add your handling code here:
        Refsh();
    }//GEN-LAST:event_StLvRfshMouseClicked

    private void jLabel110MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel110MouseClicked
        // TODO add your handling code here:
        Refsh();
    }//GEN-LAST:event_jLabel110MouseClicked

    private void tbl_StLvsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_StLvsMouseClicked
        // TODO add your handling code here:
        try {
            int ro=tbl_StLvs.getSelectedRow();
            String nm=tbl_StLvs.getModel().getValueAt(ro, 0).toString();

            String id=tbl_StLvs.getModel().getValueAt(ro, 1).toString();
            int rws=Integer.parseInt(id);
            
            StLvName.setText(nm);
            StLvSID.setText(String.valueOf(rws));

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e + "\nPosting Error");
        }
    }//GEN-LAST:event_tbl_StLvsMouseClicked

    private void ConCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConCheckActionPerformed
        // TODO add your handling code here:    
        Cmp();
        try {
            FileWriter fw=new FileWriter(f1);
            String sql="SELECT * FROM tbl_Stock";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append(",");
                fw.append(rs.getString(6));
                fw.append(",");
                fw.append(rs.getString(7));
                fw.append(",");
                fw.append(rs.getString(8));
                fw.append(",");
                fw.append(rs.getString(9));
                fw.append(",");
                fw.append(rs.getString(10));
                fw.append(",");
                fw.append(rs.getString(11));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Stock Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Stock "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f2);
            String sql="SELECT * FROM tbl_Treats";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append(",");
                fw.append(rs.getString(6));
                fw.append(",");
                fw.append(rs.getString(7));
                fw.append(",");
                fw.append(rs.getString(8));
                fw.append(",");
                fw.append(rs.getString(9));
                fw.append(",");
                fw.append(rs.getString(10));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Treating History Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Treating "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f3);
            String sql="SELECT * FROM tbl_Drugs";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append(",");
                fw.append(rs.getString(6));
                fw.append(",");
                fw.append(rs.getString(7));
                fw.append(",");
                fw.append(rs.getString(8));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Drug Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Drug "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f4);
            String sql="SELECT * FROM tbl_Clients";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append(",");
                fw.append(rs.getString(6));
                fw.append(",");
                fw.append(rs.getString(7));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Client Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Client "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f5);
            String sql="SELECT * FROM tbl_Staff";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append(",");
                fw.append(rs.getString(6));
                fw.append(",");
                fw.append(rs.getString(7));
                fw.append(",");
                fw.append(rs.getString(8));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Staff Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Staff "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f6);
            String sql="SELECT * FROM tbl_StockMorn";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Morning Stock Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("tbl Morning Stock "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f7);
            String sql="SELECT * FROM tbl_StockLevel";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("Stock Levels Table Has Been Parsed Succesfully\n");
        } catch (Exception e) {
            ConDump.append("Stock Levels "+e+"\n");
        }
        
        try {
            FileWriter fw=new FileWriter(f8);
            String sql="SELECT * FROM tbl_StockLow";
            Statement stmt=(Statement) Conn.createStatement();
            rs=stmt.executeQuery(sql);
            while (rs.next()) {                
                fw.append(rs.getString(1));
                fw.append(",");
                fw.append(rs.getString(2));
                fw.append(",");
                fw.append(rs.getString(3));
                fw.append(",");
                fw.append(rs.getString(4));;
                fw.append(",");
                fw.append(rs.getString(5));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            ConDump.append("StockLow Table Has Been Parsed Succesfully\n");
            
            try {
                String lv="INSERT INTO `tbl_Admine` ( `Count`,`AcDate`,`AcTime`,`Task`,`Output` ) VALUES (NULL,?,?,?,?)";
                pst=(PreparedStatement) Conn.prepareStatement(lv);
                IntOnly();
                pst.setString(1, day);
                pst.setString(2, hours);
                pst.setString(3, "Created Backup");
                pst.setString(4, "True");
                
                pst.executeUpdate();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            ConDump.append("tbl StockLow "+e+"\n");
        }
        
        Rejoinder();
    }//GEN-LAST:event_ConCheckActionPerformed

    private void Action_AdmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Action_AdmActionPerformed
        // TODO add your handling code here:
        Poplt();
        try {
            String cops = "SELECT * FROM `tbl_Admine`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
              rs = this.pst.executeQuery();
              tbl_Logs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_Action_AdmActionPerformed

    private void Action_StafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Action_StafActionPerformed
        // TODO add your handling code here:
        Poplt();
        try {
            String cops = "SELECT * FROM `tbl_Logs`";
              pst = ((PreparedStatement)this.Conn.prepareStatement(cops));
              rs = this.pst.executeQuery();
              tbl_Logs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_Action_StafActionPerformed

    private void ConViewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConViewMouseEntered
        // TODO add your handling code here:
        Socket ss=new Socket();
        InetSocketAddress add=new InetSocketAddress("www.google.com",80);
        try {
            ss.connect(add,8080);
            ConView.setSelectedIndex(0);
        } catch (Exception e) {
            ConView.setSelectedIndex(1);
        }
    }//GEN-LAST:event_ConViewMouseEntered

    private void RefshDetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RefshDetMouseClicked
        // TODO add your handling code here:
        Poplt();
        Tara();
    }//GEN-LAST:event_RefshDetMouseClicked

    private void SysLgutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SysLgutMouseClicked
        // TODO add your handling code here:
        stats=0;
        lvpr=0;
        Plogo.setVisible(Boolean.TRUE);
        Plogic.setVisible(Boolean.TRUE);
        Pan_Config.setVisible(Boolean.FALSE);
        Pan_Home.setVisible(Boolean.FALSE);
        Pan_Consult.setVisible(Boolean.FALSE);
        Pan_Doctor.setVisible(Boolean.FALSE);
        Pan_Lab.setVisible(Boolean.FALSE);
        Pan_OfferDrugs.setVisible(Boolean.FALSE);
        Tara();
    }//GEN-LAST:event_SysLgutMouseClicked

    private void SysDashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SysDashMouseClicked
        // TODO add your handling code here:
        if (stats==1) {
            Plogo.setVisible(Boolean.FALSE);
            Plogic.setVisible(Boolean.FALSE);
            Pan_Config.setVisible(Boolean.FALSE);
            Pan_Home.setVisible(Boolean.TRUE);
            Pan_Consult.setVisible(Boolean.FALSE);
            Pan_Doctor.setVisible(Boolean.FALSE);
            Pan_Lab.setVisible(Boolean.FALSE);
            Pan_OfferDrugs.setVisible(Boolean.FALSE);
            
//            Pan_Config.removeAll();
//            Pan_Config.removeAll();
//            Pan_Home.removeAll();
//            Pan_Consult.removeAll();
//            Pan_Doctor.removeAll();
//            Pan_Lab.removeAll();
//            Pan_OfferDrugs.removeAll();
//            Pan_Lab.removeAll();
            
            Pan_Config.updateUI();
            Pan_Home.updateUI();
            Pan_Consult.updateUI();
            Pan_Doctor.repaint();
            Pan_Lab.repaint();
            Pan_OfferDrugs.updateUI();
            Pan_Lab.updateUI();
            
            if (lvpr==1) {
                RedConsl.setEnabled(Boolean.TRUE);
                RedInj.setEnabled(Boolean.TRUE);
                RedLab.setEnabled(Boolean.TRUE);
                RedPharm.setEnabled(Boolean.TRUE);
                RedMore.setEnabled(Boolean.TRUE);
                
            }else if (lvpr==2){
                RedConsl.setEnabled(Boolean.TRUE);
                RedInj.setEnabled(Boolean.FALSE);
                RedLab.setEnabled(Boolean.FALSE);
                RedPharm.setEnabled(Boolean.FALSE);
                RedMore.setEnabled(Boolean.FALSE);
            }
            else if (lvpr==3){
                RedConsl.setEnabled(Boolean.FALSE);
                RedInj.setEnabled(Boolean.FALSE);
                RedLab.setEnabled(Boolean.FALSE);
                RedPharm.setEnabled(Boolean.TRUE);
                RedMore.setEnabled(Boolean.FALSE);
            }
        }
    }//GEN-LAST:event_SysDashMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /*if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }*/
                UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                //UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePag().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Action_Adm;
    private javax.swing.JRadioButton Action_Staf;
    private javax.swing.JTabbedPane Adminey;
    private javax.swing.JButton Bck1;
    private javax.swing.JLabel Bck10;
    private javax.swing.JButton Bck2;
    private javax.swing.JButton Bck3;
    private javax.swing.JButton BckDoc;
    private javax.swing.JPanel Cates;
    private javax.swing.JPanel Centa;
    private javax.swing.JRadioButton Cl1;
    private javax.swing.JTextArea ClCond;
    private javax.swing.JTextArea ClDiag;
    private javax.swing.JRadioButton ClDrg1;
    private javax.swing.JRadioButton ClHist1;
    private javax.swing.JTextField ClID;
    private javax.swing.JTextField ClNam;
    private javax.swing.JTextField ClTim;
    private javax.swing.JTextField Cldat;
    private javax.swing.JTextArea ClieCond;
    private javax.swing.JTextArea ClieDaig;
    private javax.swing.JTextArea ClieLab;
    private javax.swing.JTextArea ClieMed;
    private javax.swing.JTextField ClienID;
    private javax.swing.JButton ClienUpdt;
    private javax.swing.JButton ClientAdd;
    private javax.swing.JTextField ClientCont;
    private javax.swing.JTextField ClientCont1;
    private javax.swing.JButton ClientDel;
    private javax.swing.JTextField ClientDt;
    private javax.swing.JTextField ClientID;
    private javax.swing.JTextField ClientID1;
    private javax.swing.JTextField ClientIDNat;
    private javax.swing.JTextField ClientIDNat1;
    private javax.swing.JTable ClientList;
    private javax.swing.JTextField ClientNam;
    private javax.swing.JTextField ClientNat;
    private javax.swing.JTextField ClientNm;
    private javax.swing.JTextField ClientNm1;
    private javax.swing.JTextField ClientNmSrch;
    private javax.swing.JTextField ClientReg;
    private javax.swing.JTextField ClientReg1;
    private javax.swing.JTextField ClientSerch;
    private javax.swing.JLabel ClinImg;
    private javax.swing.JButton Cmmit;
    private javax.swing.JButton ComitDrug;
    private javax.swing.JButton ConCheck;
    private javax.swing.JTextArea ConDump;
    private javax.swing.JComboBox<String> ConView;
    private javax.swing.JTextField DDate;
    private javax.swing.JComboBox<String> Dr1;
    private javax.swing.JLabel DrList;
    private javax.swing.JTextArea DruCorna;
    private javax.swing.JPanel Heda;
    private javax.swing.JLabel ImgLab1;
    private javax.swing.JLabel ImgLab2;
    private javax.swing.JTextField ImgPat;
    private javax.swing.JTextField ImgPat2;
    private javax.swing.JLabel InsrtImg1;
    private javax.swing.JLabel InsrtImg3;
    private javax.swing.JTextArea LogBar;
    private javax.swing.JComboBox<String> LogLev;
    private javax.swing.JTextArea Medico;
    private javax.swing.JLabel Nott;
    private javax.swing.JPanel NwClient;
    private javax.swing.JPanel NwStaff;
    private javax.swing.JButton NwUsr;
    private javax.swing.JPanel Otherrs;
    private javax.swing.JTabbedPane Others;
    private javax.swing.JPanel Pan_Config;
    private javax.swing.JPanel Pan_Consult;
    private javax.swing.JPanel Pan_Doctor;
    private javax.swing.JPanel Pan_Home;
    private javax.swing.JPanel Pan_Lab;
    private javax.swing.JPanel Pan_OfferDrugs;
    private javax.swing.JPanel Pano;
    private javax.swing.JTextField PharmSerchID;
    private javax.swing.JButton PharmTerm;
    private javax.swing.JTextArea PhrmView;
    private javax.swing.JPanel Plogic;
    private javax.swing.JPanel Plogo;
    private javax.swing.JButton ProcidLog;
    private javax.swing.JPasswordField Pwd;
    private javax.swing.JButton RedConsl;
    private javax.swing.JButton RedInj;
    private javax.swing.JButton RedLab;
    private javax.swing.JButton RedMore;
    private javax.swing.JButton RedPharm;
    private javax.swing.JLabel RefshDet;
    private javax.swing.JButton RfADD;
    private javax.swing.JTextField RfAlble;
    private javax.swing.JTextField RfBy;
    private javax.swing.JTextField RfNam;
    private javax.swing.JTextField RfNwLv;
    private javax.swing.JTextField RfSerch;
    private javax.swing.JTextField RfStckID;
    private javax.swing.JLabel RfshClient;
    private javax.swing.JButton RstTbl;
    private javax.swing.JButton StLvApply;
    private javax.swing.JTextField StLvName;
    private javax.swing.JTextField StLvNum;
    private javax.swing.JLabel StLvRfsh;
    private javax.swing.JTextField StLvSID;
    private javax.swing.JTextField StaffCont;
    private javax.swing.JTextField StaffCont1;
    private javax.swing.JButton StaffDel;
    private javax.swing.JTextField StaffID;
    private javax.swing.JTextField StaffID1;
    private javax.swing.JTextField StaffIDNo;
    private javax.swing.JTextField StaffIDNo1;
    private javax.swing.JComboBox<String> StaffLvl;
    private javax.swing.JComboBox<String> StaffLvl1;
    private javax.swing.JTextField StaffNm;
    private javax.swing.JTextField StaffNm1;
    private javax.swing.JTextField StaffPwd;
    private javax.swing.JTextField StaffPwd1;
    private javax.swing.JButton StaffUpdt;
    private javax.swing.JTextField StaffUsr;
    private javax.swing.JTextField StaffUsr1;
    private javax.swing.JButton StatAsses;
    private javax.swing.JComboBox<String> StatCrit;
    private javax.swing.JComboBox<String> StatParam;
    private javax.swing.JLabel StckC;
    private javax.swing.JLabel StckLv;
    private javax.swing.JButton StockAdd;
    private javax.swing.JTextField StockComp;
    private javax.swing.JTable StockCurr;
    private javax.swing.JButton StockDel;
    private javax.swing.JTextArea StockDesc;
    private javax.swing.JTextField StockDt;
    private javax.swing.JButton StockEdit;
    private javax.swing.JTextField StockEff;
    private javax.swing.JTextField StockID;
    private javax.swing.JTextField StockIDSrch;
    private javax.swing.JTable StockList;
    private javax.swing.JTextField StockNm;
    private javax.swing.JTextField StockNmSrch;
    private javax.swing.JTextArea StockScope;
    private javax.swing.JTextField StockTm;
    private javax.swing.JTextField StockUPrice;
    private javax.swing.JTextField StockUnit;
    private javax.swing.JTabbedPane Stockies;
    private javax.swing.JPanel Stocks;
    private javax.swing.JLabel SysDash;
    private javax.swing.JLabel SysLgut;
    private javax.swing.JTable TreatsList;
    private javax.swing.JTextArea TrtCond;
    private javax.swing.JTextArea TrtDiag;
    private javax.swing.JTextArea TrtDrug;
    private javax.swing.JTextArea TrtLab;
    private javax.swing.JPanel UsrStting;
    private javax.swing.JTextField Usrnm;
    private javax.swing.JLabel WrtFile;
    private javax.swing.JTextField a1;
    private javax.swing.JTextField a2;
    private javax.swing.JTextField b1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTable tbl_ClientEdit;
    private javax.swing.JTable tbl_Costos;
    private javax.swing.JTable tbl_Logs;
    private javax.swing.JTable tbl_RfList;
    private javax.swing.JTable tbl_RfSerch;
    private javax.swing.JTable tbl_StLvs;
    private javax.swing.JTable tbl_Staff;
    private javax.swing.JTable tbl_Statis;
    // End of variables declaration//GEN-END:variables
    private ImageIcon imcn=null;
}
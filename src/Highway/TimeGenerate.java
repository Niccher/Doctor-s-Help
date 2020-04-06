/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author niccher
 */
public class TimeGenerate {
    
    String day,hours;
    long daty = Long.valueOf(System.currentTimeMillis());
    
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
    
}

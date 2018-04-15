/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author nicch
 */
public class ChMaster {
    
    public ChMaster(){
        Lola();
    }
    
    private  void Lola(){
            int port = 4142;
        try {

            ServerSocket ss = new ServerSocket(port);
            System.out.println("Waiting for a Clients....\n");
            while(true){
                Socket socket = ss.accept();

                SSocket sSocket = new SSocket(socket);
                Thread t = new Thread(sSocket);
                t.start();
                //System.out.println("Socket Stack Size-----");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"\nConnection Error");
        }
    }
    
    public static void main(String[] args) {
        new ChMaster();
    }
    
}

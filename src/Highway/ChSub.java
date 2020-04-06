/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author nicch
 */
public class ChSub {
    
    public ChSub(){
        Nig();
    }
    
    private void Nig(){
        int serverPort = 4142;

            try {
                InetAddress inetAdd = InetAddress.getByName("localhost");
                Socket socket = new Socket(inetAdd, serverPort);

                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                DataInputStream dIn = new DataInputStream(in);
                DataOutputStream dOut = new DataOutputStream(out);

                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));


                System.out.println("Post To Server");


                String line = null;

                System.out.println();
                while (true) {

                    line = keyboard.readLine();
                    //System.out.println("Flushing To Server");
                    dOut.writeUTF(line);
                    dOut.flush();

                    line = dIn.readUTF();
                    System.out.println("[Sent]" + line);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage()+"[Failed]" );
            }
    }
    
    
    public static void main(String[] args) {
        new ChSub();
    }
}

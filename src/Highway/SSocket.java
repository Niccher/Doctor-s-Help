/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Highway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author nicch
 */
class SSocket implements Runnable {

private Socket socket;

public SSocket(Socket socket) {
    this.socket = socket;

}

@Override
public void run() {
    try {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        DataInputStream dIn = new DataInputStream(in);
        DataOutputStream dOut = new DataOutputStream(out);

        String line = null;
       while (true) {
            line = dIn.readUTF();
            System.out.println("Received ----" + line);
            dOut.writeUTF(line + " Comming back from the server");
            dOut.flush();
            System.out.println("Listening....\n");
      }
    } catch (Exception e) {
        System.out.println(e.getMessage()+"\nSocketing Error ....");
    }
}
}

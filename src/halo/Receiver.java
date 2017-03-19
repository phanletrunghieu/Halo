/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo;

import halo.ui.ChatForm;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Phan Hieu
 */
public class Receiver extends Thread {
    private DataInputStream in;
    private ChatForm chatForm;

    public Receiver() {
    }

    public Receiver(DataInputStream in) {
        this.in = in;
    }

    public Receiver(DataInputStream in, ChatForm chatForm) {
        this.in = in;
        this.chatForm = chatForm;
    }

    @Override
    public void run() {
        String message;
        try {
            System.out.println("Start receive...");
            while ((message=in.readUTF())!=null) {
                chatForm.setVisible(true);
                chatForm.receiveNewMessage(message);
                System.out.println(message);
            }
            System.out.println("End receive...");
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

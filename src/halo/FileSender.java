/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo;

import halo.models.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Phan Hieu
 */
public class FileSender extends Thread {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;

    private File file;
    private final int MAX_LENGTH_SEND=20000;

    public FileSender(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public FileSender(String ip, int port, File file) throws IOException {
        this.socket = new Socket(ip, port);
        this.file = file;
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE, "Begin sending".getBytes("UTF8")));
            
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE_NAME, this.file.getName().getBytes("UTF8")));
            RandomAccessFile rw = new RandomAccessFile(this.file, "r");
            
            long current_file_pointer = 0;
            boolean loop_break = false;
            while (true) {
                if (din.read() == Packet.INITITALIZE) {
                    int b = 0;
                    String fromUsername = "";
                    while ((b = din.readByte()) != Packet.SEPARATOR) {
                        fromUsername += (char) b;
                    }
                    
                    byte[] cmd_buff = new byte[3];
                    din.read(cmd_buff, 0, cmd_buff.length);
                    
                    byte[] recv_buff = Packet.ReadStream(din);
                    switch (new String(cmd_buff)) {
                        case Packet.COMMAND_REQUEST_SEND_FILE_DATA:
                            current_file_pointer = Long.valueOf(new String(recv_buff));
                            int buff_len = (int) (rw.length() - current_file_pointer < MAX_LENGTH_SEND ? rw.length() - current_file_pointer : MAX_LENGTH_SEND);
                            byte[] temp_buff = new byte[buff_len];
                            if (current_file_pointer != rw.length()) {
                                rw.seek(current_file_pointer);
                                rw.read(temp_buff, 0, temp_buff.length);
                                dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE_DATA, temp_buff));
                                dout.flush();
                                System.out.println("Upload percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                            } else {
                                loop_break = true;
                            }
                            break;
                    }
                }
                if (loop_break == true) {
                    System.out.println("Stop Server informed");
                    dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FINISH, "Close".getBytes("UTF8")));
                    dout.flush();
                    socket.close();
                    System.out.println("Client Socket Closed");
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File không tồn tại");
        } catch (IOException ex) {
            Logger.getLogger(FileSender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

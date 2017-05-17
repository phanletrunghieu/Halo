package halo.file;

import halo.Halo;
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
import javax.swing.JLabel;

/**
 *
 * @author Phan Hieu
 */
public class FileSender extends Thread {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;

    private File file;
    private final int MAX_LENGTH_SEND = 20000;

    private JLabel jLabelSendFile;

    public FileSender(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public FileSender(String ip, int port, File file, JLabel jLabelSendFile) throws IOException {
        this.socket = new Socket(ip, port);
        this.file = file;
        this.jLabelSendFile = jLabelSendFile;
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE, this.file.getName().getBytes("UTF8")));

            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE_NAME, this.file.getName().getBytes("UTF8")));
            RandomAccessFile rw = new RandomAccessFile(this.file, "r");

            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FILE_LENGTH, String.valueOf(rw.length()).getBytes("UTF8")));

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
                                float percent = ((float) (current_file_pointer + temp_buff.length) / rw.length()) * 100;
                                jLabelSendFile.setText("Gửi file: " + this.file.getName() + " (" + (int)percent + "%)");
                                System.out.println("Upload percentage: " + ((float) (current_file_pointer + temp_buff.length) / rw.length()) * 100 + "%");
                            } else {
                                loop_break = true;
                            }
                            break;
                    }
                }
                if (loop_break == true) {
                    dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_FINISH, "Close".getBytes("UTF8")));
                    dout.flush();
                    rw.close();
                    socket.close();
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

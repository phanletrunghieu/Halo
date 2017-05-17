package halo.file;

import halo.Halo;
import halo.models.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import javax.swing.JLabel;

/**
 *
 * @author Phan Hieu
 */
public class FileReceiver extends Thread {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;

    private JLabel jLabelReceiveFile;

    public FileReceiver(Socket socket, JLabel jLabelReceiveFile) throws IOException {
        this.socket = socket;
        this.jLabelReceiveFile = jLabelReceiveFile;
        din = new DataInputStream(this.socket.getInputStream());
        dout = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        RandomAccessFile rw = null;
        long current_file_pointer = 0;
        String file_name = "";
        long file_length = 1;
        boolean loop_break = false;
        while (true) {
            try {
                if (din.readByte() == Packet.INITITALIZE) {
                    int b = 0;
                    String fromUsername = "";
                    while ((b = din.read()) != Packet.SEPARATOR) {
                        fromUsername += (char) b;
                    }

                    byte[] cmd_buff = new byte[3];
                    din.read(cmd_buff, 0, cmd_buff.length);
                    byte[] recv_data = Packet.ReadStream(din);
                    switch (new String(cmd_buff)) {
                        case Packet.COMMAND_SEND_FILE_NAME:
                            file_name = new String(recv_data);
                            rw = new RandomAccessFile(file_name, "rw");
                            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_REQUEST_SEND_FILE_DATA, String.valueOf(current_file_pointer).getBytes("UTF8")));
                            dout.flush();
                            break;
                        case Packet.COMMAND_SEND_FILE_LENGTH:
                            file_length = Long.parseLong(new String(recv_data));
                            break;
                        case Packet.COMMAND_SEND_FILE_DATA:
                            rw.seek(current_file_pointer);
                            rw.write(recv_data);
                            current_file_pointer = rw.getFilePointer();
                            float percent = ((float) current_file_pointer / file_length) * 100;
                            jLabelReceiveFile.setText("Nhận file: " + file_name + " (" + (int) percent + "%)");
                            System.out.println("Download percentage: " + ((float) current_file_pointer / file_length) * 100 + "%");
                            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_REQUEST_SEND_FILE_DATA, String.valueOf(current_file_pointer).getBytes("UTF8")));
                            dout.flush();
                            break;
                        case Packet.COMMAND_SEND_FINISH:
                            if ("Close".equals(new String(recv_data))) {
                                loop_break = true;
                            }
                            break;
                    }
                }
                if (loop_break == true) {
                    rw.close();
                    socket.close();
                }
            } catch (IOException ex) {
                break;
            }

        }
    }

}

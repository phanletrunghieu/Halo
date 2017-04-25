package halo.voice;

import halo.Halo;
import halo.models.Packet;
import halo.ui.call.RequestCallForm;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;

/**
 * Gửi yêu cầu cuộc gọi Chờ tín hiệu: huỷ cuộc gọi, đối phương đang bận, chấp
 * nhận cuộc gọi (bắt đầu ghi âm và gửi đi)
 *
 * @author Phan Hieu
 */
public class VoiceClient extends Thread {

    private String server_ip;
    private int port;

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private RequestCallForm requestCallingForm;

    public VoiceClient(String ip, int port, RequestCallForm requestCallingForm) throws IOException {
        this.server_ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.din = new DataInputStream(socket.getInputStream());
        this.dout = new DataOutputStream(socket.getOutputStream());
        requestCallingForm.dout = dout;
        this.requestCallingForm = requestCallingForm;
    }

    @Override
    public void run() {
        try {
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_REQUEST_CALL, "Request calling".getBytes("UTF8")));
            while (Halo.isCalling) {
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
                        case Packet.COMMAND_USER_BUSY:
                            requestCallingForm.UserBusy();
                            Halo.isCalling = false;
                            break;
                        case Packet.COMMAND_CANCEL_CALL:
                            Halo.isCalling = false;
                            requestCallingForm.dispatchEvent(new WindowEvent(requestCallingForm, WindowEvent.WINDOW_CLOSING));
                            break;
                        case Packet.COMMAND_ACCEPT_CALL:
                            new VoicePlayer().start();
                            new VoiceRecorder(this.server_ip, this.port).start();
                            requestCallingForm.Accept();
                            break;
                    }
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | LineUnavailableException ex) {
            Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

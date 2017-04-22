package halo.voice;

import halo.Halo;
import halo.models.Packet;
import halo.ui.call.ReceiveCallForm;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Chờ khi client huỷ cuộc gọi
 * @author Phan Hieu
 */
public class VoiceServer extends Thread {

    private Socket socket;
    private DataInputStream din;
    private ReceiveCallForm receiveCallForm;

    public VoiceServer(Socket socket, ReceiveCallForm receiveCallForm) throws IOException {
        this.socket = socket;
        this.din = new DataInputStream(this.socket.getInputStream());
        this.receiveCallForm = receiveCallForm;
    }

    @Override
    public void run() {
        while (Halo.isCalling) {
            try {
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
                        case Packet.COMMAND_CANCEL_CALL:
                            Halo.isCalling = false;
                            receiveCallForm.dispatchEvent(new WindowEvent(receiveCallForm, WindowEvent.WINDOW_CLOSING));
                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(VoiceServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

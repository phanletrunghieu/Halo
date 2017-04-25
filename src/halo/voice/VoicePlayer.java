package halo.voice;

import halo.Halo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Play sound nhận được
 * @author Phan Hieu
 */
public class VoicePlayer extends Thread {

    private DatagramSocket din;
    private SourceDataLine audio_out;
    private byte[] buffer = new byte[512];

    public VoicePlayer() throws LineUnavailableException, UnknownHostException, SocketException {
        AudioFormat format = getaudioformat();
        DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info_out)) {
            System.out.println("not suport");
            Halo.isCalling = false;
        }
        audio_out = (SourceDataLine) AudioSystem.getLine(info_out);
        audio_out.open(format);
        audio_out.start();
        
        this.din = new DatagramSocket(Halo.user.getPortListening(), InetAddress.getByName(Halo.user.getAddrListening()));
    }

    @Override
    public void run() {
        DatagramPacket incoming  = new DatagramPacket(buffer, buffer.length);
        while (Halo.isCalling) {
            try {
                din.receive(incoming);
                buffer = incoming.getData();
                audio_out.write(buffer, 0, buffer.length);
            } catch (IOException ex) {
                din.close();
            }
        }
        
        audio_out.close();
        audio_out.drain();
        din.close();
    }

    private AudioFormat getaudioformat() {
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.voice;

import halo.Halo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Phan Hieu
 */
public class VoiceRecorder extends Thread {

    private TargetDataLine audio_in = null;
    private DatagramSocket dout;
    private byte byte_buff[] = new byte[512];
    private InetAddress server_ip;
    private int server_port;

    public VoiceRecorder(String ip, int port) throws SocketException, UnknownHostException, LineUnavailableException {
        AudioFormat format = getaudioformat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("not suport");
            Halo.isCalling = false;
        }
        this.audio_in = (TargetDataLine) AudioSystem.getLine(info);
        this.audio_in.open(format);
        this.audio_in.start();

        this.dout = new DatagramSocket();
        this.server_ip = InetAddress.getByName(ip);
        this.server_port = port;
    }

    @Override
    public void run() {
        while (Halo.isCalling) {
            try {
                audio_in.read(byte_buff, 0, byte_buff.length);
                DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, server_ip, server_port);
                dout.send(data);
            } catch (IOException ex) {
                Logger.getLogger(VoiceRecorder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        audio_in.close();
        audio_in.drain();
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

package halo.models;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Phan Hieu
 */
public class Packet {

    public static byte INITITALIZE = 2;
    public static byte SEPARATOR = 4;

    public final static String COMMAND_SEND_TEXT = "123";
    public final static String COMMAND_SEND_FILE_NAME = "124";
    public final static String COMMAND_REQUEST_SEND_FILE_DATA = "125";
    public final static String COMMAND_SEND_FILE_DATA = "126";
    public final static String COMMAND_SEND_FINISH = "127";

    /**
     *
     * @param fromUsername
     * @param command 123: gửi text. 124: gửi file name.
     * @param data
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public static byte[] CreateDataPacket(String fromUsername, String command, byte[] data) throws UnsupportedEncodingException, IOException {
        if (data.length == 0) {
            throw new IOException();
        }

        byte[] initialize = new byte[1];
        initialize[0] = INITITALIZE;
        byte[] separator = new byte[1];
        separator[0] = SEPARATOR;
        byte[] from_username = fromUsername.getBytes("UTF8");
        byte[] cmd = command.getBytes("UTF8");
        byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
        byte[] packet = new byte[initialize.length + from_username.length + separator.length + cmd.length + data_length.length + separator.length + data.length];

        int packet_offset = 0;
        System.arraycopy(initialize, 0, packet, packet_offset, initialize.length);
        packet_offset += initialize.length;
        System.arraycopy(from_username, 0, packet, packet_offset, from_username.length);
        packet_offset += from_username.length;
        System.arraycopy(separator, 0, packet, packet_offset, separator.length);
        packet_offset += separator.length;
        System.arraycopy(cmd, 0, packet, packet_offset, cmd.length);
        packet_offset += cmd.length;
        System.arraycopy(data_length, 0, packet, packet_offset, data_length.length);
        packet_offset += data_length.length;
        System.arraycopy(separator, 0, packet, packet_offset, separator.length);
        packet_offset += separator.length;
        System.arraycopy(data, 0, packet, packet_offset, data.length);
        //packet_offset+=data.length;
        return packet;
    }

    public static byte[] ReadStream(DataInputStream din) throws IOException, NullPointerException  {
        byte[] data_buff = null;
        int b = 0;
        String buff_length = "";
        while ((b = din.read()) != SEPARATOR) {
            buff_length += (char) b;
        }
        int data_length = Integer.parseInt(buff_length);
        if(data_length==0)
            throw new NullPointerException();
        
        data_buff = new byte[Integer.parseInt(buff_length)];
        int byte_read = 0;
        int byte_offset = 0;
        while (byte_offset < data_length) {
            byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
            byte_offset += byte_read;
        }
        return data_buff;
    }
}
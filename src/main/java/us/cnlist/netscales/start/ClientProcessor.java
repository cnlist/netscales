package us.cnlist.netscales.start;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;

public class ClientProcessor extends Thread {

    private Socket sock;

    public ClientProcessor(Socket s) {
        this.sock = s;
        setPriority(NORM_PRIORITY - 1);
    }

    public void run() {
        try {
            DataInputStream d = new DataInputStream(sock.getInputStream());
            int count = 0;
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://213.136.80.146:9092/C:/Users/admin/Downloads/h2/data/netscales", "admin", "admin");
            Statement st = conn.createStatement();
            byte[] data = new byte[4];
            byte[] end = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] entity = new byte[0];

            //DataInputStream dx = new DataInputStream(new FileInputStream(f));
            System.out.println("data from: " + sock.getInetAddress().getHostName());
            while (d.read(data) != -1) {


                entity = entityWriteBytes(entity, data);
                System.out.println("reading: " + bytesToHex(entity));

                if (Arrays.equals(data, end)) {
                    System.out.println("" + (entity.length - 1) + " bytes written");
                    try {
                        ByteArrayInputStream stream = new ByteArrayInputStream(entity);

                        stream.skip(3);
                        byte[] packUniqueIdA = new byte[4];
                        stream.read(packUniqueIdA);
                        int packetUniqueId = ByteBuffer.wrap(packUniqueIdA).order(ByteOrder.LITTLE_ENDIAN).getInt();

                        byte[] dataArray = new byte[8];
                        int channelNumber = 0;

                        while (stream.read(dataArray) != -1) {
                            int entityData = ByteBuffer.wrap(dataArray).order(ByteOrder.LITTLE_ENDIAN).getInt();
                            st.execute("INSERT INTO T_COLLECTOR_DATA (PACKET_ID, CHANNEL_ID, DATA, IP_ADDRESS) VALUES(" + packetUniqueId + "" +
                                    "," + channelNumber + "," + entityData + ",'" + sock.getInetAddress().getHostName() + "') ");
                            if (channelNumber == 2) {
                                channelNumber = 0;
                            } else {
                                channelNumber++;
                            }
                        }
                        break;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    entity = new byte[0];
                }

                count++;

            }

        } catch (Exception exc) {

            exc.printStackTrace();
        }

    }

    static byte[] entityWriteBytes(byte[] entity, byte[] data) {
        byte[] buffer = new byte[entity.length - 1 + 4];
        try {
            ByteArrayOutputStream s = new ByteArrayOutputStream(buffer.length);
            s.write(entity);
            s.write(data);
            buffer = s.toByteArray();
        } catch (Exception e) {
        }

        return buffer;
    }

    private final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}

package us.cnlist.netscales.components;

import org.springframework.transaction.annotation.Transactional;
import us.cnlist.netscales.repository.PacketEntity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Transactional
public class ServerRunnable implements Runnable {

    private Socket socket;

    private TcpServer serv;
    private List<PacketEntity> localCache = new CopyOnWriteArrayList<>();

    public void setSocket(Socket s, TcpServer r) {
        serv = r;
        socket = s;
    }

    @Override

    public void run() {
        System.out.println("connected client: " + socket.getInetAddress().getHostName());

        try {
            DataInputStream channel = new DataInputStream(socket.getInputStream());
            byte[] buffer = new byte[611];

            int channelIndex = 0;
            while (channel.read(buffer) != -1) {


                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                bis.skip(3);
                byte[] packId = new byte[4];
                bis.read(packId);

                Long pid = new Long(ByteBuffer.wrap(packId).order(ByteOrder.LITTLE_ENDIAN).getInt());
                byte[] channelData = new byte[4];
                while (bis.read(channelData) != -1) {


                    //reading the entity
                    PacketEntity entity = new PacketEntity();
                    entity.setRawData(buffer);
                    entity.setIpAddress(socket.getInetAddress().getHostAddress());
                    entity.setPacketUniqueId(pid);
                    entity.setChannelId(channelIndex);
                    entity.setSessionId(Ipc.sessionId);
                    entity.setTimestamp(new Date());
                    entity.setChannelData(new Long(ByteBuffer.wrap(channelData).order(ByteOrder.LITTLE_ENDIAN).getInt()));
                    if (entity.getChannelData() != -1L) {
                        if (serv.isServing()) {
                            localCache.add(entity);
                        }
                        channelIndex++;

                    }


                    if (channelIndex == 3) {
                        channelIndex = 0;
                    }

                }
                if (serv.isServing()) {
                    serv.free(localCache);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public long getLong(byte[] array, int offset) {
        return
                ((long) (array[offset] & 0xff) << 56) |
                        ((long) (array[offset + 1] & 0xff) << 48) |
                        ((long) (array[offset + 2] & 0xff) << 40) |
                        ((long) (array[offset + 3] & 0xff) << 32) |
                        ((long) (array[offset + 4] & 0xff) << 24) |
                        ((long) (array[offset + 5] & 0xff) << 16) |
                        ((long) (array[offset + 6] & 0xff) << 8) |
                        ((long) (array[offset + 7] & 0xff));
    }
}

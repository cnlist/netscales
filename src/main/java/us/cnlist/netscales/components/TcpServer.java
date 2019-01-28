package us.cnlist.netscales.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.cnlist.netscales.repository.EntityRepository;
import us.cnlist.netscales.repository.PacketEntity;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


@Component
public class TcpServer implements Server {
    @Autowired
    EntityRepository repository;
    @Value("${us.cnlist.netscales.tcpPort}")
    private int port;
    @Autowired
    private Executor taskExecutor;
    @Value("${us.cnlist.netscales.tcpHost}")
    private String host;
    private List<PacketEntity> cache = new ArrayList<>();
    @Value("${us.cnlist.netscales.maxClientNumber}")
    private int backlog;
    private ServerSocket serverSocket;

    private boolean serving = true;

    @Override

    public void start() {
        try {
            serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host));
            while (true) {
                ServerRunnable runnable = new ServerRunnable();
                runnable.setSocket(serverSocket.accept(), this);
                taskExecutor.execute(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

    @Transactional
    @Modifying
    public synchronized void save(PacketEntity entity) {
        System.out.println("saving entity,...");
        if (cache.size() == 500) {
            try {
                repository.saveAll(cache);
                cache.clear();
                //  repository.create(entity.getPacketUniqueId(), entity.getChannelId(), entity.getIpAddress(), entity.getChannelData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cache.add(entity);
        }
    }


}

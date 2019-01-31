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
    private boolean waiting;
    private boolean serving = false;

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isServing() {
        return serving;
    }

    public void setServing(boolean serving) {
        this.serving = serving;
    }

    @Override

    public void start() {
        try {
            serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host));
            repository.save(new PacketEntity());
            setServing(false);
            setWaiting(false);
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
    public synchronized void save() {

            try {

                repository.saveAll(cache);
               // System.out.println("saving packet from "+cache.get(0).getIpAddress()+);
                cache.clear();

                //  repository.create(entity.getPacketUniqueId(), entity.getChannelId(), entity.getIpAddress(), entity.getChannelData());
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public List<PacketEntity> getCache() {
        return cache;
    }


    public void free(List<PacketEntity> cache) {
        repository.saveAll(cache);
        if (waiting) {
            setServing(false);
            setWaiting(false);
            System.out.println("PAUSED");
        }
    }

}

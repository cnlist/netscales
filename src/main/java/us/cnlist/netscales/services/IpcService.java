package us.cnlist.netscales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.cnlist.netscales.components.Ipc;
import us.cnlist.netscales.components.TcpServer;

import javax.annotation.PostConstruct;

@Service
public class IpcService {

    @Autowired
    private Ipc ipc;
    @Autowired
    private TcpServer tcpServer;

    @PostConstruct
    public void processSignal() {
        ipc.CONNECT("INT", h -> {

            if (tcpServer.isServing()){



                       tcpServer.setWaiting(true);




            }else{
                Ipc.sessionId = System.currentTimeMillis();
                tcpServer.setServing(true);
                System.out.println("STARTED");
            }
        });

    }


}

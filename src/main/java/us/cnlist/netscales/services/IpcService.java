package us.cnlist.netscales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.cnlist.netscales.components.Ipc;

import javax.annotation.PostConstruct;

@Service
public class IpcService {

    @Autowired
    private Ipc ipc;

    @PostConstruct
    public void processSignal() {
        ipc.CONNECT("INT", h -> {
            System.err.println("Handled TERM signal.");
        });

    }


}

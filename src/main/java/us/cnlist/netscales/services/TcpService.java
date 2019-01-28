package us.cnlist.netscales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import us.cnlist.netscales.components.TcpServer;

@Service
@Configuration
public class TcpService {

    @Autowired
    TcpServer server;

    @Bean
    public void serverRunner() {
        System.out.println("Bean init... server runner");
        try {
            server.start();
        } catch (Exception e) {
        }

    }


}

package us.cnlist.netscales.components;

import org.springframework.stereotype.Component;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Component
public class Ipc {

    public static Long sessionId = System.currentTimeMillis();

    public void CONNECT(String signal, SignalHandler slot) {
        Signal sig = new Signal(signal);
        Signal.handle(sig, slot);
    }

}

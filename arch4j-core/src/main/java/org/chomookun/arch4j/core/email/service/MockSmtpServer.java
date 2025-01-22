package org.chomookun.arch4j.core.email.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Component
@Lazy(false)
@ConditionalOnProperty(prefix="spring.mail", name = "host", havingValue="127.0.0.1")
public class MockSmtpServer implements InitializingBean, DisposableBean {

    @Value("${spring.mail.port}")
    private Integer port;

    private GreenMail greenMail;

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSetup serverSetup = new ServerSetup(port,null, ServerSetup.PROTOCOL_SMTP);
        greenMail = new GreenMail(serverSetup);

        // only start, if port is open
        if(!isPortOpen()) {
            greenMail.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        if(greenMail != null) {
            greenMail.stop();
        }
    }

    private boolean isPortOpen() {
        try (Socket socket = new Socket()) {
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
            socket.connect(socketAddress, 3000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}

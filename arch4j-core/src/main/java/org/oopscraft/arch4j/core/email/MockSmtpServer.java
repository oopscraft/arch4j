package org.oopscraft.arch4j.core.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
        greenMail.start();
    }

    @Override
    public void destroy() throws Exception {
        greenMail.stop();
    }

}

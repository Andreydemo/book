package com.epam.cdp.batulin;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.hornetq.HornetQConfigurationCustomizer;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EnableScheduling
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class})
@EnableJms
public class JmsApplication {
    @Value("${queue.host}")
    private String messageQueueHost;
    @Value("${queue.port}")
    private String messageQueuePort;

    @Bean
    public HornetQConfigurationCustomizer hornetCustomizer() {
        return new HornetQConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                Set<TransportConfiguration> acceptors = configuration.getAcceptorConfigurations();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("host", messageQueueHost);
                params.put("port", messageQueuePort);
                TransportConfiguration tc = new TransportConfiguration(NettyAcceptorFactory.class.getName(), params);
                acceptors.add(tc);
            }
        };
    }

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "jms");
        SpringApplication.run(JmsApplication.class, args);
    }
}

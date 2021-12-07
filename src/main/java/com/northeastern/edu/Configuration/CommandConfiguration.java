package com.northeastern.edu.Configuration;

import com.northeastern.edu.MetricRepository;
import com.northeastern.edu.Command.CommandAPI;
import com.northeastern.edu.Command.NotificationCommand;
import com.northeastern.edu.Command.NotificationReceiver;
import com.northeastern.edu.Command.ReceiverAPI;
import com.northeastern.edu.Facade.DBOrderFacade;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.Observer.MailSender;
import com.northeastern.edu.Observer.OrderMetrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CommandConfiguration {
    
    @Autowired
    private DBUserFacade userFacade;

    @Autowired
    private DBOrderFacade orderFacade;

    @Autowired
    private MetricRepository metricRepository;

    @Bean
    @DependsOn({"orderFacade","userFacade","metricRepository"})
    public CommandAPI notificationCommandBean(){
        
        ReceiverAPI receiver = new NotificationReceiver();
        receiver.register(new MailSender(orderFacade,userFacade));
        receiver.register(new OrderMetrics(userFacade,metricRepository));
        CommandAPI command = new NotificationCommand(receiver);
        return command;
    }

}

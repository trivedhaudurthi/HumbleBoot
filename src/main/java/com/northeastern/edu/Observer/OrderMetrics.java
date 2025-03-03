package com.northeastern.edu.Observer;

import java.sql.Date;
import java.util.List;

import com.northeastern.edu.MetricRepository;
import com.northeastern.edu.Facade.DBOrderFacade;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.models.Metric;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class OrderMetrics implements ObserverAPI {

  
    private DBUserFacade userFacade;
    private MetricRepository metricRepository;



    

    public OrderMetrics(DBUserFacade userFacade, MetricRepository metricRepository) {
     
        this.userFacade = userFacade;
        this.metricRepository = metricRepository;
        
    }



    @Override
    public void send() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username= ((UserDetails) principal).getUsername();
        User user  = userFacade.findUserByEmail(username);
        int user_id = user.getId();
        Date cur = new Date(System.currentTimeMillis());
        List<Metric> metrics = metricRepository.findByUserIdAndDate(user_id, cur);
        Metric metric = null;
        if(metrics.isEmpty()){
            metric = new Metric();
            metric.setDate(cur);
            metric.setUserId(user_id);
        }
        else{
            metric = metrics.get(0);
        }
        metric.setFrequency(metric.getFrequency()+1);
        metricRepository.save(metric);

        StringBuffer sb= new StringBuffer();
        sb.append("\n");
        sb.append("Metric: \n");
        sb.append("User ").append(user.getName()).append(" #Id: ").append(user.getId()).append("\n");
        sb.append("Total Notifications: ").append(metric.getFrequency());

        System.out.println(sb.toString());
    }
}

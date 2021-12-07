package com.northeastern.edu.Observer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.northeastern.edu.Facade.DBOrderFacade;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class MailSender implements ObserverAPI {

   
    private DBOrderFacade orderFacade;


    private DBUserFacade userFacade;

    

    public MailSender(DBOrderFacade orderFacade, DBUserFacade userFacade) {
        this.orderFacade = orderFacade;
        this.userFacade = userFacade;
    }



    @Override
    public void send() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username= ((UserDetails) principal).getUsername();
        int user_id = userFacade.findUserByEmail(username).getId();
        UserOrder order = orderFacade.getLatestUserOrderByEmail(user_id, PageRequest.of(0,1,Sort.by("createdTime").descending()));
        System.out.println(order.getCreatedTime());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(order.getCreatedTime().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<UserOrder> allOrders = orderFacade.getLatestUserOrders(order.getCreatedTime(), user_id);
        System.out.println("Notifications "+ buildSummary(allOrders));
    }

    public String buildSummary(List<UserOrder> orders){

        StringBuffer sb = new StringBuffer();
        for(UserOrder order:orders){
            sb.append(order.getProductName()).append("       "+order.getQuantity()+"x").append("\n");
        }

        return sb.toString();
    }


}

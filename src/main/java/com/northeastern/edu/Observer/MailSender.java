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
        User user = userFacade.findUserByEmail(username);
        int user_id = user.getId();
        UserOrder order = orderFacade.getLatestUserOrderByEmail(user_id, PageRequest.of(0,1,Sort.by("createdTime").descending()));
        // System.out.println(order.getCreatedTime());
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(order.getCreatedTime().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<UserOrder> allOrders = orderFacade.getLatestUserOrders(order.getCreatedTime(), user_id);
        StringBuffer sb= new StringBuffer();
        sb.append("Notification \n");
        sb.append("User: ").append(user.getName()).append(" #Id: ").append(user.getId()).append("\n");
        sb.append("Has ordered: \n");
        sb.append(buildSummary(allOrders));
        sb.append("On: ").append(date.toString());
        System.out.println(sb.toString());
    }

    public String buildSummary(List<UserOrder> orders){

        StringBuffer sb = new StringBuffer();
        for(UserOrder order:orders){
            sb.append("Product Name:").append(order.getProductName()).append("    Quantity: ").append("  "+order.getQuantity()+"x").append("\n");
        }

        return sb.toString();
    }


}

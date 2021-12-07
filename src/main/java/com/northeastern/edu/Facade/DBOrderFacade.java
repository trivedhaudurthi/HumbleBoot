package com.northeastern.edu.Facade;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.northeastern.edu.OrderRepository;
import com.northeastern.edu.PaymentRepository;
import com.northeastern.edu.models.Payment;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component("orderFacade")
public class DBOrderFacade {
    
    private OrderRepository orderRepository;

    private PaymentRepository paymentRepository;

    public DBOrderFacade(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public void recordPayment(Payment payment){
        this.paymentRepository.save(payment);
    }

    public List<UserOrder> findOrdersByUserId(int id){
        return this.orderRepository.findByUserId(id);
    }
	public List<UserOrder> findOrdersBySellerId(int id){
        return this.orderRepository.findBySellerId(id);
    }
    
    public Optional<UserOrder> findOrderById(int id){
        return orderRepository.findById(id);
    }

    public UserOrder saveOrder(UserOrder order){
        return orderRepository.save(order);
    }

    public UserOrder getLatestUserOrderByEmail(int id,Pageable pageable){
        List<UserOrder> orders=orderRepository.findLatestUserOrderByUserId(id, pageable);
        System.out.println(orders);
        if(orders.isEmpty()){
            return null;
        }
        return orders.get(0);
    }

    public List<UserOrder> getLatestUserOrders(Timestamp time, int id){
        List<UserOrder> orders = orderRepository.findLatestUserOrders(time, id);
        return orders;
    }
}

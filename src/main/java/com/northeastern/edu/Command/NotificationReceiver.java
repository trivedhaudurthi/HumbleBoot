package com.northeastern.edu.Command;

import java.util.ArrayList;
import java.util.List;

import com.northeastern.edu.Observer.ObserverAPI;

public class NotificationReceiver implements ReceiverAPI {

    private List<ObserverAPI> observers;

    public NotificationReceiver(){
        this.observers = new ArrayList<>();
    }

    @Override
    public void operation() {
       for(ObserverAPI observer:observers){
           observer.send();
       }
    }

    @Override
    public void register(ObserverAPI observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(ObserverAPI observer) { 
        observers.remove(observer);
    }
    
}

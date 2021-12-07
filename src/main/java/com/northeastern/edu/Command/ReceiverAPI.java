package com.northeastern.edu.Command;

import com.northeastern.edu.Observer.ObserverAPI;

public interface ReceiverAPI {
    public void register(ObserverAPI observer);
    public void operation();
    public void unregister(ObserverAPI observer);
}

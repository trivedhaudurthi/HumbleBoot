package com.northeastern.edu.Command;

public class NotificationCommand implements CommandAPI {

    private ReceiverAPI receiver;
    
    public NotificationCommand(ReceiverAPI receiver) {
        this.receiver = receiver;
    }

    public ReceiverAPI getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverAPI receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
       
       receiver.operation(); 
    }
    
}

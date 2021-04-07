package model.strategy;

import model.Server;

import java.util.Comparator;

public interface Strategy {
    Strategy BY_CLIENTS_IN_QUEUE=new QueueQuantity();
    Strategy BY_WAITING_TIME=new QueueWaitingTime();
    public Comparator<Server> getComparator();
}


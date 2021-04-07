package model.strategy;

import model.Server;

import java.util.Comparator;

public class QueueQuantity implements Strategy{
    Comparator<Server> criteria;
    public QueueQuantity(){
        criteria=new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return o1.getQueueSize()-o2.getQueueSize();
            }
        };
    }
    @Override
    public Comparator<Server> getComparator() {
        return criteria;
    }
}


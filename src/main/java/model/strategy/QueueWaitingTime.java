package model.strategy;

import model.Server;

import java.util.Comparator;

public class QueueWaitingTime implements Strategy{
    Comparator<Server> criteria;
    public QueueWaitingTime(){
        criteria=new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return o1.getServingTime()-o2.getServingTime();
            }
        };
    }
    @Override
    public Comparator<Server> getComparator() {
        return criteria;
    }
}



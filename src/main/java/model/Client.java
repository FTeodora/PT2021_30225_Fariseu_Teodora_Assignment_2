package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Comparable{
    private final int id, arrivalTime;
    private final AtomicInteger servingTime;
    public Client(int id,int arrivalTime,int servingTime){
        this.id=id;
        this.arrivalTime=arrivalTime;
        this.servingTime=new AtomicInteger(servingTime);
    }
    public void beingServed(){
        this.servingTime.getAndDecrement();
    }
    public int getArrivalTime(){
        return this.arrivalTime;
    }
    public int getServingTime(){
        return this.servingTime.get();
    }
    public int getID(){
        return this.id;
    }
    @Override
    public String toString() {
        return "("+id+","+arrivalTime+","+servingTime+")";
    }
    @Override
    public int compareTo(Object o) {
        Client obj=(Client)o;
        int i=this.arrivalTime-obj.arrivalTime;
        if(i!=0)
            return i;
        return this.servingTime.get()-obj.servingTime.get();
    }
}



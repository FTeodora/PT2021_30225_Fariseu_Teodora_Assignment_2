package model;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private final BlockingQueue<Client> pendingClients;
    private final AtomicInteger servingTime;
    private final int id;
    private SimulationManager notifier;
    private final Thread serverThread;
    public Server(int id){
        this.id=id;
        pendingClients=new LinkedBlockingQueue<>();
        servingTime=new AtomicInteger();
        serverThread=new Thread(this);

    }
    public void addClient(Client client){
        try{
            this.pendingClients.put(client);
            servingTime.addAndGet(client.getServingTime());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void setNotifier(SimulationManager notifier){ this.notifier=notifier;}
    public int getId(){  return this.id; }
    public int getQueueSize(){
        return this.pendingClients.size();
    }
    public BlockingQueue<Client> getQueueClients(){
        return this.pendingClients;
    }
    public int getServingTime(){ return this.servingTime.get();}
    private void serveClient(){
        this.pendingClients.peek().beingServed();
        this.servingTime.decrementAndGet();
    }
    private void refreshQueue() throws InterruptedException{
        if(pendingClients.isEmpty()||pendingClients.peek().getServingTime()==0)
            this.pendingClients.take();
    }
    public void start(){
        serverThread.start();
    }
    public void stop(){
        serverThread.interrupt();
    }
    @Override
    public String toString() {
        StringBuilder rez=new StringBuilder("Queue ").append(id).append(" : ");
        if(this.pendingClients.size()==0)
            return rez.append("closed").toString();
        for(Client i:this.pendingClients)
            rez.append(i.toString()).append(",");
        rez=new StringBuilder(rez.substring(0,rez.length()-1));
        return rez.toString();
    }
    @Override
    public void run() {
            while(true){
                    try {
                        if(!this.pendingClients.isEmpty()){
                            serveClient();
                            refreshQueue();
                        }
                        SimulationManager.waitOnBarrier();
                        synchronized (notifier){ notifier.wait();}
                    } catch (InterruptedException | BrokenBarrierException e) { return; }
        }
    }
}

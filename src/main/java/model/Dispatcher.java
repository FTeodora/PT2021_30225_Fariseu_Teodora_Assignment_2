package model;

import model.strategy.Strategy;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Dispatcher implements Runnable{
    private final PriorityBlockingQueue<Client> waitingClients;
    private final List<Server> queues;
    private final AtomicInteger currentTime;
    private final Strategy queuePriorityCriteria;
    private final Statistics stats;
    private BufferedWriter saveFile;
    public Dispatcher(PriorityBlockingQueue<Client> waitingClients, int queuesAmount, Strategy queuePriorityCriteria){
        this.queuePriorityCriteria=queuePriorityCriteria;
        this.waitingClients=waitingClients;
        this.queues=Collections.synchronizedList(new LinkedList<>());
        for(int i=0;i<queuesAmount;i++) {
            this.queues.add(new Server(i+1)); }
        this.currentTime=new AtomicInteger(0);
        stats=new Statistics();
    }
    public void setNotifier(SimulationManager t){
        for(Server i:queues) i.setNotifier(t);
    }
    public PriorityBlockingQueue<Client> getWaitingClients() {
        return waitingClients;
    }
    public List<Server> getQueues() {
        return queues;
    }
    public void incrementTime(){ currentTime.incrementAndGet(); }
    public boolean checkIfEmpty(){
        AtomicInteger clientsInQueues=new AtomicInteger();
        for(Server i:queues){
            clientsInQueues.addAndGet(i.getQueueSize());
        }
        stats.updatePeakTime(currentTime,clientsInQueues);
        stats.updateCurrentClientsBeingServed(clientsInQueues);
        return clientsInQueues.get()==0 && waitingClients.isEmpty();
    }
    public String getInterfaceStats(){ return stats.getInterfaceStats(); }
    public void dispatchNewTask() throws InterruptedException{
        Server min= Collections.min(queues,queuePriorityCriteria.getComparator());
        Client readyClient=this.waitingClients.take();
        min.addClient(readyClient);
        stats.updateWaitingTime(min.getServingTime());
        stats.updateServingTime(readyClient.getServingTime());
        stats.updateServedClients();
    }
    public boolean canDispatch(){ return (!waitingClients.isEmpty())&&(waitingClients.peek().getArrivalTime()<=currentTime.get()); }
    public int getCurrentTime(){   return currentTime.get();}
    public void startThreads(){ for(Server i:queues) i.start();}
    public void stopThreads(){ for(Server i:queues) i.stop();}
    public void createFile() throws IOException {
        File dir=new File("Log Files");
        dir.mkdir();
        File saveFile=new File(dir,new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+"_log.txt");
        saveFile.createNewFile();
        this.saveFile=new BufferedWriter(new FileWriter(saveFile));
    }
    public void writeStats(){
        try{
            saveFile.write(stats.getFileStats());
            saveFile.flush();
        }catch (IOException e){

        }
    }
    public void closeFile(){
        try {
            saveFile.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error closing log file","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public String toString() {
        StringBuilder rez=new StringBuilder("Time: ").append(currentTime).append("\nWaiting clients: ");
        for(Client i:waitingClients)
            rez.append(i).append(",");
        rez=new StringBuilder(rez.substring(0,rez.length()-1));
        rez.append("\n");
        for(Server i:queues)
            rez.append(i).append("\n");
        return rez.toString();
    }
    @Override
    public void run() {
        try{
            while(canDispatch())
                dispatchNewTask();
            saveFile.write(toString());
            saveFile.newLine();
            saveFile.flush();
        }catch(InterruptedException  e1){
        }
        catch(IOException  e2){
            JOptionPane.showMessageDialog(null,"Error writing in log file","Error",JOptionPane.ERROR_MESSAGE);
            closeFile();
            return;
        }
    }
}

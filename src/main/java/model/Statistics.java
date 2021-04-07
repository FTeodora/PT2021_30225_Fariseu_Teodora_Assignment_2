package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private final AtomicInteger totalClientsServed; //clientii serviti in cozi pana acum
    private final AtomicInteger totalWaiting; //timpul total de asteptare pentru servirea la coada
    private final AtomicInteger currentClientsBeingServed; //clientii care asteapta in cozi la un anumit moment
    private final AtomicInteger peakTime; //"ora" de varf
    private final AtomicInteger peakTimeClientAmount; //total clienti in cozi ora de varf
    private final AtomicInteger totalServing; // suma servingTime-ului clientilor din coada
    public Statistics(){
        totalClientsServed=new AtomicInteger();
        totalWaiting=new AtomicInteger();
        currentClientsBeingServed=new AtomicInteger();
        peakTime=new AtomicInteger();
        peakTimeClientAmount=new AtomicInteger();
        totalServing=new AtomicInteger();
    }
    void updatePeakTime(AtomicInteger time,AtomicInteger clientAmount){
        if(clientAmount.get()>this.peakTimeClientAmount.get()) {
            this.peakTime.set(time.get());
            this.peakTimeClientAmount.set(clientAmount.get());
        }
    }
    public void updateWaitingTime(int addedTime){ this.totalWaiting.addAndGet(addedTime); }
    public void updateServingTime(int addedTime){ this.totalServing.addAndGet(addedTime); }
    public void updateServedClients(){ totalClientsServed.incrementAndGet(); }
    public double computeAvgWaiting(){ return 1.0*totalWaiting.get()/totalClientsServed.get();}
    public double computeAvgServing(){ return 1.0*totalServing.get()/totalClientsServed.get();}
    public void updateCurrentClientsBeingServed(AtomicInteger currentClientsBeingServed) { this.currentClientsBeingServed.set(currentClientsBeingServed.get()); }
    public String getInterfaceStats(){
        StringBuilder rez=new StringBuilder();
        rez.append("Clients in queues:").append(currentClientsBeingServed).append("     ");
        rez.append("Served:").append(totalClientsServed).append("     ");
        return rez.toString();
    }
    public String getFileStats(){
        StringBuilder rez=new StringBuilder();
        rez.append("Peak time:").append(peakTime).append(" with ").append(peakTimeClientAmount).append(" clients\n");
        rez.append("Average waiting time:").append(computeAvgWaiting()).append(" with a total waiting time of ").append(totalWaiting).append("\n");
        rez.append("Average serving time:").append(computeAvgServing()).append(" with a total serving time of ").append(totalServing).append("\n");
        return rez.toString();
    }
}

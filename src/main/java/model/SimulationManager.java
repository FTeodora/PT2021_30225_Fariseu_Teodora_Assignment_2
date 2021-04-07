package model;

import view.windows.SimulationWindow;
import model.strategy.Strategy;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.PriorityBlockingQueue;

public class SimulationManager  implements Runnable{
    int maxRunningTime;
    final Dispatcher log;
    SimulationWindow windowDisplay;
    private final Thread objectThread;
    public volatile boolean isPaused;
    private static CyclicBarrier barrier;
    public SimulationManager(int clients,int queues,int runningTime,int minArrival,int maxArrival,int minServing,int maxServing,Strategy strategy){
        PriorityBlockingQueue<Client> waitingClients=new PriorityBlockingQueue<>();
        for(int i=0;i<clients;i++)
            waitingClients.add(new Client(i+1,generateNumberInInterval(minArrival,maxArrival),generateNumberInInterval(minServing,maxServing)));
        this.log=new Dispatcher(waitingClients,queues, strategy);
        barrier=new CyclicBarrier(queues+1,log);
        log.setNotifier(this);
        this.maxRunningTime=runningTime;
        log.startThreads();
        try {
            log.createFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error creating file","Error",JOptionPane.ERROR_MESSAGE);
        }
        windowDisplay=new SimulationWindow(log);
        objectThread=new Thread(this);

    }
    private static int generateNumberInInterval(int minBound,int maxBound){
        return (minBound + new Random().nextInt((maxBound-minBound)+1));
    }
    @Override
    public void run() {
        try{
        while(true) {
            if(isPaused){
            synchronized (windowDisplay.getTogglePause()){  windowDisplay.getTogglePause().wait(); }
            }else{
                synchronized (this){  notifyAll(); }
                barrier.await();
                boolean toBreak=log.checkIfEmpty();
                windowDisplay.refresh(log);
                if(toBreak||log.getCurrentTime()==maxRunningTime)
                    break;
                Thread.sleep(1000);
                log.incrementTime();
                }
            }
        }
        catch(InterruptedException | BrokenBarrierException e){
            JOptionPane.showMessageDialog(windowDisplay,"Simulation has been stopped","Back to Menu",JOptionPane.INFORMATION_MESSAGE);
        }finally {
            log.stopThreads();
            log.writeStats();
            log.closeFile();
            if(windowDisplay!=null)
                windowDisplay.disablePause();
        }
    }
    public void start(){
        objectThread.start();
    }
    public void stop(){
        objectThread.interrupt();
        windowDisplay.dispose();
    }
    public void pause(){
        isPaused=true;
    }
    public void unpause(){
        isPaused=false;
        synchronized (windowDisplay.getTogglePause()) {
            windowDisplay.getTogglePause().notifyAll();
        }
    }
    public static void waitOnBarrier() throws BrokenBarrierException, InterruptedException { barrier.await(); }
    public void addWindowBackListener(ActionListener l){
        windowDisplay.addBackButtonListener(l);
    }
    public void addWindowPauseListener(ActionListener l){
        windowDisplay.addPauseButtonListener(l);
    }

}

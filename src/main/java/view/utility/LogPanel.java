package view.utility;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

public class LogPanel {
    JScrollPane components;
    public LogPanel(Dispatcher dispatcher){
        JPanel content=new JPanel();
        components=new JScrollPane(content);
        content.setBorder(new EmptyBorder(2,2,2,2));
        content.setAutoscrolls(true);
        content.setMinimumSize(new Dimension(1030,500));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.add(waitingClientsRow(dispatcher.getWaitingClients()),Component.LEFT_ALIGNMENT);
        for(Server i:dispatcher.getQueues())
            content.add(queueRow(i),Component.LEFT_ALIGNMENT);
        content.validate();
        content.repaint();
    }
    private void putClients(Collection<Client> clients,String messageIfNull,JPanel target){
        if(clients.isEmpty()){
            target.add(new JLabel(messageIfNull));
        }else{

            int i=1;
            Iterator<Client> clientsIterator=clients.iterator();
            while(i<=clients.size()&&i<=10)
            {
                i++;
                target.add(InfoIcon.createImage(clientsIterator.next()));
            }
            if(i<clients.size())
                target.add(new JLabel(" ... and "+(clients.size()-i+1)+" more"));
        }
    }
    private JPanel waitingClientsRow(PriorityBlockingQueue<Client> clients){
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JScrollPane cont=new JScrollPane(row);
        cont.setBorder(new EmptyBorder(2,2,2,2));
        row.setAutoscrolls(true);
        row.setPreferredSize(new Dimension(1010,95));
        row.setMinimumSize(new Dimension(1010,95));
        putClients(clients,"There are no more clients waiting",row);
        return row;
    }
    private JPanel queueRow(Server server){
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JScrollPane cont=new JScrollPane(row);
        cont.setBorder(new EmptyBorder(2,2,2,2));
        row.setAutoscrolls(true);
        row.setPreferredSize(new Dimension(910,95));
        row.setMinimumSize(new Dimension(910,95));
        row.add(InfoIcon.createImage(server));
        putClients(server.getQueueClients(),"Closed",row);
        return row;
    }
    public JScrollPane getPanel(){
        return this.components;
    }
}



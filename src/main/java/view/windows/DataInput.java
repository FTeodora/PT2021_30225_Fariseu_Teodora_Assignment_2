package view.windows;

import model.Client;
import model.SimulationManager;
import model.strategy.Strategy;
import view.BadInputException;
import view.utility.InfoIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class DataInput extends JFrame {
    private Font DATA_FONT=new Font("Tahoma",Font.PLAIN,16);
    JTextField clients,queues,runningTime,minArrival,maxArrival,minServing,maxServing;
    JRadioButton size,time;
    JButton start;
    public DataInput(){
        super("Queues simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680,600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel center=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel content=new JPanel();
        content.setBorder(new EmptyBorder(10,10,10,10));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setMaximumSize(new Dimension(640,300));


        JPanel clientNO=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel tempLabel1=new JLabel("Number of clients:");
        tempLabel1.setFont(DATA_FONT);
        clientNO.add(tempLabel1);
        clients=new JTextField();
        clients.setPreferredSize(new Dimension(50,30));
        clients.setFont(DATA_FONT);
        clientNO.add(clients);
        content.add(clientNO,Component.LEFT_ALIGNMENT);

        JPanel queueNO=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel tempLabel2=new JLabel("Number of queues:");
        tempLabel2.setFont(DATA_FONT);
        queueNO.add(tempLabel2);
        queues=new JTextField();
        queues.setPreferredSize(new Dimension(50,30));
        queues.setFont(DATA_FONT);
        queueNO.add(queues);
        content.add(queueNO,Component.LEFT_ALIGNMENT);

        JPanel maxTime=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel tempLabel3=new JLabel("Maximum simulation time:");
        tempLabel3.setFont(DATA_FONT);
        maxTime.add(tempLabel3);
        runningTime=new JTextField();
        runningTime.setPreferredSize(new Dimension(50,30));
        runningTime.setFont(DATA_FONT);
        maxTime.add(runningTime);
        content.add(maxTime,Component.LEFT_ALIGNMENT);

        JPanel arrivalRange=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel tempLabel4=new JLabel("Client arrival time generation interval:");
        tempLabel4.setFont(DATA_FONT);
        arrivalRange.add(tempLabel4);
        minArrival=new JTextField();
        maxArrival=new JTextField();
        minArrival.setPreferredSize(new Dimension(50,30));
        maxArrival.setPreferredSize(new Dimension(50,30));
        minArrival.setFont(DATA_FONT);
        maxArrival.setFont(DATA_FONT);
        arrivalRange.add(minArrival);
        arrivalRange.add(maxArrival);
        content.add(arrivalRange,Component.LEFT_ALIGNMENT);

        JPanel servingRange=new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel tempLabel5=new JLabel("Client serving time generation interval:");
        tempLabel5.setFont(DATA_FONT);
        servingRange.add(tempLabel5);
        minServing=new JTextField();
        maxServing=new JTextField();
        minServing.setPreferredSize(new Dimension(50,30));
        maxServing.setPreferredSize(new Dimension(50,30));
        minServing.setFont(DATA_FONT);
        maxServing.setFont(DATA_FONT);
        servingRange.add(minServing);
        servingRange.add(maxServing);
        content.add(servingRange,Component.LEFT_ALIGNMENT);

        JPanel strategy=new JPanel(new FlowLayout(FlowLayout.LEADING));
        ButtonGroup strategies=new ButtonGroup();
        JLabel tempLabel6=new JLabel("Queue distribution strategy by:");
        tempLabel6.setFont(DATA_FONT);
        strategy.add(tempLabel6);
        time=new JRadioButton("queue waiting time");
        time.setSelected(true);
        time.setFont(DATA_FONT);
        size=new JRadioButton("clients in queue");
        size.setFont(DATA_FONT);
        strategies.add(time);
        strategies.add(size);
        strategy.add(time);
        strategy.add(size);
        content.add(strategy,Component.LEFT_ALIGNMENT);

        center.setBorder(new EmptyBorder(50,50,50,50));
        center.add(content);
        this.add(center,BorderLayout.CENTER);
        center.validate();
        center.repaint();

        JPanel footer=new JPanel(new FlowLayout(FlowLayout.TRAILING));
        start=new JButton("Start");
        footer.add(start);
        this.add(footer,BorderLayout.SOUTH);

        setVisible(true);
    }
    public void addStartButtonListener(ActionListener listener){
        start.addActionListener(listener);
    }
    public SimulationManager retrieveData() throws BadInputException {
        int client,queue,maxT,arr1,arr2,serv1,serv2;
        Strategy strategy;
        if((client=parseNumber(clients))==-1)
            throw new BadInputException("Number of clients",getNumberCause(clients.getText()));
        if((queue=parseNumber(queues))==-1)
            throw new BadInputException("Number of queues",getNumberCause(queues.getText()));
        if((maxT=parseNumber(runningTime))==-1)
            throw new BadInputException("Max time",getNumberCause(runningTime.getText()));
        if((arr1=parseNumber(minArrival))==-1)
            throw new BadInputException("Min arrival",getNumberCause(minArrival.getText()));
        if((arr2=parseNumber(maxArrival))==-1)
            throw new BadInputException("Max arrival",getNumberCause(maxArrival.getText()));
        if(arr2<arr1)
            throw new BadInputException("Max arrival must be greater or equal than min arrival");
        if((serv1=parseNumber(minServing))==-1)
            throw new BadInputException("Min serving",getNumberCause(minServing.getText()));
        if((serv2=parseNumber(maxServing))==-1)
            throw new BadInputException("Max serving",getNumberCause(maxServing.getText()));
        if(serv2<serv1)
            throw new BadInputException("Max serving must be greater or equal than min serving");
        if(time.isSelected())
            strategy=Strategy.BY_WAITING_TIME;
        else
            strategy=Strategy.BY_CLIENTS_IN_QUEUE;
        return new SimulationManager(client,queue,maxT,arr1,arr2,serv1,serv2,strategy);
    }
    private int parseNumber(JTextField number){
        try{
            return Integer.parseInt(number.getText().trim());
        }catch(NumberFormatException e){
            return -1;
        }
    }
    private String getNumberCause(String number){
        number=number.trim();
        if(number.equals(""))
            return "Field is empty";
        if(number.contains("."))
            return "Please enter an Integer!";
        if(number.startsWith("-"))
            return "Please enter a positive number!";
        try{
            int nr;
            nr=Integer.parseInt(number);
            if(nr==0)
                return "Field can't be zero";
        }catch(NumberFormatException e){
            return "Please enter only digits";
        }
        return "reason unknown";
    }
}

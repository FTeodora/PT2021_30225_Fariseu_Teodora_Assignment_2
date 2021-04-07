package view.windows;

import model.Dispatcher;
import view.utility.LogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationWindow extends JFrame {
    private final Color BG=new Color(43,43,43);
    private final Color FG=new Color(164,177,186);
    private final Font STATS=new Font("Tahoma",Font.BOLD,18);
    private final JButton back,togglePause;
    private LogPanel currentQueues;
    private JLabel currentTime, currentStats;
    public SimulationWindow(Dispatcher dispatcher){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050,700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        JPanel header=new JPanel(new FlowLayout(FlowLayout.LEADING));
        back=new JButton("Back");
        header.add(back);
        header.setBackground(BG);
        add(header, BorderLayout.NORTH);

        currentQueues=new LogPanel(dispatcher);
        add(currentQueues.getPanel(),BorderLayout.CENTER);

        togglePause=new JButton("pause");
        header.add(togglePause);
        JLabel tmp=new JLabel("Current Time: ");
        tmp.setForeground(FG);
        tmp.setFont(STATS);
        header.add(tmp);
        currentTime=new JLabel(Integer.toString(dispatcher.getCurrentTime()));
        currentTime.setForeground(FG);
        currentTime.setFont(STATS);
        header.add(currentTime);

        JPanel footer=new JPanel(new FlowLayout(FlowLayout.TRAILING));
        currentStats=new JLabel(dispatcher.getInterfaceStats());
        currentStats.setForeground(FG);
        currentStats.setFont(STATS);
        footer.add(currentStats);
        footer.setBackground(BG);
        add(footer,BorderLayout.SOUTH);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }
    public void refresh(Dispatcher updatedInfo){
        currentTime.setText(Integer.toString(updatedInfo.getCurrentTime()));
        remove(currentQueues.getPanel());
        currentQueues=new LogPanel(updatedInfo);
        add(currentQueues.getPanel(),BorderLayout.CENTER);
        currentStats.setText(updatedInfo.getInterfaceStats());
        getContentPane().validate();
        getContentPane().repaint();
    }
    public JButton getTogglePause(){ return togglePause;}
    public void disablePause(){ togglePause.setEnabled(false);}
    public void addPauseButtonListener(ActionListener l){
        togglePause.addActionListener(l);
    }
    public void addBackButtonListener(ActionListener l){
        back.addActionListener(l);
    }
}


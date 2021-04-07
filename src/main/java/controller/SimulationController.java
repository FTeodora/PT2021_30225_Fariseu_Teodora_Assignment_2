package controller;

import view.windows.DataInput;
import model.SimulationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationController {
    SimulationManager model;
    DataInput prev;
    SimulationController(DataInput prev,SimulationManager manager){
        this.prev=prev;
        model=manager;
        model.start();
        manager.addWindowBackListener(new BackButtonListener());
        manager.addWindowPauseListener(new PauseButtonListener());

    }
    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.stop();
            prev.setVisible(true);
        }
    }
    class PauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source=(JButton)(e.getSource());
            if(source.getText().equals("pause")){
                source.setText("continue");
                model.pause();
            }
            else{
                source.setText("pause");
                model.unpause();
            }
        }
    }
}


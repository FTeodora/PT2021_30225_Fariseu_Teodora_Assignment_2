package controller;

import view.BadInputException;
import view.windows.DataInput;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataInputController {
    DataInput window;
    public DataInputController(){
        window=new DataInput();
        window.addStartButtonListener(new StartButtonListener());
    }
    class StartButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                new SimulationController(window, window.retrieveData());
                window.setVisible(false);

            }catch(BadInputException er){
                JOptionPane.showMessageDialog(window,er.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}

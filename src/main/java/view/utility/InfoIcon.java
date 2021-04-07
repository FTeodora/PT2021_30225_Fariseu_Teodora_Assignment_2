package view.utility;

import model.Client;
import model.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InfoIcon extends JPanel {
    BufferedImage image;
    private InfoIcon(Client client){
        setLayout(new GridBagLayout());
        GridBagConstraints c1=new GridBagConstraints();
        c1.weightx=2.0;
        c1.gridx=1;
        c1.gridy=1;
        JLabel idLabel=new JLabel("#"+client.getID());
        idLabel.setForeground(Color.BLACK);
        idLabel.setBackground(Color.LIGHT_GRAY);
        idLabel.setOpaque(true);
        idLabel.setFont(new Font("Calibri",Font.BOLD,16));
        add(idLabel,c1);

        JLabel arrivalLabel=new JLabel(Integer.toString(client.getArrivalTime()));
        GridBagConstraints c2=new GridBagConstraints();
        c2.weightx=1.0;
        c2.weighty=5.0;
        c2.gridy=7;
        arrivalLabel.setForeground(Color.BLACK);
        arrivalLabel.setBackground(Color.LIGHT_GRAY);
        arrivalLabel.setFont(new Font("Calibri",Font.BOLD,14));
        arrivalLabel.setOpaque(true);
        add(arrivalLabel,c2);

        JLabel servingLabel=new JLabel(Integer.toString(client.getServingTime()));
        GridBagConstraints c3=new GridBagConstraints();
        c3.weightx=1.0;
        c3.weighty=5.0;
        c3.gridy=7;
        c3.gridx=2;
        servingLabel.setForeground(Color.BLACK);
        servingLabel.setBackground(Color.LIGHT_GRAY);
        servingLabel.setFont(new Font("Calibri",Font.BOLD,14));
        servingLabel.setOpaque(true);
        add(servingLabel,c3);
        setBorder(new EmptyBorder(14,2,0,2));
        this.setPreferredSize(new Dimension(80,80));
        this.setMinimumSize(new Dimension(80,80));
    }
    private InfoIcon(Server server){
        JLabel idLabel=new JLabel(Integer.toString(server.getId()));
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Calibri",Font.BOLD,13));
        add(idLabel);

        JLabel sizeLabel=new JLabel(server.getQueueSize()+" clients");
        sizeLabel.setForeground(Color.LIGHT_GRAY);
        sizeLabel.setFont(new Font("Calibri",Font.BOLD,13));
        add(sizeLabel);

        JLabel timeLabel=new JLabel(server.getServingTime()+" sec");
        timeLabel.setForeground(Color.LIGHT_GRAY);
        timeLabel.setFont(new Font("Calibri",Font.BOLD,13));
        add(timeLabel);

        this.setPreferredSize(new Dimension(56,80));
        this.setMinimumSize(new Dimension(56,80));
    }
    private static BufferedImage getImage(String fileName){
        File f=new File(System.getProperty("user.dir")+"/src/main/resources");
        BufferedImage img = null;
        try {
            File imf=new File(f,"/"+fileName);
            img = ImageIO.read(imf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    public static InfoIcon createImage(Client client){
        InfoIcon rez= new InfoIcon(client);
        rez.image=getImage("client.png");
        rez.paintComponent(rez.image.getGraphics());
        return rez;
    }
    public static InfoIcon createImage(Server server){
        InfoIcon rez= new InfoIcon(server);
        rez.image=getImage("server.png");
        rez.paintComponent(rez.image.getGraphics());
        return rez;
    }
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}


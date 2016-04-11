package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Canvas extends JPanel {

    private final int GAP = 8;
    
    private int[] points;
    
    private int current = 0;
    private int skip = 0;
    
    private int skipped = 0;
    
    private int max = 4095;
    
    public Canvas() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    public void setMax(int max)
    {
        this.max = max;
    }

    
    
    public void addMeasure(String m)
    {
        if(points == null)
            return;
        
        if(skipped < skip)
        {
            ++skipped;
            return;
        }
        
        skipped = 0;
        
        int conv = Integer.parseInt(m);
        
        conv = this.getHeight() - conv*(this.getHeight()-6)/max - 3;
        
        if(current >= points.length)
            current = 0;

        points[current] = conv;
        
        for(int i = 1; i <= GAP; ++i)
            points[(current+i) % points.length] = -1;
        
        current = (current + 1) % points.length;

        this.repaint();
    }
    
    public void setSkip(int skip)
    {
        this.skip = skip;
        this.skipped = 0;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        if(points == null)
            points = new int[this.getWidth()];
        
        g.setColor(Color.GRAY);
        g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
        
        //g.drawLine(0, (int) (0.707*this.getHeight()), this.getWidth(), (int) (0.707*this.getHeight()));
        
        g.setColor(Color.RED);

        for(int i = 0; i < points.length - 1; ++i)
            if(points[i] >= 0 && points[i+1] >= 0)
                g.drawLine(i, points[i], i+1, points[i+1]);
        
        
    }  
}
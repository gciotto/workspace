package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Canvas extends JPanel {

    private final int GAP = 8;
    
    private int[][] points;
    private Color[] colors = {  Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, 
    							Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK };
    
    private int current[];
    private int skip[];
    private int channel_count;
    private int skipped[];
    
    private int max[], min[];
    
    public Canvas(int channel_c) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.channel_count = channel_c;
        this.max = new int[channel_c];
        this.min = new int[channel_c];
        this.current = new int[channel_c];
        this.skipped = new int[channel_c];
        this.skip = new int[channel_c];
        
        for (int i = 0; i < channel_c; i++) {
        	this.current[i] = 0;
        	this.skip[i] = 0;
        	this.skipped[i] = 0;
        }
    }
    
    public void setMaxOfChannel(int max, int index) {
        this.max[index] = max;
    }

    public void addMeasure(int channel, String m)
    {
        if(points == null)
            return;
        
        if(skipped[channel] < skip[channel] )
        {
            ++skipped[channel];
            return;
        }
        
        skipped[channel] = 0;
        
        int conv = Integer.parseInt(m);
        
        conv = this.getHeight() - conv*(this.getHeight()-6)/this.max[channel] - 3;
        
        points[channel][current[channel]] = conv;
        
        for(int i = 1; i <= GAP; ++i)
            points[channel][(current[channel]+i) % points[channel].length] = -1;
        
        current[channel] = (current[channel] + 1) % points[channel].length;

        this.repaint();
    }
    
    public void setMinOfChannel(int min, int index){
    	this.min[index] = min;
    }
    
    public void setSkip(int skip)
    {
        for (int i = 0; i < this.channel_count; i++) {
        	this.skip[i] = skip;
        	this.skipped[i] = 0;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        if(points == null)
            points = new int[this.channel_count][this.getWidth()];
        
        //g.setColor(Color.GRAY);
        //g.drawLine(0, this.min * this.getHeight() / this.max , this.getWidth(), this.zero_y * this.getHeight() / this.max);
               
        //g.drawLine(0, (int) (0.707*this.getHeight()), this.getWidth(), (int) (0.707*this.getHeight()));
        
        for (int j = 0; j < channel_count; j++) {
        	
	        g.setColor(colors[j]);
	
	        for(int i = 0; i < points[j].length - 1; ++i)
	            if(points[j][i] >= 0 && points[j][i+1] >= 0)
	                g.drawLine(i, points[j][i], i+1, points[j][i+1]);
        }
        
    }  
}
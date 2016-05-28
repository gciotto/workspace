package gui;

import java.util.ArrayList;
import java.util.List;
import main.Board;
import main.Module;

public class Boards extends javax.swing.JPanel {

    private List<Board> boardsList;
    
    private List<Locon>     loconList;
    private List<Locomux>   locomuxList;
    private List<Rux>   	ruxList;
    
    private int         boardsCount;
    
    private int         boardsAdded;
    
    public Boards() 
    {
        initComponents();
        
        boardsCount = 9;
        loconList  = new ArrayList<Locon>();
        locomuxList = new ArrayList<Locomux>();
        ruxList = new ArrayList<Rux>();
        
        boardsAdded = 0;

        loconList.add(locon1);
        loconList.add(locon2);
        loconList.add(locon3);
        
        locomuxList.add(locomux1);
        locomuxList.add(locomux2);
        locomuxList.add(locomux3);
        
        ruxList.add(rux1);
        ruxList.add(rux2);
        ruxList.add(rux3);
        
    }
    
    public void setBoardsList(List<Board> list)
    {
        boardsList = list;
        
        int i = 0;
        
        for(Board b : list)        
            if(b.getModule() != Module.MUX16BBP 
            	&& b.getModule() != Module.RUX12BBP) {
            	
                loconList.get(i).setBoard(b);
                
                Module m = b.getModule();
                
                if (m == Module.LCN16BMP || m == Module.LCN16BBP 
                		|| m == Module.LCN16V32M) {
                
                	loconList.get(i).setCanvasMax(0xFFFF);
                	
                	if (m == Module.LCN16BBP) 
                		loconList.get(i).setCanvasZero(0xFFFF / 2);
                	else loconList.get(i).setCanvasZero(0);
                	
                }
                else {
                	
                	loconList.get(i).setCanvasMax(0xFFF);
                	
                	if (b.getModule() == Module.LCN12BBP)
                    	loconList.get(i).setCanvasZero(0xFFF / 2);
                	else loconList.get(i).setCanvasZero(0);
                	
                	
                }
                
                i++;
            }
        
        while (i < loconList.size())
            loconList.get(i++).setBoard(null);
        
        i = 0;
        for(Board b : list)        
            if(b.getModule() == Module.MUX16BBP) {
                locomuxList.get(i++).setBoard(b);
                
            }
        
        while (i < loconList.size())
            locomuxList.get(i++).setBoard(null);
        
        i = 0;
        for(Board b : list)        
            if(b.getModule() == Module.RUX12BBP) {
                ruxList.get(i).setBoard(b);
                ruxList.get(i).setCanvasMax(0xFFF);
                ruxList.get(i).setCanvasZero(0xFFF / 2);
                i++;
            }
        
        while (i < loconList.size())
            ruxList.get(i++).setBoard(null);        
    }
    
    public void refresh()
    {
        for(Locon l : loconList)
            l.refresh();
        
        for(Locomux l : locomuxList)
            l.refresh();
        
        for(Rux l : ruxList)
            l.refresh();
    }
    
    
  public void clearBoards() {
	  
        for(int i = 0; i < loconList.size(); ++i)
            loconList.get(i).setVisible(false);
        
        for(int i = 0; i < locomuxList.size(); ++i)
            locomuxList.get(i).setVisible(false);
        
        boardsAdded = 0;
    }
    
    
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
  private void initComponents() {

      locon1 = new gui.Locon();
      locon2 = new gui.Locon();
      locon3 = new gui.Locon();
      locomux1 = new gui.Locomux();
      locomux2 = new gui.Locomux();
      locomux3 = new gui.Locomux();
      rux1 = new gui.Rux();
      rux2 = new gui.Rux();
      rux3 = new gui.Rux();

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
              .addContainerGap()
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                      .addComponent(locon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(locon2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(locon3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(layout.createSequentialGroup()
                      .addComponent(locomux1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(locomux2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(locomux3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(layout.createSequentialGroup()
                      .addComponent(rux1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(rux2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(rux3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addContainerGap(24, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
              .addContainerGap()
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(locon3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(locon2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(locon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(locomux2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(locomux3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(locomux1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(rux1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(rux2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(rux3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
  }// </editor-fold>                        
  // Variables declaration - do not modify                     
  private gui.Locomux locomux1;
  private gui.Locomux locomux2;
  private gui.Locomux locomux3;
  private gui.Locon locon1;
  private gui.Locon locon2;
  private gui.Locon locon3;
  private gui.Rux rux1;
  private gui.Rux rux2;
  private gui.Rux rux3;
  // End of variables declaration          
}

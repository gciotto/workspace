package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Board;
import main.Module;

public class BoardsFrame extends JFrame {

	private List<Locon>     loconList;
	private List<Locomux>   locomuxList;
	private List<Rux>   	ruxList;
	private List<Statfnt>   statList;
	private List<Digint>	digintList;
	private List<PCOR4>		pcorList;

	private JPanel pBoards;
	
	private int usedRows, usedColumns;
	
	public BoardsFrame() {

		pBoards = new JPanel();
		pBoards.setLayout(new GridLayout(0, 3, 20, 20));
		
		pBoards.setBorder(javax.swing.BorderFactory.createTitledBorder("Boards List"));
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pBoards, BorderLayout.CENTER);

		this.setSize(100, 200);

		loconList = new ArrayList<Locon>();
		locomuxList = new ArrayList<Locomux>();
		ruxList = new ArrayList<Rux>();
		statList = new ArrayList<Statfnt>();
		digintList = new ArrayList<Digint>();
		pcorList = new ArrayList<PCOR4>();
		
		setVisible(false);
		
		setTitle("Boards");
		
		usedColumns = 0;
		usedRows 	= 0;
		
		setLocation(500, 200);
	}

	public void updateSize() {
		
		/* Updates window height according to the number of boards  */
		this.setSize(1020, this.usedRows * 320);
		this.repaint();
	}
	
	public void addBoard(Board b) {

		if (usedRows == 0)
			usedRows++;
		
		if (usedColumns >= 3) {
			usedColumns = 0;
			usedRows++;
		}
		
		usedColumns++;
		
		this.updateSize();
		
		Module m = b.getModule();
		
		/* Checks module type, in order to add a new board panel in 
		 * frame */
		
		if(m == Module.LCN16BMP || m == Module.LCN16BBP 
				|| m == Module.LCN16V32M || m == Module.LCN12BBP 
				|| m == Module.LCN12BMP) {

			Locon nloconBoard = new Locon();
			nloconBoard.setBoard(b);

			nloconBoard.setDigitalCanvasMax(255);
			nloconBoard.setDigitalCanvasMin(0);
			
			if (m == Module.LCN16BMP || m == Module.LCN16BBP 
					|| m == Module.LCN16V32M) {

				nloconBoard.setAnalogCanvasMax(0xFFFF);
				
				if (m == Module.LCN16BBP) 
					nloconBoard.setAnalogCanvasMin(0xFFFF / 2);
				else nloconBoard.setAnalogCanvasMin(0);
			}
			else {

				nloconBoard.setAnalogCanvasMax(0xFFF);

				if (b.getModule() == Module.LCN12BBP)
					nloconBoard.setAnalogCanvasMin(0xFFF / 2);
				else nloconBoard.setAnalogCanvasMin(0);
			}

			loconList.add(nloconBoard);

			pBoards.add(nloconBoard);
		}

		else if (m == Module.MUX16BBP) {

			Locomux nLocomuxBoard = new Locomux();
			nLocomuxBoard.setBoard(b);

			locomuxList.add(nLocomuxBoard);

			pBoards.add(nLocomuxBoard);
		}   
		else if (m == Module.RUX12BBP) {

			Rux nRuxBoard = new Rux();
			nRuxBoard.setBoard(b);

			nRuxBoard.setBoard(b);
			
			nRuxBoard.setDigitalCanvasMax(255);
			nRuxBoard.setDigitalCanvasMin(0);
			
			nRuxBoard.setAnalogCanvasMax(0xFFF);
			nRuxBoard.setAnalogCanvasMin(0xFFF / 2);

			ruxList.add(nRuxBoard);

			pBoards.add(nRuxBoard);

		}
		else if (m == Module.STATFNT) {
			
			Statfnt nStatFntBoard = new Statfnt();
			nStatFntBoard.setBoard(b);
			
			statList.add(nStatFntBoard);
			
			pBoards.add(nStatFntBoard);
			
		}
		else if (m == Module.DIGINT) {
			
			Digint nDigIntBoard = new Digint();
			nDigIntBoard.setBoard(b);
			
			digintList.add(nDigIntBoard);
			
			pBoards.add(nDigIntBoard);
			
		}
		else if (m == Module.PCOR4) {
			
			PCOR4 pcorBoard = new PCOR4();
			pcorBoard.setBoard(b);
			
			pcorBoard.setCanvasMax(0xFFF);
			pcorBoard.setCanvasMin(0);
			
			pcorList.add(pcorBoard);
			
			pBoards.add(pcorBoard);
			
		}
		
	}

	public void refresh() {

		/* Refreshes each non-empty list */
		
		for(Locon l : loconList)
			l.refresh();

		for(Locomux l : locomuxList)
			l.refresh();

		for(Rux l : ruxList)
			l.refresh();
		
		for(Statfnt l : statList)
			l.refresh();
		
		for(Digint l : digintList)
			l.refresh();
		
		for(PCOR4 l : pcorList)
			l.refresh();
	}

	public void clearBoards() {

		pBoards.removeAll();
		
		this.usedColumns = 0;
		this.usedRows = 0;

		this.locomuxList.clear();
		this.loconList.clear();
		this.ruxList.clear();
		this.statList.clear();
		this.digintList.clear();
		this.pcorList.clear();
	}

}

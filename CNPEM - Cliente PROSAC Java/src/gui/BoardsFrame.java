package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Board;
import main.Module;

/**
 * The BoardsFrame class inherits from JFrame and is responsible for holding all board panels.
 * It is capable of adding or removing panels dynamically.
 * 
 * For the list of supported boards, refer to Client class.
 * 
 * @see IBoard
 * @see Digint
 * @see Locomux
 * @see Locon
 * @see PCOR4
 * @see Rux
 * @see Statfnt
 * @author Gustavo Ciotto Pinton
 */
public class BoardsFrame extends JFrame {

	private List<IBoard> boardsList;
	private JPanel pBoards;
	
	private int usedRows, usedColumns;
	
	
	/**
	 * Constructs a new frame. By default, it uses a 1-row 3-column grid layout, but its size
	 * can change dynamically.
	 * 
	 * @see GridLayout
	 */
	public BoardsFrame() {

		pBoards = new JPanel();
		pBoards.setLayout(new GridLayout(0, 3, 20, 20));
		
		pBoards.setBorder(javax.swing.BorderFactory.createTitledBorder("Boards List"));
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pBoards, BorderLayout.CENTER);

		this.setSize(100, 200);
		
		boardsList = new ArrayList<IBoard>();
		
		setVisible(false);
		
		setTitle("Boards");
		
		usedColumns = 0;
		usedRows 	= 0;
		
		setLocation(500, 200);
	}

	/**
	 * Sets frame's height according to how many boards were detected.
	 */
	public void updateSize() {
		
		/* Updates window height according to the number of boards  */
		this.setSize(1020, this.usedRows * 320);
		this.repaint();
	}
	
	/**
	 * Adds a new board to the interface. It chooses the right panel to be added
	 * according to the boards characteristics.
	 * 
	 * @param b New board object.
	 * @see Board
	 */
	public void addBoard(Board b) {

		if (usedRows == 0)
			usedRows++;
		
		/* each row holds only 3 boards. If more than 3 are used, then add new row */
		if (usedColumns >= 3) {
			usedColumns = 0;
			usedRows++;
		}
		
		usedColumns++;
		
		/* Updates size if needed */
		this.updateSize();
		
		Module m = b.getModule();
		
		/* Checks module type to add a new board panel in 
		 * frame */
		
		if(m == Module.LCN16BMP || m == Module.LCN16BBP 
				|| m == Module.LCN16V32M || m == Module.LCN12BBP 
				|| m == Module.LCN12BMP) {

			Locon nloconBoard = new Locon();
			nloconBoard.setBoard(b);
			
			/* Sets canvas max. and min. values for digital inputs */
			nloconBoard.setDigitalCanvasMax(255);
			nloconBoard.setDigitalCanvasMin(0);
			
			/* We need to check if we are dealing with a 12 or 16-bit locon board */
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

			boardsList.add(nloconBoard);

			pBoards.add(nloconBoard);
		}

		else if (m == Module.MUX16BBP) {

			Locomux nLocomuxBoard = new Locomux();
			nLocomuxBoard.setBoard(b);

			boardsList.add(nLocomuxBoard);
			
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

			boardsList.add(nRuxBoard);

			pBoards.add(nRuxBoard);

		}
		else if (m == Module.STATFNT) {
			
			Statfnt nStatFntBoard = new Statfnt();
			nStatFntBoard.setBoard(b);
			
			boardsList.add(nStatFntBoard);
			
			pBoards.add(nStatFntBoard);
			
		}
		else if (m == Module.DIGINT) {
			
			Digint nDigIntBoard = new Digint();
			nDigIntBoard.setBoard(b);
			
			boardsList.add(nDigIntBoard);
			
			pBoards.add(nDigIntBoard);
			
		}
		else if (m == Module.PCOR4) {
			
			PCOR4 pcorBoard = new PCOR4();
			pcorBoard.setBoard(b);
			
			pcorBoard.setCanvasMax(0xFFF);
			pcorBoard.setCanvasMin(0);
			
			boardsList.add(pcorBoard);
			
			pBoards.add(pcorBoard);
			
		}
		
		else if (m == Module.HP232) {
			
			HP232 hp232 = new HP232();
			
			hp232.setBoard(b);
			
			boardsList.add(hp232);
			pBoards.add(hp232);
		}
		
	}

	/**
	 *  Refreshes board list
	 *  @see IBoard
	 *  @see Board 
	 */
	public void refresh() {

		for (IBoard l: boardsList)
			l.refresh();
	}

	/**
	 * Resets this object and closes the frame.
	 */
	public void clearBoards() {

		this.setVisible(false);
		
		pBoards.removeAll();
		
		this.usedColumns = 0;
		this.usedRows = 0;

		this.boardsList.clear();
	}

}

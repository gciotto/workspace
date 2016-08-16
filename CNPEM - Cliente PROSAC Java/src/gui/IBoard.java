package gui;

import main.Board;

/**
 * Interface definining the methods which all board graphical interfaces must implement.
 * 
 * @author Gustavo CIOTTO PINTON
 */

public interface IBoard {

	/**
	 * Refreshes all fields of the board.
	 */
	public void refresh();
	
	/**
	 * Sets the board which defines the interface.
	 * @param b Board object containing read and write blocks.
	 */
	public void setBoard(Board b);
	
	/**
     * Gets the address of this board.
     * 
     * @return Address of this board
     */
    public String getPosition();
    
    /**
     * Gets the address of this board.
     * @param p New address of this board
     */
    public void setPosition(String p); 
	
}

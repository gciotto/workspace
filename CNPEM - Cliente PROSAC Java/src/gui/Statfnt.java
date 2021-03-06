package gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Board;

/**
 * Graphical representation of a STATFNT board. This class inherits from JPanel, therefore
 * it can be placed inside a window, such as a JFrame. In our application, it will be instantiated
 * by the BoardsFrame class.
 * <br>
 * According to the intracont reference manual, this board presents the following features:<br>
 *  - Read Block  (5 bytes): priority + flags + port A + port B + port C<br>
 *  - Write Block (3 bytes): priority + flags + reset<br>
 * <br> 
 * Refer to <a href="http://git.cnpem.br/bruno.martins/prosac/blob/master/modules/STATFNT.c">git repository </a>
 * for further details about how PROSAC handles them.
 * 
 * @see IBoard
 * @author Gustavo CIOTTO PINTON
 */
public class Statfnt extends javax.swing.JPanel implements IBoard {

    private Board board;
    
    /**
     * Constructs a new panel. It calls initComponents function, which was generated by NetBeans
     * window editor.
     */
    public Statfnt() {
        initComponents();
    }
    
    /**
     * Registers this graphical interface with a Board object and updates its title and 
     * board address.
     * 
     * @param b Board object containing information about a detected board.
     */
    public void setBoard(Board b)
    {
        this.board = b;
        
        if(b == null)
            this.setVisible(false);
        else
        {
            txtName.setText(b.getModule().name);
            txtPosition.setText(String.valueOf(b.getPosition()));
            this.setVisible(true);
        }
        
    }
    
    /**
     * Updates the interface fields according to the values in the Board object.
     * 
     * According to the intracont reference manual:
     *  - Read Block  (5 bytes): priority + flags + port A + port B + port C
     *  - Write Block (3 bytes): priority + flags + reset	
     */
    public void refresh() {
    	
        if(board == null)
            return;
        
        int[] readBytes = board.getReadBytes();
        int[] writeBytes = board.getWriteBytes();
        
        int tPortA = readBytes[2], tPortB = readBytes[3], tPortC = readBytes[4];
        
        lblByteA.setText(Integer.toString(tPortA));
        lblByteB.setText(Integer.toString(tPortB));
        lblByteC.setText(Integer.toString(tPortC));        
                    
        writeBytes[0] = 0;        
        writeBytes[1] = 0x80; 
        
        /* intracont man page specifies that 5th bit must be set in order
         * to reset */
        if (chbReset.getState())
        	writeBytes[2] = 1 << 5;
        else writeBytes[2] = 0;
    }
    
    /**
     * Gets the address of this board.
     * 
     * @return Address of this board
     */
    public String getPosition() {
        return txtPosition.getText();
    }
    
    /**
     * Gets the address of this board.
     * @param p New address of this board
     */
    public void setPosition(String p) {
        txtPosition.setText(p);
    }    
              
    private void initComponents() {

        txtName = new JLabel();
        txtPosition = new JLabel();
        lblportA = new JLabel();
        lblportB = new JLabel();
        lblportC = new JLabel();
        lblByteA = new JLabel();
        lblByteB = new JLabel();
        chbReset = new Checkbox();
        lblByteC = new JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("STATFNT");

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblportA.setText("Porta A [In]:");

        lblportB.setText("Porta B [In]:");

        lblportC.setText("Porta C [Out]:");

        lblByteA.setText("0");

        lblByteB.setText("0");

        chbReset.setLabel("Reset Font");

        lblByteC.setText("0");

        setLayout(new BorderLayout());
        
        JPanel title = new JPanel();
        title.setLayout(new BorderLayout());
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        title.add(txtPosition, BorderLayout.EAST);
        title.add(txtName, BorderLayout.CENTER);
        
        add(title, BorderLayout.NORTH);
        
        JPanel data = new JPanel();
        data.setLayout(new GridLayout(5, 2, 10, 10));
        data.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        data.add(lblportA);
        data.add(lblByteA);
        data.add(lblportB);
        data.add(lblByteB);
        data.add(lblportC);
        data.add(lblByteC);
        
        data.add(chbReset);
        
        add(data, BorderLayout.CENTER);
        
    }                     

    // Variables declaration - do not modify                     
    private Checkbox chbReset;
    private JLabel lblByteA;
    private JLabel lblByteB;
    private JLabel lblByteC;
    private JLabel lblportA;
    private JLabel lblportB;
    private JLabel lblportC;
    private JLabel txtName;
    private JLabel txtPosition;
    // End of variables declaration                        
}

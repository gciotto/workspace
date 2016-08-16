package main;

/**
 * Encapsulates the information related to a board, such as type and address.
 * 
 * @author Gustavo CIOTTO PINTON
 * @author Bruno MARTINS
 * @see Module
 */

public class Board 
{
    private int position;
    
    /* readBytes and writeBytes represent the bytes which will be sent and received 
     * to PROSAC by this board. They are constantly updated. */
    private int readBytes[];
    private int writeBytes[];
    
    private boolean willCycle;
    private boolean willRamp;
    
    private int cycleCurve;
    private int rampCurve;
    private int rampPulses;
    
    private Module module;
    
    /**
     * Constructs a new board with a given address and module type. 
     * @param pos Address
     * @param m Module object.
     * @see Module
     */
    public Board(int pos, Module m)
    {
        this.position = pos;
        this.module = m;
        
        /* +2 comes from PRIORITY and FLAGS */
        this.readBytes  = new int[m.readBytesCount + 2]; 
        this.writeBytes = new int[m.writeBytesCount + 2]; 
        
    }

    /**
     * Gets board address.
     * @return Address
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets board type.
     * @return Board type
     * @see Module
     */
    public Module getModule() {
        return module;
    }

    /**
     * Gets cycle curve id.
     * @return Curve id. 
     */
    public int getCycleCurve() {
        return cycleCurve;
    }

    public void setCycleCurve(int cycleCurve) {
        this.cycleCurve = cycleCurve;
    }

    public int getRampCurve() {
        return rampCurve;
    }

    public void setRampCurve(int rampCurve) {
        this.rampCurve = rampCurve;
    }

    public int[] getReadBytes() {
        return readBytes;
    }

    public void setReadBytes(int[] readBytes) {
        this.readBytes = readBytes;
    }

    public int[] getWriteBytes() {
        return writeBytes;
    }

    public void setWriteBytes(int[] writeBytes) {
        this.writeBytes = writeBytes;
    }

    public boolean getWillCycle() {
        return willCycle;
    }

    public void setWillCycle(boolean willCycle) {
        this.willCycle = willCycle;
    }

    public boolean getWillRamp() {
        return willRamp;
    }

    public void setWillRamp(boolean willRamp) {
        this.willRamp = willRamp;
    }

    public int getRampPulses() {
        return rampPulses;
    }

    public void setRampPulses(int rampPulses) {
        this.rampPulses = rampPulses;
    }

    
    
}

package main;

public class Board 
{
    private int position;
    
    private int readBytes[];
    private int writeBytes[];
    
    private boolean willCycle;
    private boolean willRamp;
    
    private int cycleCurve;
    private int rampCurve;
    private int rampPulses;
    
    
    private Module module;
    
    
    public Board(int pos, Module m)
    {
        this.position = pos;
        this.module = m;
        
        this.readBytes  = new int[m.readBytesCount + 2]; // PRIORITY and FLAGS
        this.writeBytes = new int[m.writeBytesCount + 2]; 
        
        //this.writeBytes[1] = 0x80;
    }

    public int getPosition() {
        return position;
    }

    public Module getModule() {
        return module;
    }

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

package main;

public enum Module 
{
    NONE                (0x3F, 0,  0, "NONE"),
    LCN12BMP            (0x01, 3,  3, "LCN12BMP"),
    LCN12BBP            (0x02, 3,  3, "LCN12BBP"),
    LCN16BMP            (0x03, 3,  3, "LCN16BMP"),
    LCN16BBP            (0x04, 3,  3, "LCN16BBP"),
    MUX16BBP            (0x11, 21, 4, "MUX16BBP");

    public final int    type;   
    public final int    readBytesCount;
    public final int    writeBytesCount;
    public final String name;

    Module(int type, int rBytes, int wBytes, String name) {
        this.type               = type;
        this.readBytesCount     = rBytes;
        this.writeBytesCount    = wBytes;
        this.name               = name;
    }

    public static Module findByType(int type)
    {
        for (Module m : Module.values())
            if(m.type == type)
                return m;
        return null;
    }
}
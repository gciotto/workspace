package main;

/**
 * This enumeration constains all available commands which can be transmitted or read from PROSAC. 
 * @author Bruno MARTINS
 */
public enum Command 
{
    NORMAL              (0x00, "Normal"),
    ADJUST              (0x01, "Ajuste"),
    IDENT               (0x02, "Identificação"),
    END_IDENT           (0x03, "Fim-Identificação"),
    BOOT                (0x04, "Boot"),
    MSG_CONFIRM         (0x05, "Confirmação de Mensagem"),
    RAMP_INIT           (0xC8, "Rampa: inicialização"),
    RAMP_BLOCK          (0xC9, "Rampa: bloco"),
    RAMP_ASK_BLOCK      (0xCA, "Rampa: envie o próximo bloco"),
    RAMP_ENABLE         (0xCB, "Rampa: habilitar"),
    RAMP_ABORT          (0xCC, "Rampa: abortar"),
    RAMP_ABORTED        (0xCD, "Rampa: abortada"),
    RAMP_COMPLETED      (0xCE, "Rampa: completada"),
    ENABLE_READINGS     (0xCF, "Habilitar leituras"),
    RAMP_ENABLE_CYCLIC  (0xD0, "Rampa: habilitar cíclica"),
    CYCLE_ENABLE        (0xE0, "Ciclagem: habilitar"),
    CYCLE_ABORT         (0xE1, "Ciclagem: abortar"),
    CYCLE_ABORTED       (0xE2, "Ciclagem: abortada"),
    CYCLE_COMPLETED     (0xE3, "Ciclagem: completada");

    public final int bytecode;   
    public final String name;

    Command(int bytecode, String name) {
        this.bytecode = bytecode;
        this.name = name;
    }

    /**
     * Finds a enum object by its code. Returns null if it does not exist.
     * @param code Command code
     * @return Command object if it is found. Otherwise, null.
     */
    public static Command findByCode(int code)
    {
        for (Command c : Command.values())
            if(c.bytecode == code)
                return c;
        return null;
    }
}

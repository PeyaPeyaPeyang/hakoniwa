package jp.yamad.hakoniwa.java;

import org.objectweb.asm.Opcodes;

public enum InvocationType
{
    INVOKE_VIRTUAL(Opcodes.INVOKEVIRTUAL),
    INVOKE_SPECIAL(Opcodes.INVOKESPECIAL),
    INVOKE_STATIC(Opcodes.INVOKESTATIC),
    INVOKE_INTERFACE(Opcodes.INVOKEINTERFACE),
    INVOKE_DYNAMIC(Opcodes.INVOKEDYNAMIC)
    ;
    
    private final int opcode;
    
    InvocationType(int opcode) {
        this.opcode = opcode;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public static InvocationType fromOpcode(int opcode) {
        // Invocation opcodes are in the range of 182 to 186 (inclusive) for INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, and INVOKEDYNAMIC.
        // Use switch statement for better performance and clarity.
        switch (opcode) {
            case Opcodes.INVOKEVIRTUAL:
                return INVOKE_VIRTUAL;
            case Opcodes.INVOKESPECIAL:
                return INVOKE_SPECIAL;
            case Opcodes.INVOKESTATIC:
                return INVOKE_STATIC;
            case Opcodes.INVOKEINTERFACE:
                return INVOKE_INTERFACE;
            case Opcodes.INVOKEDYNAMIC:
                return INVOKE_DYNAMIC;
            default:
                throw new IllegalArgumentException("Invalid opcode: " + opcode);
        }
    }
}

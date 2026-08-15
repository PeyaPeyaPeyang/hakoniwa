package jp.yamad.hakoniwa.java;

public enum PrimitiveType implements JavaType {
    BOOLEAN("Z"),
    BYTE("B"),
    CHAR("C"),
    SHORT("S"),
    INT("I"),
    LONG("J"),
    FLOAT("F"),
    DOUBLE("D"),

    VOID("V");

    private final String descriptor;

    PrimitiveType(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public String getTypeDescriptor() {
        return this.descriptor;
    }

    public static PrimitiveType fromDescriptor(String descriptor) {
        for (PrimitiveType type : PrimitiveType.values()) {
            if (type.getTypeDescriptor().equals(descriptor)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown primitive type descriptor: " + descriptor);
    }
}

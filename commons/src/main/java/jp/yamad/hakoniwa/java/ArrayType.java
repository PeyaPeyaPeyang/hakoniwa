package jp.yamad.hakoniwa.java;

import java.util.Objects;

public class ArrayType implements JavaType {
    private final JavaType componentType;
    private final short arrayDimentions;

    public ArrayType(JavaType componentType, short arrayDimentions) {
        this.componentType = componentType;
        this.arrayDimentions = arrayDimentions;
    }

    public ArrayType(JavaType componentType, int arrayDimentions) {
        this(componentType, (short) arrayDimentions);
    }

    public ArrayType(JavaType componentType) {
        this(componentType, (short) 1);
    }

    public JavaType getComponentType() {
        return this.componentType;
    }

    public int getDimensions() {
        return this.arrayDimentions;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public String getTypeDescriptor() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.arrayDimentions; i++) {
            sb.append('[');
        }
        sb.append(this.componentType.getTypeDescriptor());
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ArrayType)) {
            return false;
        }
        ArrayType other = (ArrayType) obj;
        return this.arrayDimentions == other.arrayDimentions
                && Objects.equals(this.componentType, other.componentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.componentType, this.arrayDimentions);
    }
}

package jp.yamad.hakoniwa.java;

public class ArrayType implements JavaType {
    private final JavaType componentType;
    private final short arrayDimentions;

    public ArrayType(JavaType componentType, short arrayDimentions) {
        this.componentType = componentType;
        this.arrayDimentions = arrayDimentions;
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
}

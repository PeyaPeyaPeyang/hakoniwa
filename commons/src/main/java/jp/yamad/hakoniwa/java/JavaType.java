package jp.yamad.hakoniwa.java;

public interface JavaType {
    boolean isPrimitive();
    String getTypeDescriptor();

    /**
     * Parses a Java type descriptor and returns the corresponding JavaType instance.
     *
     * A valid descriptor can be:
     * <ul>
     *   <li>{@code Z} for {@code boolean}</li>
     *   <li>{@code B} for {@code byte}</li>
     *   <li>{@code C} for {@code char}</li>
     *   <li>{@code S} for {@code short}</li>
     *   <li>{@code I} for {@code int}</li>
     *   <li>{@code J} for {@code long}</li>
     *   <li>{@code F} for {@code float}</li>
     *   <li>{@code D} for {@code double}</li>
     *   <li>{@code V} for {@code void}</li>
     *   <li>{@code L<classname>;} for reference types (e.g., {@code Ljava/lang/String;})</li>
     *   <li>{@code [<descriptor>} for array types (e.g., {@code [I} for int[], {@code [[Ljava/lang/String;} for String[][])</li>
     * </ul>
     * @param descriptor
     * @return
     */
    static JavaType parseDescriptor(String descriptor) {
        if (descriptor == null || descriptor.isEmpty()) {
            throw new IllegalArgumentException("Descriptor cannot be null or empty");
        }

        char firstChar = descriptor.charAt(0);
        if (firstChar == '[') {
            // Handle array types
            int arrayDimensions = 0;
            while (descriptor.charAt(arrayDimensions) == '[') {
                arrayDimensions++;
            }
            String elementTypeDescriptor = descriptor.substring(arrayDimensions);
            JavaType elementType = parseDescriptor(elementTypeDescriptor);
            return new ArrayType(elementType, arrayDimensions);
        } else if (firstChar == 'L') {
            // Handle reference types
            if (!descriptor.endsWith(";")) {
                throw new IllegalArgumentException("Invalid reference type descriptor: " + descriptor);
            }
            String className = descriptor.substring(1, descriptor.length() - 1);
            return ReferenceType.of(className);
        } else {
            // Handle primitive types
            return PrimitiveType.fromDescriptor(descriptor);
        }
    }

    static JavaType of(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return PrimitiveType.fromClass(clazz);
        } else if (clazz.isArray()) {
            int dimensions = 0;
            Class<?> componentType = clazz;
            while (componentType.isArray()) {
                dimensions++;
                componentType = componentType.getComponentType();
            }
            JavaType elementType = of(componentType);
            return new ArrayType(elementType, dimensions);
        } else {
            return ReferenceType.of(clazz.getName());
        }
    }
}

package jp.yamad.hakoniwa.java;

import java.util.Arrays;
import java.util.Objects;

public class MethodDescriptor {
    private final String raw;

    private final String methodName;
    private final JavaType[] argsTypes;
    private final JavaType returnType;

    public MethodDescriptor(String raw, String methodName, JavaType[] argsTypes, JavaType returnType) {
        this.raw = raw;
        this.methodName = methodName;
        this.argsTypes = argsTypes;
        this.returnType = returnType;
    }

    public String getRaw() {
        return this.raw;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getName() {
        return this.methodName;
    }

    public JavaType[] getArgsTypes() {
        return this.argsTypes;
    }

    public JavaType getReturnType() {
        return this.returnType;
    }

    public String getDescriptor() {
        return this.methodName + getInvocationDescriptor();
    }

    public String getInvocationDescriptor() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (JavaType argType : this.argsTypes) {
            sb.append(argType.getTypeDescriptor());
        }
        sb.append(')');
        sb.append(this.returnType.getTypeDescriptor());
        return sb.toString();
    }

    /**
     * Parse method descriptor string into MethodDescriptor object.
     * <p>
     * A valid method descriptor string is in the form of:
     * <code>
     *     methodName(Type descriptors of args)returnType
     *     e.g.
     *     hoge(I)V
     *     hoge(Ljava/lang/String;[I)Ljava/lang/String;
     *     main([Ljava/lang/String;)V
     * </code>
     * 
     * @param desc method descriptor string
     * @return MethodDescriptor object
     * @see JavaType#parseDescriptor(String) 
     */
    public static MethodDescriptor parse(String desc) {
        int idxPatternOpen = desc.indexOf('(');
        int idxPatternClose = desc.indexOf(')');
        // )(
        if (idxPatternOpen < 0 || idxPatternClose < 0 || idxPatternOpen >= idxPatternClose) {
            throw new IllegalArgumentException("Invalid method descriptor: " + desc);
        }
        
        String methodName = desc.substring(0, idxPatternOpen);
        String argsDesc = desc.substring(idxPatternOpen + 1, idxPatternClose);
        String returnDesc = desc.substring(idxPatternClose + 1);

        if (methodName.isEmpty()) {
            throw new IllegalArgumentException("Method name cannot be empty in descriptor: " + desc);
        }

        JavaType[] argsTypes = parseArgumentDescriptors(argsDesc);
        JavaType returnType = JavaType.parseDescriptor(returnDesc);

        return new MethodDescriptor(desc, methodName, argsTypes, returnType);
    }

    private static JavaType[] parseArgumentDescriptors(String argsDesc) {
        if (argsDesc.isEmpty()) {
            return new JavaType[0];
        }

        // Split the argument descriptors by type
        // This is a bit tricky because we need to handle array types and reference types
        // We will iterate through the string and extract each type descriptor
        int length = argsDesc.length();
        int index = 0;
        java.util.List<JavaType> argsList = new java.util.ArrayList<>();

        while (index < length) {
            char c = argsDesc.charAt(index);
            if (c == '[') {
                // Array type, find the full descriptor
                int start = index;
                while (argsDesc.charAt(index) == '[') {
                    index++;
                }
                // Now index points to the element type
                JavaType elementType = JavaType.parseDescriptor(argsDesc.substring(start, index + 1));
                argsList.add(elementType);
                index++; // Move past the element type
            } else if (c == 'L') {
                // Reference type, find the full descriptor
                int start = index;
                do {
                    index++;
                }
                while (index < length && argsDesc.charAt(index) != ';');
                
                if (index >= length || argsDesc.charAt(index) != ';') {
                    throw new IllegalArgumentException("Invalid argument descriptor: " + argsDesc);
                }
                JavaType refType = JavaType.parseDescriptor(argsDesc.substring(start, index + 1));
                argsList.add(refType);
                index++; // Move past ';'
            } else {
                // Primitive type
                JavaType primType = JavaType.parseDescriptor(String.valueOf(c));
                argsList.add(primType);
                index++; // Move past the primitive type
            }
        }

        return argsList.toArray(new JavaType[0]);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MethodDescriptor other)) {
            return false;
        }
        return Objects.equals(this.methodName, other.methodName)
                && Arrays.equals(this.argsTypes, other.argsTypes)
                && Objects.equals(this.returnType, other.returnType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.methodName, this.returnType);
        result = 31 * result + Arrays.hashCode(this.argsTypes);
        return result;
    }
}

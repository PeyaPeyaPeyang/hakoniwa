package jp.yamad.hakoniwa.java;

public class ClassMethod {
    private final ReferenceType owner;
    private final MethodDescriptor method;

    public ClassMethod(ReferenceType owner, MethodDescriptor method) {
        this.owner = owner;
        this.method = method;
    }

    public ReferenceType getOwner() {
        return this.owner;
    }

    public MethodDescriptor getMethod() {
        return this.method;
    }
}

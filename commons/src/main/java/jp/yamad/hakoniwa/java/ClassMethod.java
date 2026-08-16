package jp.yamad.hakoniwa.java;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ClassMethod)) {
            return false;
        }
        ClassMethod other = (ClassMethod) obj;
        return Objects.equals(this.owner, other.owner)
                && Objects.equals(this.method, other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.owner, this.method);
    }
}

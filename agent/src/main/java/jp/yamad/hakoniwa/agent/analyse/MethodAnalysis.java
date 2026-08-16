package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.InvocationTrap;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;

public class MethodAnalysis {
    private final MethodDescriptor descriptor;
    private final InvocationTrap<?>[] traps;
    private final ClassMethod[] invokes;

    public MethodAnalysis(MethodDescriptor descriptor, InvocationTrap<?>[] traps, ClassMethod[] invokes) {
        this.descriptor = descriptor;
        this.traps = traps;
        this.invokes = invokes;
    }

    public MethodAnalysis(MethodDescriptor descriptor, InvocationTrap<?>[] traps) {
        this(descriptor, traps, new ClassMethod[0]);
    }

    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    public InvocationTrap<?>[] getTraps() {
        return this.traps;
    }

    public ClassMethod[] getInvokes() {
        return this.invokes;
    }
}

package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.ActionModel;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;

public class MethodAnalysis {
    private final MethodDescriptor descriptor;
    private final ActionModel<?>[] actions;
    private final ClassMethod[] invokes;

    public MethodAnalysis(MethodDescriptor descriptor, ActionModel<?>[] actions, ClassMethod[] invokes) {
        this.descriptor = descriptor;
        this.actions = actions;
        this.invokes = invokes;
    }

    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    public ActionModel<?>[] getActions() {
        return this.actions;
    }

    public ClassMethod[] getInvokes() {
        return this.invokes;
    }
}

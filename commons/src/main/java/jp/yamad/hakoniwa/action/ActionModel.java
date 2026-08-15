package jp.yamad.hakoniwa.action;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;

import java.util.List;

public class ActionModel<A extends Operation> {
    private final A operation;
    private final SecurityTarget target;
    private final InsnList beforeInvocation;
    private final InsnList afterInvocation;

    public ActionModel(A operation, SecurityTarget target, InsnList> beforeInvocation, InsnList afterInvocation) {
        this.operation = operation;
        this.target = target;
        this.beforeInvocation = beforeInvocation;
        this.afterInvocation = afterInvocation;
    }

    public A getOperation() {
        return this.operation;
    }

    public SecurityTarget getTarget() {
        return this.target;
    }

    public InsnList getBeforeInvocation() {
        return this.beforeInvocation;
    }

    public InsnList getAfterInvocation() {
        return this.afterInvocation;
    }
}

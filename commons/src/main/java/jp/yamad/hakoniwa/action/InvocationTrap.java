package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class InvocationTrap<A extends Operation> {
    private final A operation;
    private final SecurityTarget target;
    private final ClassMethod hookedMethod;
    private final ClassMethod checkMethod;

    public InvocationTrap(A operation, SecurityTarget target, ClassMethod hookedMethod, ClassMethod checkMethod) {
        this.operation = operation;
        this.target = target;
        this.hookedMethod = hookedMethod;
        this.checkMethod = checkMethod;
    }

    public A getOperation() {
        return this.operation;
    }

    public SecurityTarget getTarget() {
        return this.target;
    }

    public ClassMethod getHookedMethod() {
        return this.hookedMethod;
    }

    public ClassMethod getCheckMethod() {
        return this.checkMethod;
    }

    public InsnList getBeforeInvocation(int layerId, ClassMethod invokedMethod) {
        InsnList insns = new InsnList();
        insns.add(new LdcInsnNode(layerId));
        insns.add(new LdcInsnNode(invokedMethod.getOwner().getFullName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getInvocationDescriptor()));
        insns.add(new MethodInsnNode(
                INVOKESTATIC,
                this.checkMethod.getOwner().getFullName(),
                this.checkMethod.getMethod().getName(),
                this.checkMethod.getMethod().getInvocationDescriptor(),
                false));
        return insns;
    }

    public InsnList getAfterInvocation(int layerId, ClassMethod invokedMethod) {
        return new InsnList();
    }
}

package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;

public class MethodInvocationTrap extends InvocationTrap<MethodAction.MethodOperation> {
    public MethodInvocationTrap(
            MethodAction.MethodOperation operation,
            SecurityTarget target,
            ClassMethod hookedMethod,
            ClassMethod checkMethod) {
        super(operation, target, hookedMethod, checkMethod);
    }

    public ClassMethod getMethod() {
        return getHookedMethod();
    }
}

package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;

public class MethodActionModel extends ActionModel<MethodAction.MethodOperation> {
    private final ClassMethod method;

    public MethodActionModel(MethodAction.MethodOperation operation, SecurityTarget target, ClassMethod method) {
        super(operation, target);
        this.method = method;
    }

    public ClassMethod getMethod() {
        return this.method;
    }
}

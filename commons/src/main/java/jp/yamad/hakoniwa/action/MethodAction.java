package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;

public abstract class MethodAction extends AbstractAction<MethodOperation> {
    private static final SecurityTarget TARGET = new SecurityTarget(SecurityTarget.ROOT, "method");

    private final ClassMethod performer;
    private final MethodOperation operation;

    public MethodAction(ClassMethod performer, MethodOperation operation) {
        super(SecurityTarget.METHOD, operation);

        this.performer = performer;
        this.operation = operation;
    }

    public ClassMethod getPerformer() {
        return this.performer;
    }

    public MethodOperation getOperation() {
        return this.operation;
    }

    public enum MethodOperation implements Operation {
        INVOKE_STATIC(true),
        INVOKE_INSTANCE(true),
        NEW_INSTANCE(true),

        OTHER(false)
        ,
        ;

        private final boolean critical;

        MethodOperation(boolean critical) {
            this.critical = critical;
        }

        public boolean isCritical() {
            return this.critical;
        }
    }
}

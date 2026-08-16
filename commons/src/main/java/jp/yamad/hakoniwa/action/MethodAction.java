package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;

public abstract class MethodAction extends AbstractAction<MethodAction.MethodOperation> {
    public static final SecurityTarget TARGET = SecurityTarget.METHOD;

    private final ClassMethod performer;
    private final MethodOperation operation;
    private final Object[] arguments;

    public MethodAction(ClassMethod performer, MethodOperation operation) {
        this(performer, operation, new Object[0]);
    }

    public MethodAction(ClassMethod performer, MethodOperation operation, Object[] arguments) {
        super(TARGET, operation);

        this.performer = performer;
        this.operation = operation;
        this.arguments = arguments == null ? new Object[0] : arguments.clone();
    }

    public ClassMethod getPerformer() {
        return this.performer;
    }

    public MethodOperation getOperation() {
        return this.operation;
    }

    public Object[] getArguments() {
        return this.arguments.clone();
    }

    public static class Invocation extends MethodAction {
        public Invocation(ClassMethod performer, MethodOperation operation) {
            super(performer, operation);
        }

        public Invocation(ClassMethod performer, MethodOperation operation, Object... arguments) {
            super(performer, operation, arguments);
        }
    }

    public enum MethodOperation implements Operation {
        INVOKE_STATIC(true),
        INVOKE_INSTANCE(true),
        NEW_INSTANCE(true),

        OTHER(false),
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

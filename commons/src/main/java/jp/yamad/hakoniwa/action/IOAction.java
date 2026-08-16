package jp.yamad.hakoniwa.action;

public abstract class IOAction<A extends Operation> extends AbstractAction<A> {
    public static final SecurityTarget TARGET = new SecurityTarget(SecurityTarget.ROOT, "io");

    protected IOAction(SecurityTarget target, A operation) {
        super(target, operation);
    }
}

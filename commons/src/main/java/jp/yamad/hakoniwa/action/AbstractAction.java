package jp.yamad.hakoniwa.action;

public abstract class AbstractAction<A extends Operation> implements SecurityAction<A> {
    protected final SecurityTarget target;
    protected final A operation;

    public AbstractAction(SecurityTarget target, A operation) {
        this.target = target;
        this.operation = operation;
    }

    @Override
    public SecurityTarget getTarget() {
        return target;
    }

    @Override
    public A getOperation() {
        return operation;
    }
}

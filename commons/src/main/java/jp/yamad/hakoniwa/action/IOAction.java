package jp.yamad.hakoniwa.action;

public abstract class IOAction<A extends Operation> extends AbstractAction<Operation> {
    public static final SecurityTarget TARGET = new SecurityTarget(SecurityTarget.ROOT, "io");
}

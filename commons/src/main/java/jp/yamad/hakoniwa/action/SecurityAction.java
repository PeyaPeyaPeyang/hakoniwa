package jp.yamad.hakoniwa.action;

public interface SecurityAction<A extends Operation> {
    A getOperation();
    SecurityTarget getTarget();
}

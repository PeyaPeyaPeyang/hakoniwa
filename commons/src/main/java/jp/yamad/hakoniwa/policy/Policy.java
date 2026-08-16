package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.Operation;
import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.action.SecurityTarget;

public class Policy {
    private final boolean allowed;
    private final SecurityTarget securityTarget;
    private final Operation operation;

    public Policy(boolean allowed, SecurityTarget securityTarget, Operation operation) {
        this.allowed = allowed;
        this.securityTarget = securityTarget;
        this.operation = operation;
    }

    public static Policy allow(SecurityTarget target, Operation operation) {
        return new Policy(true, target, operation);
    }

    public static Policy deny(SecurityTarget target, Operation operation) {
        return new Policy(false, target, operation);
    }

    public Boolean check(SecurityAction<?> action) {
        if (this.securityTarget != null && !this.securityTarget.equals(action.getTarget())) {
            return null;
        }

        if (this.operation != null && !this.operation.equals(action.getOperation())) {
            return null;
        }

        return this.allowed;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public SecurityTarget getSecurityTarget() {
        return this.securityTarget;
    }

    public Operation getOperation() {
        return this.operation;
    }
}

package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.Operation;
import jp.yamad.hakoniwa.action.SecurityTarget;

import java.util.ArrayList;
import java.util.List;

public class PolicyBuilder {
    private final List<Policy> definitions;

    public PolicyBuilder() {
        this.definitions = new ArrayList<>();
    }

    public void addDefinition(Policy policy) {
        this.definitions.add(policy);
    }

    public PolicyBuilder allow(SecurityTarget target, Operation operation) {
        return add(Policy.allow(target, operation));
    }

    public PolicyBuilder deny(SecurityTarget target, Operation operation) {
        return add(Policy.deny(target, operation));
    }

    public List<Policy> build() {
        return new ArrayList<>(this.definitions);
    }

    private PolicyBuilder add(Policy policy) {
        this.definitions.add(policy);
        return this;
    }
}

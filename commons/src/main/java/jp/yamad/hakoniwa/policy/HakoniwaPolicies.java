package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.ReferenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HakoniwaPolicies {
    private final List<Policy> policies;

    public HakoniwaPolicies(List<Policy> policies) {
        this.policies = new ArrayList<>(policies);
    }

    public HakoniwaPolicies(Policy... policies) {
        this(Arrays.asList(policies));
    }

    public List<Policy> getPolicies() {
        return this.policies;
    }

    public Policy[] getPoliciesFor(ClassMethod executor) {
        return this.policies.stream()
                .filter(policy -> policy.getTarget().matches(executor))
                .toArray(Policy[]::new);
    }

    public Policy[] getPoliciesFor(ReferenceType clazz) {
        return this.policies.stream()
                .filter(policy -> policy.getTarget().matches(clazz))
                .toArray(Policy[]::new);
    }
}

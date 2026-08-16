package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.SecurityAction;
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

    public Policy[] getPoliciesFor(SecurityAction<?> action) {
        return this.policies.stream()
                .filter(policy -> policy.check(action) != null)
                .toArray(Policy[]::new);
    }

    public Policy[] getPoliciesFor(ClassMethod executor) {
        MethodAction action = new MethodAction.Invocation(executor, MethodAction.MethodOperation.INVOKE_INSTANCE);
        return this.policies.stream()
                .filter(policy -> policy.check(action) != null)
                .toArray(Policy[]::new);
    }

    public Policy[] getPoliciesFor(ReferenceType clazz) {
        return new Policy[0];
    }
}

package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.ReferenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class HakoniwaPolicies {
    private final List<Policy> policies;

    public HakoniwaPolicies(List<Policy> policies) {
        this.policies = new ArrayList<>(policies);
    }

    public HakoniwaPolicies(Policy... policies) {
        this(Arrays.asList(policies));
    }

    public HakoniwaPolicies() {
        this.policies = new ArrayList<>();
        this.loadProviders();
    }
    
    private void loadProviders() {
        PolicyBuilder builder = new PolicyBuilder();
        for (HakoniwaPolicyProvider provider : ServiceLoader.load(HakoniwaPolicyProvider.class)) {
            provider.registerPolicies(builder);
        }
        this.policies.addAll(builder.build());
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

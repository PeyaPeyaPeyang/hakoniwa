package jp.yamad.hakoniwa.example;

import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.SecurityTarget;
import jp.yamad.hakoniwa.policy.HakoniwaPolicyProvider;
import jp.yamad.hakoniwa.policy.PolicyBuilder;

public class PrintlnPolicyProvider implements HakoniwaPolicyProvider {
    @Override
    public void registerPolicies(PolicyBuilder builder) {
        builder.denyWhen(
                SecurityTarget.METHOD,
                MethodAction.MethodOperation.INVOKE_INSTANCE,
                action -> {
                    if (!(action instanceof MethodAction)) {
                        return false;
                    }
                    Object[] arguments = ((MethodAction) action).getArguments();
                    return arguments.length > 0
                            && String.valueOf(arguments[0]).contains("hakoniwa-deny");
                });
    }
}

package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.SecurityAction;

@FunctionalInterface
public interface PolicyCondition {
    boolean matches(SecurityAction<?> action);
}

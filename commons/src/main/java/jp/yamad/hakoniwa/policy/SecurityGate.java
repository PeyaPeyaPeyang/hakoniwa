package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.java.ClassMethod;

import java.util.function.BiFunction;

@FunctionalInterface
public interface SecurityGate extends BiFunction<ClassMethod, SecurityAction, Boolean> {
    SecurityGate ALLOW = (classMethod, securityAction) -> true;
    SecurityGate DENY = (classMethod, securityAction) -> false;
    SecurityGate PASS = (classMethod, securityAction) -> null;

    @Override
    Boolean apply(ClassMethod classMethod, SecurityAction securityAction);
}

package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.java.ClassMethod;

import java.util.function.BiFunction;

@FunctionalInterface
public interface SecurityGate extends BiFunction<ClassMethod, SecurityAction, Boolean> {
    static SecurityGate ALLOW = (classMethod, securityAction) -> true;
    static SecurityGate DENY = (classMethod, securityAction) -> false;
    static SecurityGate PASS = (classMethod, securityAction) -> null;

    @Override
    Boolean apply(ClassMethod classMethod, SecurityAction securityAction);
}

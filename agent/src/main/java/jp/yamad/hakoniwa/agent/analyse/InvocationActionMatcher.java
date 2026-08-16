package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.InvocationTrap;
import jp.yamad.hakoniwa.java.ClassMethod;

import java.util.function.BiFunction;

@FunctionalInterface
public interface InvocationActionMatcher extends BiFunction<ClassMethod, ClassMethod, InvocationTrap<?>> {
    @Override
    InvocationTrap<?> apply(ClassMethod instructionOwner, ClassMethod invokedMethod);
}

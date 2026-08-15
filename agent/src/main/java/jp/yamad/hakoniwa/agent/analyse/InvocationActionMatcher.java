package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.ActionModel;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface InvocationActionMatcher extends BiFunction<ClassMethod,  ActionModel<?>> {
    @Override
    ActionModel<?> apply(ClassMethod instructionOwner, MethodDescriptor methodName);
}

package jp.yamad.hakoniwa.example;

import jp.yamad.hakoniwa.action.InvocationTrapProvider;
import jp.yamad.hakoniwa.action.InvocationTrapRegistry;
import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.MethodInvocationTrap;
import jp.yamad.hakoniwa.action.SecurityTarget;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;
import jp.yamad.hakoniwa.java.ReferenceType;

public class PrintlnTrapProvider implements InvocationTrapProvider {
    @Override
    public void registerTraps(InvocationTrapRegistry registry) {
        registry.register(new MethodInvocationTrap(
                MethodAction.MethodOperation.INVOKE_INSTANCE,
                SecurityTarget.METHOD,
                new ClassMethod(
                        ReferenceType.of("java/io/PrintStream"),
                        MethodDescriptor.parse("println(Ljava/lang/String;)V")),
                new ClassMethod(
                        ReferenceType.of("jp/yamad/hakoniwa/agent/HakoniwaAccessManager"),
                        MethodDescriptor.parse("checkMethodInvocation(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V"))));
    }
}

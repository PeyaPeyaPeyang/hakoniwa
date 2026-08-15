package jp.yamad.hakoniwa.agent;

import jp.yamad.hakoniwa.agent.analyse.InvocationActionMatcher;
import jp.yamad.hakoniwa.policy.HakoniwaPolicies;
import jp.yamad.hakoniwa.transform.TransformerRegistry;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Hakoniwa {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("Hakoniwa agent loaded");
        inst.addTransformer(new Transformer(new HakoniwaPolicies(), new TransformerRegistry()));
    }

    private  class Transformer implements ClassFileTransformer {
        private final HakoniwaPolicies policies;
        private final TransformerRegistry registry;

        public Transformer(HakoniwaPolicies policies, TransformerRegistry registry) {
            this.policies = policies;
            this.registry = registry;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassNode clazz = new ClassNode();
            classReader.accept(clazz, 0);

            InvocationActionMatcher matcher = new InvocationActionMatcher(policies);
            matcher.analyseClass(clazz);

            return classfileBuffer;
        }
    }
}

package jp.yamad.hakoniwa.agent;

import jp.yamad.hakoniwa.action.InvocationTrapRegistry;
import jp.yamad.hakoniwa.agent.analyse.MethodAnalyser;
import jp.yamad.hakoniwa.layer.HakoniwaLayer;
import jp.yamad.hakoniwa.layer.LayerRegistry;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Hakoniwa {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }

    private static class Transformer implements ClassFileTransformer {
        private final InvocationTrapRegistry registry = new InvocationTrapRegistry();

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            // Skip transformation for classes in the Hakoniwa package to avoid infinite recursion
            if (className == null || className.startsWith("jp/yamad/hakoniwa/")) {
                return classfileBuffer;
            }

            HakoniwaLayer layer = LayerRegistry.getOrCreateLayer(loader);
            int layerId = layer.getID();

            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassNode clazz = new ClassNode();
            classReader.accept(clazz, 0);

            MethodAnalyser analyser = new MethodAnalyser(layerId, this.registry);
            boolean transformed = analyser.transformClass(clazz);
            if (!transformed) {
                return classfileBuffer;
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            clazz.accept(writer);
            return writer.toByteArray();
        }
    }
}

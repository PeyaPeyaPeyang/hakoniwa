package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.InvocationTrap;
import jp.yamad.hakoniwa.action.InvocationTrapRegistry;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.InvocationType;
import jp.yamad.hakoniwa.java.MethodDescriptor;
import jp.yamad.hakoniwa.java.ReferenceType;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyses the methods of a class and determines their security actions based on method invocations.
 * It uses an InvocationActionMatcher to match method invocations to security actions.
 */
public class MethodAnalyser {
    private final int layerId;
    private final InvocationActionMatcher matcher;

    public MethodAnalyser(int layerId, InvocationActionMatcher matcher) {
        this.layerId = layerId;
        this.matcher = matcher;
    }

    public MethodAnalyser(int layerId, InvocationTrapRegistry registry) {
        this(layerId, (instructionOwner, invokedMethod) -> registry.get(invokedMethod));
    }

    public boolean transformClass(ClassNode node) {
        MethodAnalysis[] analyses = this.analyseClass(node);
        for (MethodAnalysis analysis : analyses) {
            if (analysis.getTraps().length > 0) {
                return true;
            }
        }
        return false;
    }

    public MethodAnalysis[] analyseClass(ClassNode node) {
        ReferenceType clazz = ReferenceType.of(node.name);
        MethodAnalysis[] behaviours = new MethodAnalysis[node.methods.size()];
        for (int i = 0; i < node.methods.size(); i++) {
            MethodNode methodNode = node.methods.get(i);
            behaviours[i] = this.analyse(clazz, methodNode);
        }

        return behaviours;
    }

    public MethodAnalysis analyse(ReferenceType owner, MethodNode node) {
        // The ASM's descriptor does not include the method name,
        // so we need to combine them to create a MethodDescriptor.
        MethodDescriptor desc = MethodDescriptor.parse(node.name + node.desc);
        if (node.instructions == null || node.instructions.size() == 0) {
            return new MethodAnalysis(desc, new InvocationTrap<?>[0]);
        } else {
            InvocationTrap<?>[] traps = this.analyseActions(new ClassMethod(owner, desc), node);
            return new MethodAnalysis(desc, traps);
        }
    }

    private InvocationTrap<?>[] analyseActions(ClassMethod instructionOwner, MethodNode methodNode) {
        InsnList insns = methodNode.instructions;
        List<InvocationTrap<?>> traps = new ArrayList<>();
        for (AbstractInsnNode insn : insns.toArray()) {
            if (insn instanceof MethodInsnNode) {
                InvocationType invocationType = InvocationType.fromOpcode(insn.getOpcode());
                InvocationTrap<?> trap = this.analyseMethodInvocation(
                        invocationType,
                        instructionOwner,
                        (MethodInsnNode) insn,
                        methodNode);
                if (trap != null) {
                    traps.add(trap);
                }
            }
        }

        return traps.toArray(new InvocationTrap<?>[0]);
    }

    private InvocationTrap<?> analyseMethodInvocation(InvocationType type, 
                                                      ClassMethod instructionOwner, MethodInsnNode node, MethodNode methodNode) {
        if (type == InvocationType.INVOKE_SPECIAL && "<init>".equals(node.name)) {
            return null;
        }

        MethodDescriptor desc = MethodDescriptor.parse(node.name + node.desc);
        ClassMethod method = new ClassMethod(ReferenceType.of(node.owner), desc);
        InvocationTrap<?> trap = this.matcher.apply(instructionOwner, method);
        if (trap != null) {
            int localBase = methodNode.maxLocals;
            methodNode.maxLocals = Math.max(
                    methodNode.maxLocals,
                    localBase + trap.getRequiredLocalSlots(method));

            InsnList before = trap.getBeforeInvocation(this.layerId, type, method, localBase);
            if (before != null && before.size() > 0) {
                methodNode.instructions.insertBefore(node, before);
            }
            InsnList after = trap.getAfterInvocation(this.layerId, type, method);
            if (after != null && after.size() > 0) {
                methodNode.instructions.insert(node, after);
            }
        }
        return trap;
    }
}

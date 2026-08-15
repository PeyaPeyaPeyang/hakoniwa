package jp.yamad.hakoniwa.agent.analyse;

import jp.yamad.hakoniwa.action.ActionModel;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;
import jp.yamad.hakoniwa.java.ReferenceType;
import jp.yamad.hakoniwa.policy.HakoniwaPolicies;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyses the methods of a class and determines their security actions based on method invocations.
 * It uses an InvocationActionMatcher to match method invocations to security actions.
 */
public class MethodAnalyser {
    private final HakoniwaPolicies poliices;
    private final InvocationActionMatcher matcher;

    public MethodAnalyser(HakoniwaPolicies policies, InvocationActionMatcher matcher) {
        this.poliices = policies;
        this.matcher = matcher;
    }

    public MethodAnalysis[] analyseClass(ClassNode node) {
        ReferenceType clazz = ReferenceType.parse(node.name);
        MethodAnalysis[] behaviours = new MethodAnalysis[node.methods.size()];
        for (int i = 0; i < node.methods.size(); i++) {
            MethodNode methodNode = node.methods.get(i);
            behaviours[i] = analyse(clazz, methodNode);
        }

        return behaviours;
    }

    public MethodAnalysis analyse(ReferenceType owner, MethodNode node) {
        // The ASM's descriptor does not include the method name,
        // so we need to combine them to create a MethodDescriptor.
        MethodDescriptor desc = MethodDescriptor.parse(node.name + node.desc);
        if (node.instructions == null || node.instructions.size() == 0) {
            return new MethodAnalysis(desc, new ActionModel<?>[0]);
        } else {
            ClassMethod classMethod = new ClassMethod(owner, desc);

            List<InsnNode> insnList = node.instructions.toArray();
            ActionModel<?>[] actions = analyseActions(owner, insnList);
            return new MethodAnalysis(desc, actions);
        }
    }

    private ActionModel<?>[] analyseActions(ReferenceType owner, InsnList insns) {
        ClassMethod instructionOwner = new ClassMethod(owner, desc);
        List<ActionModel<?>> actions = new ArrayList<>();
        for (InsnNode insn : new ArrayList<>(insns)) {
            if (insn instanceof MethodInsnNode) {
                ActionModel<?> action = analyseMethodInvocation(instructionOwner, (MethodInsnNode) insn, insns);
                if (action != null) {
                    actions.add(action);
                }
            }
        }

        return actions.toArray(new ActionModel<?>[0]);
    }

    private ActionModel<?> analyseMethodInvocation(ClassMethod instructionOwner, MethodInsnNode node, InsnList insns) {
        MethodDescriptor desc = MethodDescriptor.parse(node.name + node.desc);
        ClassMethod method = new ClassMethod(ReferenceType.parse(node.owner), desc);
        Policy[] policies = poliices.getPoliciesFor(method);
        if (policies.length == 0) {
            return null;
        }

        ActionModel<?> model = matcher.apply(node.owner, desc);
        if (action != null) {
            if (action.getBeforeInvocation() != null) {
                insns.insertBefore(node, action.getBeforeInvocation());
            }
            if (action.getAfterInvocation() != null) {
                insns.insert(node, action.getAfterInvocation());
            }
        }
    }
}

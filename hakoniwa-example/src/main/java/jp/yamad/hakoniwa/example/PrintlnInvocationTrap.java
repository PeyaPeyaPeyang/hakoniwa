package jp.yamad.hakoniwa.example;

import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.MethodInvocationTrap;
import jp.yamad.hakoniwa.action.SecurityTarget;
import jp.yamad.hakoniwa.java.ClassMethod;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.DUP_X1;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.SWAP;

public class PrintlnInvocationTrap extends MethodInvocationTrap {
    public PrintlnInvocationTrap(
            MethodAction.MethodOperation operation,
            SecurityTarget target,
            ClassMethod hookedMethod,
            ClassMethod checkMethod) {
        super(operation, target, hookedMethod, checkMethod);
    }

    @Override
    public InsnList getBeforeInvocation(int layerId, ClassMethod invokedMethod) {
        InsnList insns = new InsnList();

        insns.add(new InsnNode(DUP));
        insns.add(new InsnNode(ICONST_1));
        insns.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
        insns.add(new InsnNode(DUP_X1));
        insns.add(new InsnNode(SWAP));
        insns.add(new InsnNode(ICONST_0));
        insns.add(new InsnNode(SWAP));
        insns.add(new InsnNode(AASTORE));

        insns.add(new LdcInsnNode(layerId));
        insns.add(new LdcInsnNode(invokedMethod.getOwner().getFullName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getInvocationDescriptor()));
        insns.add(new MethodInsnNode(
                INVOKESTATIC,
                getCheckMethod().getOwner().getFullName(),
                getCheckMethod().getMethod().getName(),
                getCheckMethod().getMethod().getInvocationDescriptor(),
                false));

        return insns;
    }
}

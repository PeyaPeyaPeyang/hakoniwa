package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.InvocationType;
import jp.yamad.hakoniwa.java.JavaType;
import jp.yamad.hakoniwa.java.PrimitiveType;
import jp.yamad.hakoniwa.java.ReferenceType;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP2_X1;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.POP2;
import static org.objectweb.asm.Opcodes.SIPUSH;

public class InvocationTrap<A extends Operation> {
    private final A operation;
    private final SecurityTarget target;
    private final ClassMethod hookedMethod;
    private final ClassMethod checkMethod;

    public InvocationTrap(A operation, SecurityTarget target, ClassMethod hookedMethod, ClassMethod checkMethod) {
        this.operation = operation;
        this.target = target;
        this.hookedMethod = hookedMethod;
        this.checkMethod = checkMethod;
    }

    public A getOperation() {
        return this.operation;
    }

    public SecurityTarget getTarget() {
        return this.target;
    }

    public ClassMethod getHookedMethod() {
        return this.hookedMethod;
    }

    public ClassMethod getCheckMethod() {
        return this.checkMethod;
    }

    public InsnList getBeforeInvocation(int layerId, InvocationType type, ClassMethod invokedMethod) {
        return getBeforeInvocation(layerId, type, invokedMethod, 0);
    }

    public InsnList getBeforeInvocation(int layerId, InvocationType type, ClassMethod invokedMethod, int localBase) {
        InsnList insns = new InsnList();

        JavaType[] argumentTypes = invokedMethod.getMethod().getArgsTypes();

        // Create an array to hold the arguments if there are any arguments.
        if (argumentTypes.length > 0) {
            insns.add(pushInt(argumentTypes.length));
            insns.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
            insns.add(new VarInsnNode(ASTORE, localBase));

            for (int i = argumentTypes.length - 1; i >= 0; i--) {
                boxIfNeeded(insns, argumentTypes[i]);
                insns.add(new VarInsnNode(ALOAD, localBase));
                insns.add(pushInt(i));
                moveArrayIndexBeforeValue(insns);
                insns.add(new InsnNode(AASTORE));
            }
        }

        insns.add(new LdcInsnNode(layerId));
        insns.add(new LdcInsnNode(invokedMethod.getOwner().getFullName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getName()));
        insns.add(new LdcInsnNode(invokedMethod.getMethod().getInvocationDescriptor()));
        insns.add(new LdcInsnNode(type.name()));
        if (argumentTypes.length > 0) {
            insns.add(new VarInsnNode(ALOAD, localBase));
        } else {
            // If there are no arguments, we need to push a null array to the stack for the check method.
            insns.add(pushInt(0));
            insns.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
        }

        insns.add(new MethodInsnNode(
                INVOKESTATIC,
                this.checkMethod.getOwner().getFullName(),
                this.checkMethod.getMethod().getName(),
                this.checkMethod.getMethod().getInvocationDescriptor(),
                false));

        for (int i = 0; i < argumentTypes.length; i++) {
            insns.add(new VarInsnNode(ALOAD, localBase));
            insns.add(pushInt(i));
            insns.add(new InsnNode(AALOAD));
            castOrUnbox(insns, argumentTypes[i]);
        }

        return insns;
    }

    public int getRequiredLocalSlots(ClassMethod invokedMethod) {
        return invokedMethod.getMethod().getArgsTypes().length == 0 ? 0 : 1;
    }

    private static AbstractInsnNode pushInt(int num) {
        if (num >= -1 && num <= 5) {
            // Use ICONST_<n> for small integers
            switch (num) {
                case -1:
                    return new InsnNode(ICONST_M1);
                case 0:
                    return new InsnNode(ICONST_0);
                case 1:
                    return new InsnNode(ICONST_1);
                case 2:
                    return new InsnNode(ICONST_2);
                case 3:
                    return new InsnNode(ICONST_3);
                case 4:
                    return new InsnNode(ICONST_4);
                case 5:
                    return new InsnNode(ICONST_5);
            }
            throw new IllegalArgumentException("Unexpected value: " + num);
        } else if (num >= Byte.MIN_VALUE && num <= Byte.MAX_VALUE) {
            return new IntInsnNode(BIPUSH, num);
        } else if (num >= Short.MIN_VALUE && num <= Short.MAX_VALUE) {
            return new IntInsnNode(SIPUSH, num);
        } else {
            return new LdcInsnNode(num);
        }
    }

    private static void moveArrayIndexBeforeValue(InsnList insns) {
        insns.add(new InsnNode(DUP2_X1));
        insns.add(new InsnNode(POP2));
    }

    private static void boxIfNeeded(InsnList insns, JavaType type) {
        if (!(type instanceof PrimitiveType)) {
            return;
        }

        PrimitiveType primitive = (PrimitiveType) type;
        switch (primitive) {
            case BOOLEAN:
                box(insns, "java/lang/Boolean", "(Z)Ljava/lang/Boolean;");
                break;
            case BYTE:
                box(insns, "java/lang/Byte", "(B)Ljava/lang/Byte;");
                break;
            case CHAR:
                box(insns, "java/lang/Character", "(C)Ljava/lang/Character;");
                break;
            case SHORT:
                box(insns, "java/lang/Short", "(S)Ljava/lang/Short;");
                break;
            case INT:
                box(insns, "java/lang/Integer", "(I)Ljava/lang/Integer;");
                break;
            case LONG:
                box(insns, "java/lang/Long", "(J)Ljava/lang/Long;");
                break;
            case FLOAT:
                box(insns, "java/lang/Float", "(F)Ljava/lang/Float;");
                break;
            case DOUBLE:
                box(insns, "java/lang/Double", "(D)Ljava/lang/Double;");
                break;
            default:
                throw new IllegalArgumentException("Cannot box primitive type: " + primitive);
        }
    }

    private static void box(InsnList insns, String owner, String descriptor) {
        insns.add(new MethodInsnNode(INVOKESTATIC, owner, "valueOf", descriptor, false));
    }

    private static void castOrUnbox(InsnList insns, JavaType type) {
        if (!(type instanceof PrimitiveType)) {
            checkCastIfNeeded(insns, type);
            return;
        }

        PrimitiveType primitive = (PrimitiveType) type;
        switch (primitive) {
            case BOOLEAN:
                unbox(insns, "java/lang/Boolean", "booleanValue", "()Z");
                break;
            case BYTE:
                unbox(insns, "java/lang/Byte", "byteValue", "()B");
                break;
            case CHAR:
                unbox(insns, "java/lang/Character", "charValue", "()C");
                break;
            case SHORT:
                unbox(insns, "java/lang/Short", "shortValue", "()S");
                break;
            case INT:
                unbox(insns, "java/lang/Integer", "intValue", "()I");
                break;
            case LONG:
                unbox(insns, "java/lang/Long", "longValue", "()J");
                break;
            case FLOAT:
                unbox(insns, "java/lang/Float", "floatValue", "()F");
                break;
            case DOUBLE:
                unbox(insns, "java/lang/Double", "doubleValue", "()D");
                break;
            default:
                throw new IllegalArgumentException("Cannot unbox primitive type: " + primitive);
        }
    }

    private static void checkCastIfNeeded(InsnList insns, JavaType type) {
        if (type instanceof ReferenceType) {
            String owner = ((ReferenceType) type).getFullName();
            if (!"java/lang/Object".equals(owner)) {
                insns.add(new TypeInsnNode(CHECKCAST, owner));
            }
            return;
        }
        insns.add(new TypeInsnNode(CHECKCAST, type.getTypeDescriptor()));
    }

    private static void unbox(InsnList insns, String owner, String name, String descriptor) {
        insns.add(new TypeInsnNode(CHECKCAST, owner));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, owner, name, descriptor, false));
    }
    
    public InsnList getAfterInvocation(int layerId, InvocationType type, ClassMethod invokedMethod) {
        return new InsnList();
    }
}

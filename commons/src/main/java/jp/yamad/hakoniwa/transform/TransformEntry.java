package jp.yamad.hakoniwa.transform;

import org.objectweb.asm.tree.InsnList;

public class TransformEntry {
    private final InsnList before;
    private final InsnList after;

    public TransformEntry(InsnList before, InsnList after) {
        this.before = before;
        this.after = after;
    }

    public InsnList getBefore() {
        return this.before;
    }

    public InsnList getAfter() {
        return this.after;
    }
}

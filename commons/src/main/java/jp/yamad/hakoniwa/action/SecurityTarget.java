package jp.yamad.hakoniwa.action;

import java.util.Objects;

public class SecurityTarget {
    public static final SecurityTarget ROOT = new SecurityTarget("root");
    public static final SecurityTarget METHOD = new SecurityTarget(ROOT, "method");

    private final SecurityTarget parent;
    private final String key;

    private SecurityTarget(String key) {
        this.parent = null;
        this.key = key;
    }

    public SecurityTarget(SecurityTarget parent, String key) {
        this.parent = parent;
        this.key = key;
    }

    public SecurityTarget getParent() {
        return this.parent == null ? ROOT : this.parent;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecurityTarget)) {
            return false;
        }
        SecurityTarget other = (SecurityTarget) obj;
        return Objects.equals(this.parent, other.parent)
                && Objects.equals(this.key, other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parent, this.key);
    }
}

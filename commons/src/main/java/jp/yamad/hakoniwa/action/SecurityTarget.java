package jp.yamad.hakoniwa.action;

public class SecurityTarget {
    public static final SecurityTarget ROOT = new SecurityTarget("root");

    private final SecurityTarget parent;
    private final String key;

    private SecurityTarget(String key) {
        this.parent = null;
        this.key = key;
    }

    private SecurityTarget(SecurityTarget parent, String key) {
        this.parent = parent;
        this.key = key;
    }

    public SecurityTarget getParent() {
        return this.parent == null ? ROOT : this.parent;
    }

    public String getKey() {
        return key;
    }
}

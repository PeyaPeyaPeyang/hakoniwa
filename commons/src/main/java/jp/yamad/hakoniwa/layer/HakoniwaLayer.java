package jp.yamad.hakoniwa.layer;

public class HakoniwaLayer {
    public static final ROOT = new HakoniwaLayer(null, "root", LayerAccess.TRUSTED);

    private final HakoniwaLayer parent;
    private final String name;
    private final LayerAccess access;
    private final int depth;

    private HakoniwaLayer(HakoniwaLayer parent, String name, LayerAccess access) {
        if (ROOT != null) {
            throw new IllegalStateException("Duplicated root creation");
        }

        this.parent = parent;
        this.name = name;
        this.access = access;
        this.depth = 0;
    }

    public HakoniwaLayer(HakoniwaLayer parent, String name, LayerAccess access) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent layer cannot be null");
        }
        this.parent = parent;
        this.name = name;
        this.access = access;
        this.depth = parent.depth + 1;
    }

    public boolean hasAccessTo(HakoniwaLayer other) {
        switch (this.access) {
            case TRUSTED:
                return true;
            case UNTRUSTED:
                return this.isHigherThan(other);
            case HORIZONTAL_TRUSTED:
                return this.isLowerOrEqualThan(other);
            case RESTRICTED:
                return false;
        }
    }

    public boolean isHigherThan(HakoniwaLayer other) {
        return this.depth < other.depth;
    }

    public boolean isLowerOrEqualThan(HakoniwaLayer other) {
        return this.depth >= other.depth;
    }
}

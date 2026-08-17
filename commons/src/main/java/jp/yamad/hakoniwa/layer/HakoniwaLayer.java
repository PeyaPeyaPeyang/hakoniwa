package jp.yamad.hakoniwa.layer;

import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.policy.Policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HakoniwaLayer {
    public static final HakoniwaLayer ROOT = new HakoniwaLayer("root");

    private final HakoniwaLayer parent;
    private final String name;
    private final LayerAccess access;
    private final int depth;
    private final List<Policy> policies;
    
    private /* stable */ int id;

    private HakoniwaLayer(String name) {
        this.parent = null;
        this.name = name;
        this.access = LayerAccess.UNTRUSTED;
        this.depth = 0;
        this.policies = new ArrayList<>();
    }

    public HakoniwaLayer(HakoniwaLayer parent, String name, LayerAccess access) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent layer cannot be null");
        }
        this.parent = parent;
        this.name = name;
        this.access = access;
        this.depth = parent.depth + 1;
        this.policies = new ArrayList<>();
    }

    void setID(int id) {
        // Since default id is 0, we can use it to check if the id has already been set
        if (this.id != 0) {
            throw new IllegalStateException("ID has already been set");
        }
        this.id = id;
    }
    
    public int getID() {
        return this.id;
    }
    
    public boolean check(SecurityAction<?> action) {
        for (Policy policy : this.policies) {
            Boolean result = policy.check(action);
            if (result != null) {
                return result;
            }
        }

        return this.access != LayerAccess.RESTRICTED;
    }

    public void addPolicy(Policy policy) {
        this.policies.add(policy);
    }

    public List<Policy> getPolicies() {
        return Collections.unmodifiableList(this.policies);
    }

    public boolean hasAccessTo(HakoniwaLayer other) {
        switch (this.access) {
            case TRUSTED:
                return true;
            case UNTRUSTED:
                return this.isHigherThan(other);
            case HORIZONTAL_TRUSTED:
                return this.isLowerOrEqualThan(other);
            default:
                return false;
        }
    }

    public boolean isHigherThan(HakoniwaLayer other) {
        return this.depth < other.depth;
    }

    public boolean isLowerOrEqualThan(HakoniwaLayer other) {
        return this.depth >= other.depth;
    }

    public HakoniwaLayer getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public LayerAccess getAccess() {
        return this.access;
    }

    public int getDepth() {
        return this.depth;
    }
}

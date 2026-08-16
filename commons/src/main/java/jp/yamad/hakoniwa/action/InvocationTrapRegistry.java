package jp.yamad.hakoniwa.action;

import jp.yamad.hakoniwa.java.ClassMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class InvocationTrapRegistry {
    private final Map<ClassMethod, InvocationTrap<?>> registry;

    public InvocationTrapRegistry() {
        this.registry = new HashMap<>();
        this.loadProviders();
    }

    private void loadProviders() {
        for (InvocationTrapProvider provider : ServiceLoader.load(InvocationTrapProvider.class)) {
            provider.registerTraps(this);
        }
    }

    public InvocationTrap<?> get(ClassMethod hookedMethod) {
        return this.registry.get(hookedMethod);
    }

    public void register(InvocationTrap<?> trap) {
        this.registry.put(trap.getHookedMethod(), trap);
    }
}

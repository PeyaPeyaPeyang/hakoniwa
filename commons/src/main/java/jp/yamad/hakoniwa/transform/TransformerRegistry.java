package jp.yamad.hakoniwa.transform;

import jp.yamad.hakoniwa.action.SecurityAction;

import java.util.HashMap;
import java.util.Map;

public class TransformerRegistry {
    private final Map<Class<? extends SecurityAction<?>>, TransformerEntry> registry;

    public TransformerRegistry() {
        this.registry = new HashMap<>();
        this.registerDefaultTransfoerms();
    }

    private void registerDefaultTransformers() {

    }

    public TransformerEntry getTransformer(Class<? extends SecurityAction<?>> actionClass) {
        return this.registry.get(actionClass);
    }

    public void registerTransformer(Class<? extends SecurityAction<?>> actionClass, TransformerEntry entry) {
        this.registry.put(actionClass, entry);
    }
}

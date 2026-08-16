package jp.yamad.hakoniwa.layer;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class LayerRegistry {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private static final Map<ClassLoader, HakoniwaLayer> LAYER_BY_CLASS_LOADER;
    private static final Map<HakoniwaLayer, Set<WeakReference<ClassLoader>>> CLASS_LOADERS_BY_LAYER;
    private static final Map<Integer, HakoniwaLayer> LAYER_BY_ID;

    static {
        LAYER_BY_CLASS_LOADER = Collections.synchronizedMap(new WeakHashMap<>());
        CLASS_LOADERS_BY_LAYER = Collections.synchronizedMap(new IdentityHashMap<>());
        LAYER_BY_ID = new ConcurrentHashMap<>();
        
        registerLayer(HakoniwaLayer.ROOT);
    }

    private LayerRegistry() {
    }

    public static HakoniwaLayer getOrCreateLayer(ClassLoader loader) {
        if (loader == null) {
            return HakoniwaLayer.ROOT;
        }

        HakoniwaLayer existing = LAYER_BY_CLASS_LOADER.get(loader);
        if (existing != null) {
            return existing;
        }

        HakoniwaLayer created = new HakoniwaLayer(
                HakoniwaLayer.ROOT,
                "classloader-" + NEXT_ID.get(),
                LayerAccess.UNTRUSTED);
        register(created, loader);
        return created;
    }

    public static void register(HakoniwaLayer layer, ClassLoader loader) {
        if (loader == null || layer.getID() != 0) {
            return;
        }

        registerLayer(layer);
        LAYER_BY_CLASS_LOADER.put(loader, layer);
        CLASS_LOADERS_BY_LAYER
                .computeIfAbsent(layer, ignored -> Collections.newSetFromMap(new IdentityHashMap<>()))
                .add(new WeakReference<>(loader));
    }

    public static HakoniwaLayer getLayer(int id) {
        HakoniwaLayer layer = LAYER_BY_ID.get(id);
        if (layer == null) {
            throw new IllegalArgumentException("Unknown Hakoniwa layer id: " + id);
        }
        return layer;
    }

    private static void registerLayer(HakoniwaLayer layer) {
        int id = NEXT_ID.getAndIncrement();
        layer.setID(id);
        LAYER_BY_ID.put(id, layer);
    }
}

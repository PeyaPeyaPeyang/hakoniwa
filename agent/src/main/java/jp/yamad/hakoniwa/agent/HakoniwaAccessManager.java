package jp.yamad.hakoniwa.agent;

import jp.yamad.hakoniwa.action.MethodAction;
import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.exception.SecurityException;
import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.MethodDescriptor;
import jp.yamad.hakoniwa.java.ReferenceType;
import jp.yamad.hakoniwa.layer.HakoniwaLayer;
import jp.yamad.hakoniwa.layer.LayerRegistry;

public class HakoniwaAccessManager {
    public static void check(SecurityAction<?> action) {
        HakoniwaLayer currentLayer = getCurrentLayer();
        check(currentLayer, action);
    }

    public static void checkMethodInvocation(int layerId, String owner, String name, String descriptor) {
        HakoniwaLayer layer = LayerRegistry.getLayer(layerId);
        MethodDescriptor method = MethodDescriptor.parse(name + descriptor);
        ClassMethod classMethod = new ClassMethod(ReferenceType.of(owner), method);
        MethodAction action = new MethodAction.Invocation(classMethod, MethodAction.MethodOperation.INVOKE_INSTANCE);
        check(layer, action);
    }

    public static void check(HakoniwaLayer layer, SecurityAction<?> action) {
        if (!layer.check(action)) {
            throw new SecurityException("Denied " + action.getOperation() + " from layer " + layer.getName());
        }
    }

    public static HakoniwaLayer getCurrentLayer() {
        return HakoniwaLayer.ROOT;
    }

    public static HakoniwaLayer getLayerOf(Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();
        return LayerRegistry.getOrCreateLayer(loader);
    }
}

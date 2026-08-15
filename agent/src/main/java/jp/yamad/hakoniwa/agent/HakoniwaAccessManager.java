package jp.yamad.hakoniwa.agent;

import jp.yamad.hakoniwa.action.SecurityAction;
import jp.yamad.hakoniwa.layer.HakoniwaLayer;

public class HakoniwaAccessManager {
    public static void check(SecurityAction<?> action) {
        HakoniwaLayer currentLayer = getCurrentlayer();

    }

    public static HakoniwaLayer getCurrentlayer() {

    }

    public static HakoniwaLayer getLayerOf(Class<?> clazz) {

    }
}

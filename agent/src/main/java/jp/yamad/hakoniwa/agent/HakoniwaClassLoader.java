package jp.yamad.hakoniwa.agent;

import jp.yamad.hakoniwa.layer.HakoniwaLayer;

import java.net.URL;
import java.net.URLClassLoader;

public class HakoniwaClassLoader extends URLClassLoader {
    private final HakoniwaLayer layer;

    public HakoniwaClassLoader(HakoniwaLayer layer, URL... url) {
        super(url);
        this.layer = layer;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }
}

package com.agonkolgeci.neptune_api.plugin;

public interface PluginController<C extends PluginController<C>> {

    public abstract C load();
    public abstract void unload();

}

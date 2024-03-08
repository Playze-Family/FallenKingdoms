package com.agonkolgeci.neptune_api.common.config.callback.any;

import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;

public interface ConfigurationObjectCallback<O> {


    @NotNull O retrieveObject(@NotNull ConfigSection configuration);

}
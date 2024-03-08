package com.agonkolgeci.neptune_api.common.config.callback.type;

import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;

public interface ConfigurationTypeCallback<K> {

    @NotNull K retrieveType(@NotNull ConfigSection configuration);

}
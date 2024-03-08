package com.agonkolgeci.neptune_api;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;

@Getter
public class FkAPI extends FkPlugin {

    @Getter private static FkAPI instance;
    @Getter private static ProtocolManager protocolManager;

    @Override
    protected void onInstance() throws Exception {
        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

}

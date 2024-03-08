package com.agonkolgeci.neptune_api.common.database;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


@Getter
public class DatabaseCredentials {

    @NotNull private final String host;
    @NotNull private final String username;
    @NotNull private final String password;
    @NotNull private final String name;
    private final int maxPoolSize;
    private final int port;

    public DatabaseCredentials(@NotNull String host, @NotNull String username, @NotNull String password, @NotNull String name, int maxPoolSize, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.name = name;
        this.maxPoolSize = maxPoolSize;
        this.port = port;
    }

    @NotNull
    public String toURI () {
        return "jdbc:mysql://" + host + ":" + port + "/" + name;
    }

    @NotNull
    public HikariConfig toHikariConfig () {
        @NotNull final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(toURI());
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maxPoolSize);

        return hikariConfig;
    }
}
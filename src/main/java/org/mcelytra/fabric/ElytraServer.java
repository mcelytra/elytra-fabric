/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric;

import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.mcelytra.core.Server;
import org.mcelytra.core.ServerPing;
import org.mcelytra.core.entity.EntityPlayer;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class ElytraServer extends Server
{
    private final       MinecraftServer            server;

    public ElytraServer(Logger logger, MinecraftServer server)
    {
        super(logger);
        this.server = server;
    }

    public void launch()
    {
        this.start();
    }

    @Override
    protected void start()
    {
        ElytraRegistry.hello(this);
        super.start();
        this.tcp_port = this.server.getServerPort();
        this.update_server_ping();

        this.addon_manager.enable_addons();
    }

    @Override
    public final String get_brand()
    {
        return "ElytraFabric";
    }

    @Override
    public String get_version()
    {
        return "1.0.0-SNAPSHOT";
    }

    @Override
    public final String get_mc_version()
    {
        return this.server.getVersion();
    }

    @Override
    public String get_mc_bedrock_version()
    {
        return "";
    }

    @Override
    public int get_protocol_version()
    {
        return SharedConstants.getGameVersion().getProtocolVersion();
    }

    @Override
    public int get_bedrock_protocol_version()
    {
        return 0;
    }

    @Override
    public int get_compression_threshold()
    {
        return this.server.getNetworkCompressionThreshold();
    }

    @Override
    public Collection<EntityPlayer> get_online_players()
    {
        return this.server.getPlayerManager().getPlayerList().stream().map(p -> (EntityPlayer) p).collect(Collectors.toList());
    }

    @Override
    public void update_server_ping()
    {
        if (this.server_ping == null) {
            this.server_ping = new ServerPing();
            this.update_server_ping(true);
        } else
            this.update_server_ping(false);
    }

    public void update_server_ping(boolean load)
    {
        if (load) {
            try {
                this.server_ping.load_favicon();
            } catch (IOException e) {
                logger.warn("Error while loading the favicon: " + e.getMessage());
            }
            this.server_ping.set_max_players(this.get_max_players());
            this.server_ping.set_protocol(this.get_protocol_version());
            this.server_ping.set_version_name(this.get_brand() + " " + this.get_mc_version());
            this.server_ping.set_motd(this.config.get_motd());
        }
        this.server_ping.set_online_count(this.players.size());
        this.server_ping.set_players(this.players.parallelStream().map(EntityPlayer::get_profile).collect(Collectors.toList()));
    }
}

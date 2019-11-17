/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.UserCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.core.Elytra;
import org.mcelytra.core.Server;
import org.mcelytra.fabric.ElytraServer;
import org.mcelytra.fabric.utils.ServerGet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.Proxy;
import java.net.URISyntaxException;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin extends MinecraftServer implements ServerGet
{
    private ElytraServer elytra_server;

    public MinecraftDedicatedServerMixin(File file_1, Proxy proxy_1, DataFixer dataFixer_1, CommandManager commandManager_1, YggdrasilAuthenticationService yggdrasilAuthenticationService_1, MinecraftSessionService minecraftSessionService_1, GameProfileRepository gameProfileRepository_1, UserCache userCache_1, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory_1, String string_1)
    {
        super(file_1, proxy_1, dataFixer_1, commandManager_1, yggdrasilAuthenticationService_1, minecraftSessionService_1, gameProfileRepository_1, userCache_1, worldGenerationProgressListenerFactory_1, string_1);
    }

    @Inject(method = "setupServer", at = @At("HEAD"))
    public void on_setup_server(CallbackInfoReturnable<Boolean> ci) throws URISyntaxException
    {
        // Elytra Entrypoint.
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(Elytra.class.getResource("/log4j2.xml").toURI());
        Logger logger = LogManager.getLogger("Elytra");
        this.elytra_server = new ElytraServer(logger, this);
        Elytra.init(this.elytra_server);
        this.elytra_server.launch();
    }

    @Override
    public @NotNull Server get_server()
    {
        return this.elytra_server;
    }
}

/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins;

import net.minecraft.client.network.packet.QueryResponseS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerQueryPacketListener;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.minecraft.server.network.packet.QueryRequestC2SPacket;
import org.mcelytra.core.Elytra;
import org.mcelytra.core.event.network.ServerStatusResponseEvent;
import org.mcelytra.fabric.utils.ConversionUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ServerQueryNetworkHandler.class)
public abstract class ServerQueryNetworkHandlerMixin implements ServerQueryPacketListener
{
    @Shadow
    @Final
    private ClientConnection client;

    /**
     * Handles the QueryRequestC2SPacket.
     *
     * @author LambdAurora
     * @reason Elytra handle of request and events.
     */
    @Inject(method = "onRequest", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/Packet;)V"), cancellable = true)
    private void on_request(QueryRequestC2SPacket packet, CallbackInfo ci)
    {
        ServerStatusResponseEvent event = new ServerStatusResponseEvent((InetSocketAddress) this.client.getAddress(), Elytra.get_handle().get_server_ping());
        Elytra.get_addon_manager().fire_event(event);
        if (!event.is_cancelled()) {
            this.client.send(new QueryResponseS2CPacket(ConversionUtils.to_server_metadata(event.get_server_ping())));
        }
        ci.cancel();
    }
}

/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"), cancellable = true)
    public void on_chat_message(ChatMessageC2SPacket packet, CallbackInfo ci)
    {
        // @TODO Chat Event
    }

    @Inject(method = "onPlayerMove", at = @At("HEAD"))
    public void on_player_move(PlayerMoveC2SPacket packet, CallbackInfo ci)
    {
    }
}

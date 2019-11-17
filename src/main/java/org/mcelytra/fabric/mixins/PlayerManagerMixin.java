/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.UserCache;
import net.minecraft.world.dimension.DimensionType;
import org.mcelytra.core.entity.EntityPlayer;
import org.mcelytra.core.event.player.PlayerJoinEvent;
import org.mcelytra.fabric.utils.ConversionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
    private ThreadLocal<EntityPlayer> player  = new ThreadLocal<>();
    private ThreadLocal<Boolean>      renamed = new ThreadLocal<>();

    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/UserCache;add(Lcom/mojang/authlib/GameProfile;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void on_player_connect(ClientConnection client, ServerPlayerEntity player_entity, CallbackInfo ci, GameProfile game_profile, UserCache user_cache, String user_name)
    {
        player.set((EntityPlayer) player_entity);
        renamed.set(!player_entity.getGameProfile().getName().equalsIgnoreCase(user_name));
    }

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/text/Text;)V"))
    private void on_player_connect_notice(PlayerManager player_manager, Text text)
    {
        EntityPlayer player = this.player.get();
        PlayerJoinEvent event = new PlayerJoinEvent(player, ConversionUtils.to_elytra_text(text), renamed.get());
        player.get_server().get_addon_manager().fire_event(event);
        player_manager.sendToAll(ConversionUtils.to_minecraft_text(event.get_join_message()));
        player.get_server().update_server_ping();
        this.player.remove();
        renamed.remove();
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void on_player_leave(ServerPlayerEntity player_entity, CallbackInfo ci)
    {
        ((EntityPlayer) player_entity).get_server().update_server_ping();
    }

    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void on_player_respawn(ServerPlayerEntity player_entity, DimensionType dimension_type, boolean b, CallbackInfoReturnable<ServerPlayerEntity> ci)
    {
        ((EntityPlayer) player_entity).get_server().update_server_ping();
    }
}

/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.SharedConstants;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.mcelytra.core.GameMode;
import org.mcelytra.core.MinecraftEdition;
import org.mcelytra.core.entity.EntityPlayer;
import org.mcelytra.fabric.utils.ConversionUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntityMixin implements EntityPlayer
{
    @Shadow
    private String clientLanguage;

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;

    @Shadow
    public int pingMilliseconds;

    @Shadow
    public abstract void sendChatMessage(Text text_1, MessageType messageType_1);

    @Shadow
    public abstract String getServerBrand();

    @Shadow private net.minecraft.client.options.ChatVisibility clientChatVisibility;

    @Override
    public int get_protocol_version()
    {
        // @TODO Do more injection to get the true client protocol version in case of a ViaVersion like plugin.
        return SharedConstants.getGameVersion().getProtocolVersion();
    }

    @Override
    public MinecraftEdition get_mc_edition()
    {
        return MinecraftEdition.JAVA;
    }

    @Override
    public @NotNull String get_brand()
    {
        return this.getServerBrand();
    }

    @Override
    public InetSocketAddress get_address()
    {
        return (InetSocketAddress) this.networkHandler.client.getAddress();
    }

    @Override
    public int get_ping()
    {
        return this.pingMilliseconds;
    }

    @Override
    public String get_locale()
    {
        return this.clientLanguage;
    }

    @Override
    public ChatVisibility get_chat_visibility()
    {
        return ConversionUtils.to_elytra_chat_visibility(this.clientChatVisibility);
    }

    @Override
    public void send_message(ChatMessageType type, BaseComponent... message)
    {
        this.sendChatMessage(ConversionUtils.to_minecraft_text(message), ConversionUtils.to_minecraft_chat_message_type(type));
    }

    @Override
    public void disconnect(BaseComponent... reason)
    {
        this.networkHandler.disconnect(ConversionUtils.to_minecraft_text(reason));
    }

    @Override
    public @NotNull GameMode get_game_mode()
    {
        return ConversionUtils.to_elytra_gamemode(this.interactionManager.getGameMode());
    }

    @Override
    public void set_game_mode(@NotNull GameMode game_mode)
    {
        this.interactionManager.setGameMode(ConversionUtils.to_minecraft_game_mode(game_mode));
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void on_player_attack_entity(net.minecraft.entity.Entity mc_target, CallbackInfo info) {
        // @TODO attack entity event?
    }
}

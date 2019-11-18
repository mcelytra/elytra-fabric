/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.utils;

import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.MessageType;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.mcelytra.core.GameMode;
import org.mcelytra.core.Hand;
import org.mcelytra.core.Rarity;
import org.mcelytra.core.ServerPing;

public class ConversionUtils
{
    public static Text to_minecraft_text(BaseComponent... components)
    {
        if (components == null || components.length == 0)
            return null;
        return Text.Serializer.fromJson(ComponentSerializer.toString(components));
    }

    public static BaseComponent[] to_elytra_text(Text text)
    {
        if (text == null)
            return null;
        return ComponentSerializer.parse(Text.Serializer.toJson(text));
    }

    public static MessageType to_minecraft_chat_message_type(ChatMessageType type)
    {
        switch (type) {
            case CHAT:
                return MessageType.CHAT;
            case ACTION_BAR:
                return MessageType.GAME_INFO;
            default:
                return MessageType.SYSTEM;
        }
    }

    public static ChatMessageType to_elytra_chat_message_type(MessageType type)
    {
        switch (type) {
            case CHAT:
                return ChatMessageType.CHAT;
            case GAME_INFO:
                return ChatMessageType.ACTION_BAR;
            default:
                return ChatMessageType.SYSTEM;
        }
    }

    public static ChatVisibility to_elytra_chat_visibility(net.minecraft.client.options.ChatVisibility visibility)
    {
        switch (visibility) {
            case SYSTEM:
                return ChatVisibility.SYSTEM;
            case HIDDEN:
                return ChatVisibility.HIDDEN;
            default:
                return ChatVisibility.FULL;
        }
    }

    public static net.minecraft.client.options.ChatVisibility to_minecraft_chat_visibility(ChatVisibility visibility)
    {
        switch (visibility) {
            case SYSTEM:
                return net.minecraft.client.options.ChatVisibility.SYSTEM;
            case HIDDEN:
                return net.minecraft.client.options.ChatVisibility.HIDDEN;
            default:
                return net.minecraft.client.options.ChatVisibility.FULL;
        }
    }

    public static net.minecraft.world.GameMode to_minecraft_game_mode(GameMode game_mode)
    {
        return net.minecraft.world.GameMode.byId(game_mode.get_id());
    }

    public static GameMode to_elytra_gamemode(net.minecraft.world.GameMode game_mode)
    {
        return GameMode.from_id(game_mode.getId());
    }

    public static net.minecraft.util.Rarity to_minecraft_rarity(Rarity rarity)
    {
        switch (rarity) {
            case UNCOMMON:
                return net.minecraft.util.Rarity.UNCOMMON;
            case RARE:
                return net.minecraft.util.Rarity.RARE;
            case EPIC:
                return net.minecraft.util.Rarity.EPIC;
            default:
                return net.minecraft.util.Rarity.COMMON;
        }
    }

    public static Rarity to_elytra_rarity(net.minecraft.util.Rarity rarity)
    {
        switch (rarity) {
            case UNCOMMON:
                return Rarity.UNCOMMON;
            case RARE:
                return Rarity.RARE;
            case EPIC:
                return Rarity.EPIC;
            default:
                return Rarity.COMMON;
        }
    }

    public static net.minecraft.util.Hand to_minecraft_hand(Hand hand)
    {
        return hand == Hand.OFF_HAND ? net.minecraft.util.Hand.OFF_HAND : net.minecraft.util.Hand.MAIN_HAND;
    }

    public static Hand to_elytra_hand(net.minecraft.util.Hand hand)
    {
        return hand == net.minecraft.util.Hand.OFF_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }

    /**
     * Converts Elytra's ServerPing to Minecraft's ServerMetadata version.
     *
     * @param ping Elytra ServerPing object.
     * @return Minecraft ServerMetadata version object.
     */
    public static ServerMetadata.Version to_server_metadata_version(ServerPing ping)
    {
        return new ServerMetadata.Version(ping.get_version_name(), ping.get_protocol());
    }

    /**
     * Converts Elytra's ServerPing player data to Minecraft's ServerMetadata.Players object.
     *
     * @param ping Elytra ServerPing object to convert.
     * @return The conversion result.
     */
    public static ServerMetadata.Players to_server_metadata_players(ServerPing ping)
    {
        ServerMetadata.Players players = new ServerMetadata.Players(ping.get_max_players(), ping.get_online_count());
        players.setSample(ping.get_players().toArray(new GameProfile[0]));
        return players;
    }

    /**
     * Converts Elytra's ServerPing to Minecraft's ServerMetadata.
     * The two objects represents the same things.
     *
     * @param ping Elytra ServerPing object to convert.
     * @return The conversion result.
     */
    public static ServerMetadata to_server_metadata(ServerPing ping)
    {
        ServerMetadata metadata = new ServerMetadata();
        metadata.setFavicon(ping.get_favicon());
        metadata.setVersion(to_server_metadata_version(ping));
        metadata.setPlayers(to_server_metadata_players(ping));
        metadata.setDescription(to_minecraft_text(ping.get_motd()));
        return metadata;
    }
}

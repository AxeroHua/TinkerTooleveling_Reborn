package org.embeddedt.tinkerleveling;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ClientHelper {

    public static void sendLevelUpMessage(int level, Component toolName) {
        Component textComponent;
        // special message
        if(I18n.exists("message.levelup." + level)) {
            textComponent = Component.translatable("message.levelup." + level, toolName).withStyle(style -> style.withColor(ChatFormatting.DARK_AQUA));
        }
        // generic message
        else {
            textComponent = Component.translatable("message.levelup.generic", toolName).append(ClientEvents.getLevelString(level));
        }
        Minecraft.getInstance().player.sendSystemMessage(textComponent);
    }

}

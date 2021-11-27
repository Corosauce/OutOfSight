package com.corosus.out_of_sight.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

//public class net.minecraft.commands.Commands static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
public class CommandReloadConfig {
	public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal(getCommandName())
			.then(Commands.literal("client").executes(c -> {
				/** dummy literal for autocomplete sake, see EventHandlerForge.clientChat for what actually "intercepts" this */
				return Command.SINGLE_SUCCESS;
			})).then(Commands.literal("common").executes(c -> {
				ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FMLPaths.CONFIGDIR.get());
				System.out.println("reloading all mods client configurations");
				return Command.SINGLE_SUCCESS;
			}))
		);
	}

	public static String getCommandName() {
		return "reloadconfig";
	}
}

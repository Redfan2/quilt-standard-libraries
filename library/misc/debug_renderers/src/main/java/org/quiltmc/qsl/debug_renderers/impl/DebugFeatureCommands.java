/*
 * Copyright 2024 The Quilt Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.debug_renderers.impl;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;

import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;

@ApiStatus.Internal
final class DebugFeatureCommands {
	static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(
				literal("quilt_debug").then(
						argument("feature", IdentifierArgumentType.identifier())
								.suggests((c, b) -> CommandSource.suggestIdentifiers(DebugFeaturesImpl.getFeatures().stream().filter(DebugFeature::needsServer).map(DebugFeature::id), b)).then(
										literal("enable").executes(setEnabled(true))
								).then(
										literal("disable").executes(setEnabled(false))
								)
				)
		));
	}

	private static final DynamicCommandExceptionType INVALID_FEATURE = new DynamicCommandExceptionType(id -> Text.literal("No such Debug Feature "+id+"!"));

	private static final Dynamic2CommandExceptionType NOT_SERVER_FEATURE = new Dynamic2CommandExceptionType((id, enable) -> {
		var suggestedCommand = "/quilt_debug_client " + id + " " + (enable == Boolean.TRUE ? "enable" : "disable");
		return Text.empty()
				.append(Text.literal("Debug Feature " + id + " is not server-side! Did you mean to use ["))
				.append(Text.literal(suggestedCommand).styled(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suggestedCommand))))
				.append("]");
	});

	private static Command<ServerCommandSource> setEnabled(boolean value) {
		return ctx -> {
			var id = IdentifierArgumentType.getIdentifier(ctx, "feature");
			var feature = DebugFeaturesImpl.get(id);
			if (feature == null) {
				throw INVALID_FEATURE.create(id);
			}

			if (!feature.needsServer()) {
				throw NOT_SERVER_FEATURE.create(id, value);
			}

			DebugFeaturesImpl.setEnabledNotifyClients(feature, value, ctx.getSource().getServer());
			ctx.getSource().sendFeedback(new Supplier<Text>() {
				@Override
				public Text get() {
					return Text.empty()
						.append(Text.literal("[Debug|Server]: ").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
						.append(Text.literal(id+" "+(value ? "enabled" : "disabled")));
				}
				},
				true
			);
			return Command.SINGLE_SUCCESS;
		};
	}
}
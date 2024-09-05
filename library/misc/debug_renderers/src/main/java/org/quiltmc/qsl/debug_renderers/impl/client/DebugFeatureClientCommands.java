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

package org.quiltmc.qsl.debug_renderers.impl.client;

import static org.quiltmc.qsl.command.api.client.ClientCommandManager.argument;
import static org.quiltmc.qsl.command.api.client.ClientCommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.command.api.client.ClientCommandRegistrationCallback;
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.debug_renderers.impl.DebugFeaturesImpl;

@ApiStatus.Internal
@ClientOnly
final class DebugFeatureClientCommands {
	static void init() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(
				literal("quilt_debug_client").then(
						argument("feature", IdentifierArgumentType.identifier())
								.suggests((c, b) -> CommandSource.suggestIdentifiers(DebugFeaturesImpl.getFeatures().stream().map(DebugFeature::id), b)).then(
										literal("enable").executes(setEnabled(true))
								).then(
										literal("disable").executes(setEnabled(false))
								)
				)
		));
	}

	private static final DynamicCommandExceptionType INVALID_FEATURE = new DynamicCommandExceptionType(id -> Text.literal("No such Debug Feature "+id+"!"));

	private static Command<QuiltClientCommandSource> setEnabled(boolean value) {
		return ctx -> {
			var id = ctx.getArgument("feature", Identifier.class);
			var feature = DebugFeaturesImpl.get(id);
			if (feature == null) {
				throw INVALID_FEATURE.create(id);
			}

			if (feature.needsServer() && !DebugFeaturesImpl.isEnabledOnServer(feature)) {
				var suggestedCommand = "/quilt_debug " + id + " enable";
				ctx.getSource().sendFeedback(
						Text.empty()
								.append(Text.literal("[Debug|Client]: ").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
								.append(Text.literal("Debug Feature " + id + " must be enabled on the server, but it is not - enable it with [")).formatted(Formatting.YELLOW)
								.append(Text.literal(suggestedCommand).styled(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suggestedCommand))))
								.append(Text.literal("]")).formatted(Formatting.YELLOW)
				);
			}

			DebugFeaturesImpl.setEnabledNotifyServer(feature, value);

			ctx.getSource().sendFeedback(
					Text.empty()
							.append(Text.literal("[Debug|Client]: ").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
							.append(Text.literal(id+" "+(value ? "enabled" : "disabled")))
			);
			return Command.SINGLE_SUCCESS;
		};
	}
}

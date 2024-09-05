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

package org.quiltmc.qsl.debug_renderers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

import org.quiltmc.qsl.debug_renderers.api.client.DebugRendererRegistrationCallback;
import org.quiltmc.qsl.debug_renderers.impl.client.DebugRendererRegistry;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "<init>", at = @At(value = "INVOKE", shift = At.Shift.BY, by = 2, target = "Lnet/minecraft/client/render/debug/DebugRenderer;<init>(Lnet/minecraft/client/MinecraftClient;)V"))
	private void quilt$initDebugRenderers(RunArgs args, CallbackInfo ci) {
		DebugRendererRegistrationCallback.EVENT.invoker().registerDebugRenderers(DebugRendererRegistry::register);
	}
}

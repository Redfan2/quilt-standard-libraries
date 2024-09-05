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

import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Collection;

import net.minecraft.client.render.debug.DebugRenderer;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.debug_renderers.impl.DebugFeaturesImpl;

@ApiStatus.Internal
@ClientOnly
public final class DebugRendererRegistry {
	private static final Map<DebugFeature, DebugRenderer.Renderer> RENDERERS = new HashMap<>();

	DebugRendererRegistry() {}

	public static void register(DebugFeature feature, DebugRenderer.Renderer renderer) {
		RENDERERS.put(feature, renderer);
	}

	public static Collection<DebugRenderer.Renderer> getEnabledRenderers() {
		return DebugFeaturesImpl.getEnabledFeatures().stream()
				.filter(DebugFeature::shouldRender)
				.map(RENDERERS::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}

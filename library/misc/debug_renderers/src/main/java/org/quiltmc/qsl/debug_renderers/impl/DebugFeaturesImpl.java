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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.HashMap;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.networking.api.PlayerLookup;

@ApiStatus.Internal
public class DebugFeaturesImpl {
	private static final Map<Identifier, DebugFeature> DEBUG_FEATURES = new HashMap<>();
	private static final Set<DebugFeature> ENABLED_FEATURES = new HashSet<>();
	private static final WeakHashMap<ServerPlayerEntity, Set<Identifier>> ENABLED_FEATURES_PER_PLAYER = new WeakHashMap<>();
	@ClientOnly
	private static final Set<Identifier> ENABLED_FEATURES_ON_SERVER = new HashSet<>();

	public static DebugFeature register(DebugFeature feature) {
		DEBUG_FEATURES.put(feature.id(), feature);
		return feature;
	}

	public static @Nullable DebugFeature get(Identifier id) {
		return DEBUG_FEATURES.get(id);
	}

	public static Set<DebugFeature> getFeatures() {
		return new HashSet<>(DEBUG_FEATURES.values());
	}

	public static boolean isEnabled(DebugFeature feature) {
		return ENABLED_FEATURES.contains(feature);
	}

	public static void setEnabled(DebugFeature feature, boolean value) {
		if (value) {
			ENABLED_FEATURES.add(feature);
		} else {
			ENABLED_FEATURES.remove(feature);
		}
	}

	public static void setEnabledNotifyClients(DebugFeature feature, boolean value, MinecraftServer server) {
		setEnabled(feature, value);
		DebugFeatureSync.syncFeaturesToClient(PlayerLookup.all(server), feature);
	}

	@ClientOnly
	public static void setEnabledNotifyServer(DebugFeature feature, boolean value) {
		setEnabled(feature, value);
		DebugFeatureSync.syncFeaturesToServer(feature);
	}

	public static Set<DebugFeature> getEnabledFeatures() {
		return new HashSet<>(ENABLED_FEATURES);
	}

	public static boolean isEnabledForPlayer(ServerPlayerEntity player, DebugFeature feature) {
		return ENABLED_FEATURES_PER_PLAYER.getOrDefault(player, Set.of()).contains(feature.id());
	}

	public static void setEnabledForPlayer(ServerPlayerEntity player, DebugFeature feature, boolean value) {
		var set = ENABLED_FEATURES_PER_PLAYER.getOrDefault(player, new HashSet<>());
		if (value) {
			set.add(feature.id());
		} else {
			set.remove(feature.id());
		}

		ENABLED_FEATURES_PER_PLAYER.put(player, set);
	}

	public static void setEnabledForPlayer(ServerPlayerEntity player, Map<DebugFeature, Boolean> statuses) {
		var set = ENABLED_FEATURES_PER_PLAYER.getOrDefault(player, new HashSet<>());
		for (var entry : statuses.entrySet()) {
			if (entry.getValue()) {
				set.add(entry.getKey().id());
			} else {
				set.remove(entry.getKey().id());
			}
		}

		ENABLED_FEATURES_PER_PLAYER.put(player, set);
	}

	@ClientOnly
	public static boolean isEnabledOnServer(DebugFeature feature) {
		return ENABLED_FEATURES_ON_SERVER.contains(feature.id());
	}

	@ClientOnly
	public static void setEnabledOnServer(DebugFeature feature, boolean value) {
		if (value) {
			ENABLED_FEATURES_ON_SERVER.add(feature.id());
		} else {
			ENABLED_FEATURES_ON_SERVER.remove(feature.id());
		}
	}

	@ClientOnly
	public static void setEnabledOnServer(Map<DebugFeature, Boolean> statuses) {
		for (var entry : statuses.entrySet()) {
			if (entry.getValue()) {
				ENABLED_FEATURES_ON_SERVER.add(entry.getKey().id());
			} else {
				ENABLED_FEATURES_ON_SERVER.remove(entry.getKey().id());
			}
		}
	}
}

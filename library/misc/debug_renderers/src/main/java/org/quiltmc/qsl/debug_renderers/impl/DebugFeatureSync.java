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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PayloadTypeRegistry;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.quiltmc.qsl.networking.api.server.ServerPlayNetworking;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public final class DebugFeatureSync {
	@ClientOnly
	public static void syncFeaturesToServer() {
		var features = DebugFeaturesImpl.getFeatures();
		ClientPlayNetworking.send(new DebugFeatureSyncPacket(convertStatuses(features)));
	}

	@ClientOnly
	public static void syncFeaturesToServer(DebugFeature... features) {
		ClientPlayNetworking.send(new DebugFeatureSyncPacket(convertStatuses(List.of(features))));
	}

	public static void syncFeaturesToClient(ServerPlayerEntity... players) {
		var features = DebugFeaturesImpl.getFeatures();
		ServerPlayNetworking.send(List.of(players), new DebugFeatureSyncPacket(convertStatuses(features)));
	}

	public static void syncFeaturesToClient(Collection<ServerPlayerEntity> players, DebugFeature... features) {
		ServerPlayNetworking.send(players, new DebugFeatureSyncPacket(convertStatuses(List.of(features))));
	}

	public static PacketByteBuf writeStatuses(Collection<DebugFeature> features) {
		var buf = PacketByteBufs.create();
		buf.writeVarInt(features.size());
		for (var feature : features) {
			buf.writeIdentifier(feature.id());
			buf.writeBoolean(DebugFeaturesImpl.isEnabled(feature));
		}

		return buf;
	}

	public static Map<DebugFeature, Boolean> convertStatuses(Collection<DebugFeature> features) {
		Map<DebugFeature, Boolean> map = new HashMap<>();
		for (DebugFeature feature : features) {
			map.put(feature, DebugFeaturesImpl.isEnabled(feature));
		}

		return map;
	}

	/** Takes a PacketByteBuf, validates that it contains {@code DebugFeature}s and their corresponding statuses and returns these.
	 * @author QuiltMC, WillBl
	 * */
	public static Map<DebugFeature, Boolean> readStatuses(PacketByteBuf buf) {
		final int size = buf.readVarInt();
		var statuses = new HashMap<DebugFeature, Boolean>();
		for (int i = 0; i < size; i++) {
			var featureId = buf.readIdentifier();
			var feature = DebugFeaturesImpl.get(featureId);
			if (feature == null) {
				Initializer.LOGGER.warn("Received value for unknown debug feature {}", featureId);
				continue;
			}

			boolean enabled = buf.readBoolean();
			statuses.put(feature, enabled);
		}

		return statuses;
	}

	public static void init() {
		PayloadTypeRegistry.playS2C().register(DebugFeatureSyncPacket.ID, DebugFeatureSyncPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(DebugFeatureSyncPacket.ID, (server, player, handler, packet, responseSender) -> {
			server.execute(() -> {
				DebugFeaturesImpl.setEnabledForPlayer(player, packet.features());
			});
		});
	}

	@ClientOnly
	public static void clientInit() {
		if (Initializer.HAS_NETWORKING) {
			ClientPlayNetworking.registerGlobalReceiver(DebugFeatureSyncPacket.ID, (client, handler, packet, responseSender) -> {
				client.execute(() -> {
					DebugFeaturesImpl.setEnabledOnServer(packet.features());
					DebugFeatureSync.syncFeaturesToServer();
				});
			});
		}
	}
}

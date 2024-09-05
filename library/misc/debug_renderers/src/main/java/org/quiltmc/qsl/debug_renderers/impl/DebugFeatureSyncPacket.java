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

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.payload.CustomPayload;

import org.quiltmc.qsl.debug_renderers.api.DebugFeature;

/**	This payload syncs the enabled and available {@code DebugFeature}s from the server to the Client.
 * */
public record DebugFeatureSyncPacket(Map<DebugFeature, Boolean> features) implements CustomPayload {
	public static final Codec<DebugFeatureSyncPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(
				DebugFeature.CODEC,
				Codec.BOOL
			).fieldOf("states").forGetter(DebugFeatureSyncPacket::features)).apply(instance, DebugFeatureSyncPacket::new));
	public static final PacketCodec<ByteBuf, DebugFeatureSyncPacket> PACKET_CODEC = PacketCodecs.fromCodec(DebugFeatureSyncPacket.CODEC);
	public static final CustomPayload.Id<DebugFeatureSyncPacket> ID = new CustomPayload.Id<>(Initializer.id("sync_features"));

	public Map<DebugFeature, Boolean> toMap() {
		HashMap<DebugFeature, Boolean> map = new HashMap<>();
		this.features.forEach((feature, enabled) -> map.put(feature, enabled));
		return map;
	}

	public static Map<DebugFeature, Boolean> readStatuses(PacketByteBuf buf) {
		final int size = buf.readVarInt();
		Map<DebugFeature, Boolean> statuses = new HashMap<>();
		for (int i = 0; i < size; i++) {
			var featureId = buf.readIdentifier();
			@Nullable DebugFeature feature = DebugFeaturesImpl.get(featureId);
			if (feature == null) {
				Initializer.LOGGER.warn("Received value for unknown debug feature {}", featureId);
				continue;
			}

			Boolean enabled = feature.isEnabled();
			statuses.put(feature, enabled);
		}

		return statuses;
	}

	public void write(PacketByteBuf buf) {
		buf.writeVarInt(this.features.size());
		this.features.forEach((feature, enabled) -> {
			buf.writeIdentifier(feature.id());
			buf.writeBoolean(DebugFeaturesImpl.isEnabled(feature));
		});
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	private void convertStatuses(PacketByteBuf buf) {
		//List<>
		//DebugFeatureSync.readStatuses(buf).forEach();
	}
}

package org.quiltmc.qsl.debug_renderers.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;

import java.util.HashMap;
import java.util.Map;

/**	This payload syncs the enabled and available {@code DebugFeature}s from the server to the Client
 * */
public record DebugFeatureSyncPacket(Map<DebugFeature, Boolean> features) implements CustomPayload {
	public DebugFeatureSyncPacket(PacketByteBuf buf) {
		this(readStatuses(buf));
	}
	public DebugFeatureSyncPacket(int value) {
		this(Map.of());
	}
	public static final Codec<DebugFeatureSyncPacket> CODEC = RecordCodecBuilder.create(instance->instance.group(
		Codec.unboundedMap(
			DebugFeature.CODEC,
			Codec.BOOL
		).fieldOf("states").forGetter(DebugFeatureSyncPacket::features)).apply(instance,DebugFeatureSyncPacket::new));
	public static final PacketCodec<ByteBuf, DebugFeatureSyncPacket> PACKET_CODEC = PacketCodecs.fromCodec(DebugFeatureSyncPacket.CODEC);
	public static final CustomPayload.Id<DebugFeatureSyncPacket> ID = new CustomPayload.Id<>(Initializer.id("sync_features"));


	public Map<DebugFeature,Boolean> toMap() {
		HashMap<DebugFeature,Boolean> map = new HashMap<>();
		this.features.forEach((feature,enabled)-> map.put(feature, enabled));
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
		buf.writeVarInt(features.size());
		features.forEach((feature,enabled)->{
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

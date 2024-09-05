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

package org.quiltmc.qsl.debug_renderers.api;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.network.codec.PacketCodec;

import org.quiltmc.qsl.debug_renderers.impl.DebugFeaturesImpl;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public final class DebugFeature {
	private final Identifier id;
	private final boolean needsServer;

	public static final Codec<DebugFeature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(DebugFeature::id),
			Codec.BOOL.fieldOf("needsServer").forGetter(DebugFeature::needsServer)
	).apply(instance, DebugFeature::new));
	public static final PacketCodec<ByteBuf, DebugFeature> PACKET_CODEC = PacketCodecs.fromCodec(CODEC);

	private DebugFeature(Identifier id, boolean needsServer) {
		this.id = id;
		this.needsServer = needsServer;
	}

	public boolean isEnabled() {
		return DebugFeaturesImpl.isEnabled(this);
	}

	public Collection<ServerPlayerEntity> getPlayersWithFeatureEnabled(MinecraftServer server) {
		return DebugFeaturesImpl.isEnabled(this)
			?
			PlayerLookup.all(server).stream().filter(p -> DebugFeaturesImpl.isEnabledForPlayer(p, this)).toList()
			:
			List.of();
	}

	public boolean isEnabledOnServerAndClient(ServerPlayerEntity player) {
		return DebugFeaturesImpl.isEnabled(this) && DebugFeaturesImpl.isEnabledForPlayer(player, this);
	}

	@ClientOnly
	public boolean isEnabledOnServerAndClient() {
		return DebugFeaturesImpl.isEnabledOnServer(this) && DebugFeaturesImpl.isEnabledOnServer(this);
	}

	@ClientOnly
	public boolean shouldRender() {
		return this.isEnabled() && !this.needsServer() || this.isEnabledOnServerAndClient();
	}

	public static DebugFeature register(Identifier id, boolean needsServer) {
		var existingFeature = DebugFeaturesImpl.get(id);
		if (existingFeature != null) {
			throw new IllegalArgumentException("A debug feature with the id %s already exists!".formatted(id));
		}

		return DebugFeaturesImpl.register(new DebugFeature(id, needsServer));
	}

	public Identifier id() {
		return this.id;
	}

	public boolean needsServer() {
		return this.needsServer;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (DebugFeature) obj;
		return Objects.equals(this.id, that.id)
			&&
			this.needsServer == that.needsServer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.needsServer);
	}

	@Override
	public String toString() {
		return "DebugFeature["
			+ "id=" + this.id + ", " + "needsServer=" + this.needsServer + ']';
	}
}

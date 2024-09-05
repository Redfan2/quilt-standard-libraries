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

import net.minecraft.util.Identifier;

import org.quiltmc.qsl.debug_renderers.api.DebugFeature;

/**
 * In this class are {@link DebugFeature DebugFeatures} for the vanilla Debug Renderers which do not have other means
 * of activation (i.e., not chunk borders, not collision, and not game test).
 */
//TODO Javadoc
public final class VanillaDebugFeatures {
	//TODO fill in new Renderers
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#supportBlockDebugRenderer
	 * @see net.minecraft.client.render.debug.DebugRenderer#gameTestDebugRenderer
	 * @see net.minecraft.client.render.debug.DebugRenderer#villageDebugRenderer
	 * */

	public static final DebugFeature BREEZE = DebugFeature.register(Identifier.ofDefault("breeze"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#pathfindingDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.PathDebugPayload
	 */
	public static final DebugFeature PATHFINDING = DebugFeature.register(Identifier.ofDefault("pathfinding"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#waterDebugRenderer
	 */
	public static final DebugFeature WATER = DebugFeature.register(Identifier.ofDefault("water"), false);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#heightmapDebugRenderer
	 */
	public static final DebugFeature HEIGHTMAP = DebugFeature.register(Identifier.ofDefault("heightmap"), false);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#neighborUpdateDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.NeighborsUpdateDebugPayload
	 */
	public static final DebugFeature NEIGHBORS_UPDATE = DebugFeature.register(Identifier.ofDefault("neighbors_update"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#structureDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.StructuresDebugPayload
	 */
	public static final DebugFeature STRUCTURE = DebugFeature.register(Identifier.ofDefault("structure"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#skyLightDebugRenderer
	 */
	public static final DebugFeature LIGHT = DebugFeature.register(Identifier.ofDefault("light"), false);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#worldGenAttemptDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.WorldGenAttemptDebugPayload
	 */
	public static final DebugFeature WORLD_GEN_ATTEMPT = DebugFeature.register(Identifier.ofDefault("world_gen_attempt"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#blockOutlineDebugRenderer
	 */
	public static final DebugFeature SOLID_FACE = DebugFeature.register(Identifier.ofDefault("solid_face"), false);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#chunkLoadingDebugRenderer
	 */
	public static final DebugFeature CHUNK = DebugFeature.register(Identifier.ofDefault("chunk"), false);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#villageDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.PoiAddedDebugPayload
	 * @see net.minecraft.network.packet.s2c.payload.PoiRemovedDebugPayload
	 * @see net.minecraft.network.packet.s2c.payload.PoiTicketCountDebugPayload
	 * @see net.minecraft.network.packet.s2c.payload.BrainDebugPayload
	 */
	public static final DebugFeature BRAIN = DebugFeature.register(Identifier.ofDefault("brain"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#villageSectionsDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.VillageSectionsDebugPayload
	 */
	public static final DebugFeature VILLAGE_SECTIONS = DebugFeature.register(Identifier.ofDefault("village_sections"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#beeDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.BeeDebugPayload
	 * @see net.minecraft.network.packet.s2c.payload.HiveDebugPayload
	 */
	public static final DebugFeature BEE = DebugFeature.register(Identifier.ofDefault("bee"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#raidCenterDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.RaidsDebugPayload
	 */
	public static final DebugFeature RAID = DebugFeature.register(Identifier.ofDefault("raid"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#goalSelectorDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.GoalSelectorDebugPayload
	 */
	public static final DebugFeature GOAL_SELECTOR = DebugFeature.register(Identifier.ofDefault("goal_selector"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#gameEventDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.GameEventDebugPayload
	 */
	public static final DebugFeature GAME_EVENT = DebugFeature.register(Identifier.ofDefault("game_event"), true);
	/**
	 * @see net.minecraft.client.render.debug.DebugRenderer#gameEventDebugRenderer
	 * @see net.minecraft.network.packet.s2c.payload.GameEventListenersDebugPayload
	 */
	public static final DebugFeature GAME_EVENT_LISTENERS = DebugFeature.register(Identifier.ofDefault("game_event_listeners"), true);

	private VanillaDebugFeatures() {}

	static void init() {}
}

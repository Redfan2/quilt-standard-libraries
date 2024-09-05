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

import net.minecraft.client.MinecraftClient;

import org.quiltmc.qsl.debug_renderers.api.client.DebugRendererRegistrationCallback;
import org.quiltmc.qsl.debug_renderers.impl.VanillaDebugFeatures;

public class VanillaDebugRenderers implements DebugRendererRegistrationCallback {
	@Override
	public void registerDebugRenderers(DebugRendererRegistrar registrar) {
		var debugRenderer = MinecraftClient.getInstance().debugRenderer;
		registrar.register(VanillaDebugFeatures.PATHFINDING, debugRenderer.pathfindingDebugRenderer);
		registrar.register(VanillaDebugFeatures.WATER, debugRenderer.waterDebugRenderer);
		registrar.register(VanillaDebugFeatures.HEIGHTMAP, debugRenderer.heightmapDebugRenderer);
		registrar.register(VanillaDebugFeatures.NEIGHBORS_UPDATE, debugRenderer.neighborUpdateDebugRenderer);
		registrar.register(VanillaDebugFeatures.STRUCTURE, debugRenderer.structureDebugRenderer);
		registrar.register(VanillaDebugFeatures.LIGHT, debugRenderer.skyLightDebugRenderer);
		registrar.register(VanillaDebugFeatures.WORLD_GEN_ATTEMPT, debugRenderer.worldGenAttemptDebugRenderer);
		registrar.register(VanillaDebugFeatures.SOLID_FACE, debugRenderer.blockOutlineDebugRenderer);
		registrar.register(VanillaDebugFeatures.CHUNK, debugRenderer.chunkLoadingDebugRenderer);
		registrar.register(VanillaDebugFeatures.BRAIN, debugRenderer.villageDebugRenderer);
		registrar.register(VanillaDebugFeatures.VILLAGE_SECTIONS, debugRenderer.villageSectionsDebugRenderer);
		registrar.register(VanillaDebugFeatures.BEE, debugRenderer.beeDebugRenderer);
		registrar.register(VanillaDebugFeatures.RAID, debugRenderer.raidCenterDebugRenderer);
		registrar.register(VanillaDebugFeatures.GOAL_SELECTOR, debugRenderer.goalSelectorDebugRenderer);
		registrar.register(VanillaDebugFeatures.GAME_EVENT, debugRenderer.gameEventDebugRenderer);
	}
}

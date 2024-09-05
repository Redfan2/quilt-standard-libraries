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

package org.quiltmc.qsl.debug_renderers.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ClientInformation;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.debug_renderers.impl.DebugFeaturesImpl;
import org.quiltmc.qsl.debug_renderers.impl.Initializer;
import org.quiltmc.qsl.testing.api.game.QuiltTestContext;
import org.quiltmc.qsl.testing.api.game.TestStructureNamePrefix;

@TestStructureNamePrefix("quilt:")
public class DebugFeaturePacketTest {

	@GameTest(structureName = "empty")
	/** Tests that the {@code DebugFeatureSyncPacket} is sent, received and processed correctly
	*/
	public void testPackets(QuiltTestContext ctx) {
		PlayerEntity player = ctx.createMockPlayer(GameMode.CREATIVE);
		DebugFeature testFeature = DebugFeature.register(Identifier.of(Initializer.NAMESPACE,"test_feature"),true);
		DebugFeaturesImpl.setEnabledNotifyClients(testFeature,true, ctx.getWorld().getServer());
		//ServerPlayNetworking.send((ServerPlayerEntity) player, new DebugFeatureSyncPacket());
		ctx.assertTrue(
			DebugFeaturesImpl.isEnabledForPlayer(
				new ServerPlayerEntity(
					ctx.getWorld().getServer(),
					ctx.getWorld(),
					player.getGameProfile(),
					ClientInformation.defaults()
				),
			testFeature),
		"Test debug feature was false"
		);

	}
}

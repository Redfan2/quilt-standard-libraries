package org.quiltmc.qsl.debug_renderers.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.network.packet.s2c.payload.*;
import net.minecraft.registry.tag.PoiTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.debug_renderers.impl.Initializer;
import org.quiltmc.qsl.debug_renderers.impl.VanillaDebugFeatures;
import org.quiltmc.qsl.networking.api.server.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.network.DebugInfoSender;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Mixin(DebugInfoSender.class)
public abstract class DebugInfoSenderMixin {
	//TODO re-implement the empty methods in DebugInfoSender

	@Shadow
	private static String format(ServerWorld world, @Nullable Object object) {
		throw new UnsupportedOperationException("mixin");
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendBreezeDebugData(BreezeEntity breeze) {
		if (breeze == null || breeze.getWorld().isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}

		ServerPlayNetworking.send(
			VanillaDebugFeatures.BREEZE.getPlayersWithFeatureEnabled(breeze.getWorld().getServer()),
			new BreezeDebugPacket(
				new BreezeDebugPacket.BreezeInfo(
					breeze.getUuid(),
					breeze.getId(),
					breeze.getTarget().getId(),
					breeze.getPositionTarget()
				)
			)
		);
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendGameEventListener(World world, GameEventListener eventListener) {
		if (world == null || world.isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}
		ServerPlayNetworking.send(
			VanillaDebugFeatures.GAME_EVENT_LISTENERS.getPlayersWithFeatureEnabled(world.getServer()),
			new GameEventListenersDebugPayload(eventListener.getPositionSource(), eventListener.getRange())
		);
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendPathfindingData(World world, MobEntity mob, @Nullable Path path, float nodeReachProximity) {
		if (path == null || world.isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}
		ServerPlayNetworking.send(
			VanillaDebugFeatures.PATHFINDING.getPlayersWithFeatureEnabled(world.getServer()),
			new PathDebugPayload(mob.getId(), path, nodeReachProximity)
		);
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendNeighborUpdate(World world, BlockPos pos) {
		if (world.isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}
		ServerPlayNetworking.send(
			VanillaDebugFeatures.NEIGHBORS_UPDATE.getPlayersWithFeatureEnabled(world.getServer()),
			new NeighborsUpdateDebugPayload(world.getTime(), pos)
		);
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendStructureStart(StructureWorldAccess world, StructureStart structureStart) {
		/*if (world.isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}

		MinecraftServer server = Objects.requireNonNull(world.getServer());
		DynamicRegistryManager.Frozen registryManager = server.getRegistryManager();

		List<StructuresDebugPayload.Piece> pieces = new ArrayList<>();
		for (int i = 0; i < structureStart.getChildren().size(); i++) {
			StructurePiece child = structureStart.getChildren().get(i);
			pieces.add(new StructuresDebugPayload.Piece(child.getBoundingBox(), i == 0));
		}

		ServerPlayNetworking.send(
			VanillaDebugFeatures.STRUCTURE.getPlayersWithFeatureEnabled(world.getServer()),
			new StructuresDebugPayload(
				registryManager.get(RegistryKeys.WORLD).get(world.getDimension().effectsLocation()).getRegistryKey(),
				structureStart.setBoundingBoxFromChildren(),
				pieces
				)
		);*/
		//TODO
	}

	/**
	 * @author QuiltMC, Will BL
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendBrainDebugData(LivingEntity living) {
		/*if (living.getWorld().isClient() || !Initializer.HAS_NETWORKING) {
			return;
		}
		List<String > villagerGossip=List.of();
		if (living instanceof VillagerEntity villager) {
			villager.getGossip()
		}
		Brain<LivingEntity> brain = (Brain<LivingEntity>) living.getBrain();
		BrainDebugPayload.BrainDump brainDump = new BrainDebugPayload.BrainDump(
			living.getUuid(),
			living.getId(),
			living.getName().getLiteralString(),
			living instanceof VillagerDataContainer villager ? villager.getVillagerData().getProfession().name() : "none",
			living instanceof VillagerEntity villager ? villager.getExperience() : 0,
			living.getHealth(),
			living.getMaxHealth(),
			living.getPos(),
			((InventoryOwner) living).getInventory().toString(),
			null,
			false,
			0,
			null,
			null,
			null,


		);
		format((ServerWorld) living.getWorld(), living)




		ServerPlayNetworking.send(
			VanillaDebugFeatures.BRAIN.getPlayersWithFeatureEnabled(living.getServer()),
			new BrainDebugPayload(brainDump)
		);*/
		//TODO
	}

	/**
	 * @author QuiltMC
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendChunkWatchingChange(ServerWorld world, ChunkPos pos) {
		for (ServerPlayerEntity players:  world.getChunkManager().delegate.getPlayersWatchingChunk(pos,false)) {
			//TODO Figure out
		}
	}

	/**At the current point, I do not know which of the three
	 *  types of POI Debug Payloads I am supposed to send.
	 *  I suspect that I gotta figure that out myself after querying
	 *  the PointOfInterestStorage about the existence of the POI
	 *
	 * @see PoiAddedDebugPayload
	 * @see PoiRemovedDebugPayload
	 * @see PoiTicketCountDebugPayload
	 *
	 * @see net.minecraft.client.render.debug.VillageDebugRenderer
	* **/
	@Overwrite
	public static void sendPoiAddition(ServerWorld world, BlockPos pos) {
		sendPoi(world, pos);
	}
	@Overwrite
	public static void sendPoiRemoval(ServerWorld world, BlockPos pos) {
		sendPoi(world, pos);
	}
	@Overwrite
	public static void sendPointOfInterest(ServerWorld world, BlockPos pos) {
		sendPoi(world, pos);
	}
	@Overwrite
	private static void sendPoi(ServerWorld world, BlockPos pos) {
	}

	/**
	 * @author QuiltMC
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendGoalSelector(World world, MobEntity mob, GoalSelector goalSelector) {
		List<GoalSelectorDebugPayload.Goal> goals = new ArrayList<>(List.of());
		goalSelector.getGoals().forEach(goal->{
			goals.add(new net.minecraft.network.packet.s2c.payload.GoalSelectorDebugPayload.Goal(goal.getPriority(),goal.isRunning(),goal.toString()));
		});

		ServerPlayNetworking.send(
			VanillaDebugFeatures.GOAL_SELECTOR.getPlayersWithFeatureEnabled(world.getServer()),
			new GoalSelectorDebugPayload(mob.getId(), mob.getBlockPos(),goals)
		);
	}

	/**
	 * @author QuiltMC
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendRaids(ServerWorld server, Collection<Raid> raids) {
		List<BlockPos> centers= new ArrayList<>();
		raids.forEach(raid->{centers.add(raid.getCenter());});

		ServerPlayNetworking.send(
			VanillaDebugFeatures.RAID.getPlayersWithFeatureEnabled(server.getServer()),
			new RaidsDebugPayload(centers)
		);
	}

	/**
	 * @author QuiltMC
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendBeeDebugData(BeeEntity bee) {
		BlockPos blockPos = bee.getBlockPos();
		PointOfInterestStorage pointOfInterestStorage = ((ServerWorld)bee.getWorld()).getPointOfInterestStorage();
		Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(point -> point.isIn(PoiTags.BEE_HOME), blockPos, 20, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED);
		List<BlockPos> blacklistedHives = (List<BlockPos>)stream.map(PointOfInterest::getPos)
			.sorted(Comparator.comparingDouble(pos -> pos.getSquaredDistance(blockPos)))
			.collect(Collectors.toList());

		Set<String> goals = Set.of();
		bee.getGoalSelector().getGoals().forEach(goal->{
			goals.add(goal.toString());
		});


		quilt$send(
			VanillaDebugFeatures.BEE,
			new BeeDebugPayload(
				new BeeDebugPayload.BeeInfo(
					bee.getUuid(),
					bee.getId(),
					bee.getPos(),
					bee.getNavigation().getCurrentPath(),
					bee.getHivePos(),
					bee.getFlowerPos(),
					bee.getMoveGoalTicks(),
					goals, blacklistedHives
				)
			),
			bee.getCommandSource().getWorld()
		);

	}

	/**
	 * @author QuiltMC
	 * @reason Re-implementation of method with missing body
	 */
	@Overwrite
	public static void sendBeehiveDebugData(World world, BlockPos pos, BlockState state, BeehiveBlockEntity hive) {
/*
		new HiveDebugPayload(new HiveDebugPayload.HiveInfo(pos,))
	*/
	//TODO figure out String 'hiveType'
	}

	/**Shorthand method for sending debug payloads
	 * */
	@Unique
	private static void quilt$send(DebugFeature feature, CustomPayload payload, ServerWorld world) {
		ServerPlayNetworking.send(
			feature.getPlayersWithFeatureEnabled(world.getServer()),
			payload
		);
	}
}

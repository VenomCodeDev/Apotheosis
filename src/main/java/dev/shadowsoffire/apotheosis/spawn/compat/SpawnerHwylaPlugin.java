package dev.shadowsoffire.apotheosis.spawn.compat;

import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.spawn.modifiers.SpawnerStats;
import dev.shadowsoffire.apotheosis.spawn.spawner.ApothSpawnerBlock;
import dev.shadowsoffire.apotheosis.spawn.spawner.ApothSpawnerTile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BaseSpawner;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class SpawnerHwylaPlugin implements IWailaPlugin, IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final String STATS = "spw_stats";

    @Override
    public void register(IWailaCommonRegistration reg) {
        reg.registerBlockDataProvider(this, ApothSpawnerTile.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration reg) {
        reg.registerBlockComponent(this, ApothSpawnerBlock.class);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (Screen.hasControlDown()) {
            int[] stats = accessor.getServerData().getIntArray(STATS);
            if (stats.length != 12) return;
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.MIN_DELAY.name(), stats[0]));
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.MAX_DELAY.name(), stats[1]));
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.SPAWN_COUNT.name(), stats[2]));
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.MAX_NEARBY_ENTITIES.name(), stats[3]));
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.REQ_PLAYER_RANGE.name(), stats[4]));
            tooltip.add(ApothSpawnerBlock.concat(SpawnerStats.SPAWN_RANGE.name(), stats[5]));
            if (stats[6] == 1) tooltip.add(SpawnerStats.IGNORE_PLAYERS.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[7] == 1) tooltip.add(SpawnerStats.IGNORE_CONDITIONS.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[8] == 1) tooltip.add(SpawnerStats.REDSTONE_CONTROL.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[9] == 1) tooltip.add(SpawnerStats.IGNORE_LIGHT.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[10] == 1) tooltip.add(SpawnerStats.NO_AI.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[11] == 1) tooltip.add(SpawnerStats.SILENT.name().withStyle(ChatFormatting.DARK_GREEN));
        }
        else tooltip.add(Component.translatable("misc.apotheosis.ctrl_stats"));
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor access) {
        if (access.getBlockEntity() instanceof ApothSpawnerTile spw) {
            BaseSpawner logic = spw.getSpawner();

            tag.putIntArray(STATS,
                new int[] {
                    logic.minSpawnDelay,
                    logic.maxSpawnDelay,
                    logic.spawnCount,
                    logic.maxNearbyEntities,
                    logic.requiredPlayerRange,
                    logic.spawnRange,
                    spw.ignoresPlayers ? 1 : 0,
                    spw.ignoresConditions ? 1 : 0,
                    spw.redstoneControl ? 1 : 0,
                    spw.ignoresLight ? 1 : 0,
                    spw.hasNoAI ? 1 : 0,
                    spw.silent ? 1 : 0
                });

        }
    }

    @Override
    public ResourceLocation getUid() {
        return Apotheosis.loc("spawner");
    }

}

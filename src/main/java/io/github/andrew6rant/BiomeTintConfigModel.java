package io.github.andrew6rant;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Expanded;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Modmenu(modId = "biome-tint")
@Config(name = "biome-tint", wrapperName = "BiomeTintConfig")
public class BiomeTintConfigModel {
    @Expanded
    public List<String> blockIds = Arrays.asList("minecraft:mossy_cobblestone#8425a3", "minecraft:mossy_stone_bricks#3495eb", "minecraft:oak_sapling#foliage");
    @Expanded
    public List<String> itemIds = Arrays.asList();
}

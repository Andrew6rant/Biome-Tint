package io.github.andrew6rant;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BiomeTintConfig extends ConfigWrapper<BiomeTintConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.util.List<java.lang.String>> blockIds = this.optionForKey(this.keys.blockIds);
    private final Option<java.util.List<java.lang.String>> itemIds = this.optionForKey(this.keys.itemIds);

    private BiomeTintConfig() {
        super(io.github.andrew6rant.BiomeTintConfigModel.class);
    }

    private BiomeTintConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(io.github.andrew6rant.BiomeTintConfigModel.class, janksonBuilder);
    }

    public static BiomeTintConfig createAndLoad() {
        var wrapper = new BiomeTintConfig();
        wrapper.load();
        return wrapper;
    }

    public static BiomeTintConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new BiomeTintConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public java.util.List<java.lang.String> blockIds() {
        return blockIds.value();
    }

    public void blockIds(java.util.List<java.lang.String> value) {
        blockIds.set(value);
    }

    public java.util.List<java.lang.String> itemIds() {
        return itemIds.value();
    }

    public void itemIds(java.util.List<java.lang.String> value) {
        itemIds.set(value);
    }


    public static class Keys {
        public final Option.Key blockIds = new Option.Key("blockIds");
        public final Option.Key itemIds = new Option.Key("itemIds");
    }
}


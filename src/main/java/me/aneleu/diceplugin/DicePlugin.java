package me.aneleu.diceplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DicePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("DicePlugin enabled!");

        Objects.requireNonNull(getCommand("dice")).setExecutor(new DiceCommand(this));
    }

}

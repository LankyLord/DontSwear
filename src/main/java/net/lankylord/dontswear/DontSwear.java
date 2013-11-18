/*
 * Copyright (c) 2013, LankyLord
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.lankylord.dontswear;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lankylord.dontswear.commands.AddSwearCommand;
import net.lankylord.dontswear.commands.RemoveSwearCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author LankyLord
 */
public class DontSwear extends JavaPlugin {

    private SwearManager manager;

    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger.info("Don't Swear Enabled.")
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();

        manager = new SwearManager(this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
        loadCommands();

        if (getConfig().getBoolean("AutoUpdater.Enabled")) {
            Updater updater = new Updater(this, 52997, this.getFile(), Updater.UpdateType.DEFAULT, true);
            getLogger.info("[DontSwear] AutoUpdater Enabled.");
        }

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            getLogger.info("[DontSwear] Error while submitting stats.");
        }
    }

    private void loadCommands() {
        getCommand("addswear").setExecutor(new AddSwearCommand(manager));
        getCommand("delswear").setExecutor(new RemoveSwearCommand(manager));
    }
}

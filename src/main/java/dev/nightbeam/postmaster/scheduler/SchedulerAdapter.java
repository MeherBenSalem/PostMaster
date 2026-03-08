package dev.nightbeam.postmaster.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public final class SchedulerAdapter {
    private final Plugin plugin;
    private final boolean folia;

    public SchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
        this.folia = detectFolia();
    }

    public boolean isFolia() {
        return folia;
    }

    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void runGlobalSync(Runnable runnable) {
        if (folia) {
            invokeGlobalFoliaTask(runnable);
            return;
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public void runAtPlayer(Player player, Runnable runnable) {
        if (!folia) {
            Bukkit.getScheduler().runTask(plugin, runnable);
            return;
        }

        try {
            Method getScheduler = player.getClass().getMethod("getScheduler");
            Object scheduler = getScheduler.invoke(player);
            Method execute = scheduler.getClass().getMethod("execute", Plugin.class, Runnable.class, Runnable.class, long.class);
            execute.invoke(scheduler, plugin, runnable, null, 1L);
        } catch (ReflectiveOperationException ex) {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    private boolean detectFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private void invokeGlobalFoliaTask(Runnable runnable) {
        try {
            Object server = Bukkit.getServer();
            Method getGlobalRegionScheduler = server.getClass().getMethod("getGlobalRegionScheduler");
            Object scheduler = getGlobalRegionScheduler.invoke(server);
            Method execute = scheduler.getClass().getMethod("execute", Plugin.class, Runnable.class);
            execute.invoke(scheduler, plugin, runnable);
        } catch (ReflectiveOperationException ex) {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
}

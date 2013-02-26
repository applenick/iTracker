package tc.oc.tracker.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import tc.oc.tracker.DamageResolverManager;
import tc.oc.tracker.DamageResolvers;
import tc.oc.tracker.TrackerManager;
import tc.oc.tracker.Trackers;
import tc.oc.tracker.damage.resolvers.*;
import tc.oc.tracker.trackers.ExplosiveTracker;
import tc.oc.tracker.trackers.base.SimpleExplosiveTracker;

public class TrackerPlugin extends JavaPlugin {
    @Override
    public void onDisable() {
        Trackers.getManager().clearTracker(ExplosiveTracker.class, SimpleExplosiveTracker.class);
    }

    @Override
    public void onEnable() {
        // basic operation listeners
        this.registerEvents(new LifetimeListener());
        this.registerEvents(new WorldListener(Trackers.getManager()));

        EntityDamageEventListener damageEventListener = new EntityDamageEventListener();
        damageEventListener.register(this);

        // tracker setup
        TrackerManager tm = Trackers.getManager();

        ExplosiveTracker explosiveTracker = new SimpleExplosiveTracker();
        explosiveTracker.enable();

        this.registerEvents(new ExplosiveListener(explosiveTracker));
        tm.setTracker(ExplosiveTracker.class, explosiveTracker);

        // register damage resolvers
        DamageResolverManager drm = DamageResolvers.getManager();

        drm.register(new BlockDamageResolver());
        drm.register(new FallDamageResolver());
        drm.register(new LavaDamageResolver());
        drm.register(new ProjectileDamageResolver());
        drm.register(new TNTDamageResolver(explosiveTracker));
        drm.register(new VoidDamageResolver());

        // debug
        this.registerEvents(new DebugListener());
    }

    private void registerEvents(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}

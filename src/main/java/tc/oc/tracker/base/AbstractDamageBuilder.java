package tc.oc.tracker.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.joda.time.Instant;

import tc.oc.tracker.Damage;

import com.google.common.base.Preconditions;

public abstract class AbstractDamageBuilder<T extends AbstractDamageBuilder<T>> {
    /**
     * Fetches the {@code this} instance of the underlying class.
     * @return This instance of the class
     */
    protected abstract @Nonnull T me();

    public @Nonnull T hearts(int hearts) {
        Preconditions.checkArgument(hearts > 0, "hearts must be greater than 0");

        this.hearts = hearts;

        return this.me();
    }

    public @Nonnull T location(@Nonnull Location newLocation) {
        Preconditions.checkNotNull(newLocation, "new location");

        this.location = newLocation.clone();

        return this.me();
    }

    public @Nonnull T time(@Nonnull Instant time) {
        Preconditions.checkNotNull(time, "time");

        this.time = time;

        return this.me();
    }

    public @Nonnull T resolvedDamager(@Nullable LivingEntity resolvedDamager) {
        this.resolvedDamager = resolvedDamager;

        return this.me();
    }

    public @Nonnull T base(@Nonnull Damage base) {
        Preconditions.checkNotNull(base, "base");

        this.hearts(base.getHearts());
        this.location(base.getLocation());
        this.time(base.getTime());
        this.resolvedDamager(base.getResolvedDamager());

        return this.me();
    }

    public @Nonnull T base(@Nonnull EntityDamageEvent event) {
        Preconditions.checkNotNull(event, "entity damage event");

        this.hearts(event.getDamage());
        this.location(event.getEntity().getLocation());
        this.time(Instant.now());

        return this.me();
    }

    protected @Nonnull Damage buildPartial() throws IllegalStateException {
        Preconditions.checkState(this.hearts > 0, "hearts must be greater than zero");
        Preconditions.checkState(this.location != null, "location cannot be null");
        Preconditions.checkState(this.time != null, "time cannot be null");

        return new BaseDamage(this.hearts, this.location, this.time, this.resolvedDamager);
    }

    protected int hearts = 0;
    protected @Nullable Location location;
    protected @Nullable Instant time;
    protected @Nullable LivingEntity resolvedDamager;

    private static class BaseDamage extends AbstractDamage {
        public BaseDamage(int hearts, Location location, Instant time, LivingEntity livingCause) {
            super(hearts, location, time, livingCause);
        }
    }
}

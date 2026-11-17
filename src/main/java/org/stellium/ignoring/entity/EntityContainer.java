package org.stellium.ignoring.entity;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class EntityContainer {

	@Nullable
	private Entity entity;
	private boolean enabled;

	public EntityContainer() {
		this.entity = null;
		this.enabled = true;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setEntity(@Nullable Entity entity) {
		if (!this.isEnabled()) {
			return;
		}
		this.entity = entity;
	}

	@Nullable
	public Entity getEntity() {
		return this.isEnabled() ? this.entity : null;
	}
}

package grondag.hs.dialog;

import net.minecraft.entity.player.PlayerEntity;

public abstract class DialogState<T> {
	public final T owner;
	public final PlayerEntity player;

	public DialogState(T owner, PlayerEntity player) {
		this.owner = owner;
		this.player = player;
	}

	abstract public DialogNode<T> rootNode();
}

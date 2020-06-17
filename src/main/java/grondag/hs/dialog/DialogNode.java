package grondag.hs.dialog;

import com.google.common.collect.ImmutableList;

public class DialogNode<T> {
	public final  ImmutableList<DialogOption<T>> actions;
	public final T state;
	public final String npcText;

	public DialogNode(T state, String npcText, ImmutableList<DialogOption<T>> actions) {
		this.state = state;
		this.npcText = npcText;
		this.actions = actions;
	}

	public static <T> DialogNode<T> of(T state, String text, ImmutableList<DialogOption<T>> actions) {
		return new DialogNode<>(state, text, actions);
	}
}

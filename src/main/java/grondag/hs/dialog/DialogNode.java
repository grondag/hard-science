package grondag.hs.dialog;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class DialogNode<T> {
	public final  ObjectArrayList<DialogOption<T>> actions = new ObjectArrayList<>();

	public abstract Text text();

	@SafeVarargs
	public DialogNode(Identifier id, DialogOption<T>... actions) {
		this.actions.addElements(0, actions);
	}
}

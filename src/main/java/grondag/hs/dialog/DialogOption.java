package grondag.hs.dialog;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DialogOption<T> {
	public final Text text;

	public final DialogAction<T> action;

	public DialogOption(Text text, DialogAction<T> action) {
		this.text = text;
		this.action = action;
	}

	static <T> DialogOption<T> of(Text text, DialogAction<T> action) {
		return new DialogOption<>(text, action);
	}

	static <T> DialogOption<T> of(String textId, DialogAction<T> action) {
		return new DialogOption<>(new TranslatableText(textId), action);
	}
}

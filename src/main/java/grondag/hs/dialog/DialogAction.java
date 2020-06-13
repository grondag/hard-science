package grondag.hs.dialog;

public interface DialogAction<T> {
	DialogNode<T> apply(DialogState<T> state, DialogNode<T> node, DialogOption<T> option);
}

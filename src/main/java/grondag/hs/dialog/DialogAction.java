package grondag.hs.dialog;

public interface DialogAction<T> {
	DialogNode<T> apply(T state);
}

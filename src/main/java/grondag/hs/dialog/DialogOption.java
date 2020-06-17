package grondag.hs.dialog;

public class DialogOption<T> {
	public final String playerText;

	public final boolean journalPlayerText;

	public final DialogAction<T> action;


	public DialogOption(String playerText, boolean journalPlayerText, DialogAction<T> action) {
		this.playerText = playerText;
		this.journalPlayerText = journalPlayerText;
		this.action = action;
	}

	public String playerJournalText() {
		return journalPlayerText ? playerText : "";
	}
}

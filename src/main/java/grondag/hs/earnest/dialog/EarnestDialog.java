package grondag.hs.earnest.dialog;

import com.google.common.collect.ImmutableList;

import grondag.hs.dialog.DialogNode;
import grondag.hs.dialog.DialogOption;
import grondag.hs.earnest.EarnestPlayerState;

public class EarnestDialog {
	public static DialogNode<EarnestPlayerState> root(EarnestPlayerState state, boolean includeGreeting) {
		final ImmutableList<DialogOption<EarnestPlayerState>> list = ImmutableList.of(QUESTIONS, EXIT);
		return new DialogNode<>(state, includeGreeting ? greeting(state) : "", list);
	}

	private static String greeting(EarnestPlayerState state) {
		return "dialog.hard-science.earnest.root";
	}

	public static final DialogNode<EarnestPlayerState> EMPTY_DIALOG = new DialogNode<>(null, "", ImmutableList.of());

	public static final DialogOption<EarnestPlayerState> EXIT = new DialogOption<>("dialog.hard-science.player.exit", false, s -> null);
	static final DialogOption<EarnestPlayerState> CHANGE_TOPIC = new DialogOption<>("dialog.hard-science.player.root", false, s -> root(s, false));
	static final DialogOption<EarnestPlayerState> QUESTIONS = new DialogOption<>("dialog.hard-science.player.question", false, EarnestDialog::questions);
	static final DialogOption<EarnestPlayerState> QUESTIONS_BACK = new DialogOption<>("dialog.hard-science.player.question.back", false, EarnestDialog::questions);

	private static DialogNode<EarnestPlayerState> questions(EarnestPlayerState state) {
		final ImmutableList<DialogOption<EarnestPlayerState>> list = ImmutableList.of(CHANGE_TOPIC, EXIT);
		return new DialogNode<>(state, "", list);
	}
}

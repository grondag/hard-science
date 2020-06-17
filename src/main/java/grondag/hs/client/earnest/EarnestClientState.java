package grondag.hs.client.earnest;

import net.minecraft.client.resource.language.I18n;

import grondag.hs.earnest.EarnestEntity;
import grondag.mcmd.node.Node;
import grondag.mcmd.parser.Parser;

public class EarnestClientState {
	public static EarnestEntity earnest;
	private static final Parser PARSER = Parser.builder().build();
	private static final Node EMPTY_NODE = PARSER.parse("");

	private static String journalText = "";
	private static Node journalMarkdown = EMPTY_NODE;
	private static int journalVersion = 0;
	private static Node actionMarkdown = EMPTY_NODE;
	private static int actionVersion = 0;
	private static int actionCount = 0;

	public static Node journalMarkdown() {
		return journalMarkdown;
	}

	public static int journalVersion() {
		return journalVersion;
	}

	public static Node actionMarkdown() {
		return actionMarkdown;
	}

	public static int actionVersion() {
		return actionVersion;
	}

	public static void advanceDialog(String playerText, String npcText, String[] actions) {
		actionCount = actions.length;
		final StringBuilder builder = new StringBuilder();

		for (int i = 1; i <= actionCount; ++i) {
			builder.append(i).append(". ").append(I18n.translate(actions[i - 1])).append("\n");
		}

		actionMarkdown = PARSER.parse(builder.toString());
		++actionVersion;

		boolean rebuildJournal = false;

		if (!playerText.isEmpty()) {
			journalText += I18n.translate(playerText) + "\n";
			rebuildJournal = true;
		}

		if (!npcText.isEmpty()) {
			journalText += "**Earnest:** " + I18n.translate(npcText) + "\n";
			rebuildJournal = true;
		}

		if (rebuildJournal) {
			journalMarkdown = PARSER.parse(journalText);
			++journalVersion;
		}
	}

	public static void resetDialog() {
		journalVersion = 0;
		journalMarkdown = EMPTY_NODE;
		actionVersion = 0;
		actionMarkdown = EMPTY_NODE;
		journalText = "";
	}
}

package grondag.hs.earnest.dialog;

import net.minecraft.entity.player.PlayerEntity;

import grondag.hs.dialog.DialogNode;
import grondag.hs.dialog.DialogState;
import grondag.hs.earnest.EarnestPlayerData;

public class EarnestDialogState extends DialogState<EarnestPlayerData> {

	public EarnestDialogState(EarnestPlayerData data, PlayerEntity player) {
		super(data, player);
	}

	@Override
	public DialogNode<EarnestPlayerData> rootNode() {
		// TODO Auto-generated method stub
		return null;
	}

}

package firis.lmlib.api.caps;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * テクスチャ選択機能を使う場合に必要なインターフェース定義
 * @author firis-games
 *
 */
public interface IGuiTextureSelect {

	/**
	 * マルチモデルの名称を取得する
	 */
	public String getGuiTargetLittleMaidName();
	public String getGuiTargetArmorName(EntityEquipmentSlot slot);
	
	/**
	 * 色情報を取得する
	 */
	public int getGuiTargetColor();
	
	/**
	 * 契約状態を取得する
	 * @return
	 */
	public boolean getGuiTargetContract();
	
	/**
	 * リトルメイドモデルを設定する
	 * クライアント側のみの通知のため同期処理は独自実装が必要
	 */
	public void syncSelectTextureLittleMaid(String textureName, int color);
	
	
	/**
	 * アーマーモデルを設定する
	 * クライアント側のみの通知のため同期処理は独自実装が必要
	 */
	public void syncSelectTextureArmor(String headTextureName, String chestTextureName, String legsTextureName, String feetTextureName);
	
}

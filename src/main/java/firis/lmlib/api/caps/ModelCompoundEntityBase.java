package firis.lmlib.api.caps;

import java.util.EnumMap;
import java.util.Map;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.constant.EnumColor;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * テクスチャ管理用ベースクラス
 * 
 * 各モデルへのアクセスと設定用のメソッドと変数を定義
 */
public abstract class ModelCompoundEntityBase<T extends EntityLivingBase> implements IModelCompoundEntity {
	
	/**
	 * 対象のEntity
	 */
	protected T owner;
	
	/**
	 * 描画用のModelCaps
	 */
	protected IModelCapsEntity entityCaps;

	/**
	 * 選択色
	 */
	protected int color;
	
	/**
	 * 契約テクスチャを選択するかどうか
	 */
	protected boolean contract;
	
	/**
	 * メイドさんのテクスチャモデル
	 */
	protected LMTextureBox textureBoxLittleMaid = null;

	/**
	 * 防具のテクスチャモデル
	 */
	protected Map<EntityEquipmentSlot, LMTextureBox> textureBoxArmor = new EnumMap<>(EntityEquipmentSlot.class);
	
	/**
	 * コンストラクタ
	 * @param pEntity
	 * @param pCaps
	 */
	public ModelCompoundEntityBase(T entity, IModelCapsEntity caps) {
		
		//パラメータ保存
		this.owner = entity;
		this.entityCaps = caps;
		
		//パラメータ初期化
		this.textureBoxLittleMaid = LMLibraryAPI.instance().getTextureManager().getDefaultLMTextureBox();
		
		//防具モデル初期化
		this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		
		this.color = EnumColor.BROWN.getColor();
		this.contract = false;
		
	}
	
	/**
	 * 描画用のModelCapsを取得する
	 */
	@Override
	public IModelCapsEntity getModelCaps() {
		return this.entityCaps;
	}
	
	/**
	 * 色設定
	 * @param pColor
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * 色取得
	 * @return
	 */
	@Override
	public int getColor() {
		return this.color;
	}
	
	/**
	 * 契約状態設定
	 * @param pContract
	 */
	public void setContract(boolean contract) {
		this.contract = contract;
	}

	/**
	 * 契約状態確認
	 * @return
	 */
	@Override
	public boolean isContract() {
		return contract;
	}
	
	/**
	 * メイドさんのテクスチャ
	 * @return
	 */
	@Override
	public ResourceLocation getTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		if (!this.isContract()) {
			return this.textureBoxLittleMaid.getTextureWildLittleMaid(this.color);
		}
		return this.textureBoxLittleMaid.getTextureLittleMaid(this.color);
	}
	
	/**
	 * メイドさんの発光テクスチャ
	 * @return
	 */
	@Override
	public ResourceLocation getLightTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		if (!this.isContract()) {
			return this.textureBoxLittleMaid.getLightTextureWildLittleMaid(this.color);
		}
		return this.textureBoxLittleMaid.getLightTextureLittleMaid(this.color);
	}
	
	/**
	 * インナー防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getTextureInnerArmor(ItemStack.EMPTY);
	}
	
	/**
	 * インナー発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getLightTextureInnerArmor(ItemStack.EMPTY);
	}
	
	/**
	 * アウター防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getTextureOuterArmor(ItemStack.EMPTY);
	}
	
	/**
	 * アウター発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getLightTextureOuterArmor(ItemStack.EMPTY);
	}
	
	/**
	 * メイドモデルを取得する
	 */
	@Override
	public ModelMultiBase getModelLittleMaid() {
		if (textureBoxLittleMaid == null) return null;
		return textureBoxLittleMaid.getModelLittleMaid();
	}
	
	/**
	 * インナー防具モデルを取得する
	 * @param slot
	 * @return
	 */
	@Override
	public ModelMultiBase getModelInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getModelInnerArmor();
	}
	
	/**
	 * アウター防具モデルを取得する
	 */
	@Override
	public ModelMultiBase getModelOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getModelOuterArmor();
	}
	
	/**
	 * メイドモデルの名称を取得する
	 */
	@Override
	public String getTextureModelNameLittleMaid() {
		if (textureBoxLittleMaid == null) return null;
		return textureBoxLittleMaid.getTextureModelName();
	}
	
	/**
	 * アーマーモデルの名称を取得する
	 */
	@Override
	public String getTextureModelNameArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getTextureModelName();
	}
	
	/**
	 * メイドさんテクスチャモデルを取得する
	 * @return
	 */
	public LMTextureBox getTextureBoxLittleMaid() {
		return this.textureBoxLittleMaid;
	}
	
	/**
	 * 防具テクスチャモデルを取得する
	 * @param slot
	 * @return
	 */
	public LMTextureBox getTextureBoxArmor(EntityEquipmentSlot slot) {
		if (!textureBoxArmor.containsKey(slot)) return null;
		return textureBoxArmor.get(slot);
	}
	
	/**
	 * メイドさんモデルを設定する
	 * @param textureBox
	 */
	public void setTextureBoxLittleMaid(LMTextureBox textureBox) {
		this.textureBoxLittleMaid = textureBox;
	}
	
	/**
	 * 防具モデルを設定する
	 * @param textureBox
	 * @param slot
	 */
	public void setTextureBoxArmor(EntityEquipmentSlot slot, LMTextureBox textureBox) {
		this.textureBoxArmor.put(slot, textureBox);
	}
	
	/**
	 * 防具モデル一括扱いで取得する
	 */
	public LMTextureBox getTextureBoxArmorAll() {
		
		LMTextureBox textureBox;
				
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.HEAD);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.CHEST);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.LEGS);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.FEET);
		if (textureBox != null) return textureBox;
		
		return LMLibraryAPI.instance().getTextureManager().getDefaultLMTextureBox();
		
	}
	
	/**
	 * 防具モデルに一括で設定する
	 * @param textureBox
	 */
	public void setTextureBoxArmorAll(LMTextureBox textureBox) {
		this.textureBoxArmor.put(EntityEquipmentSlot.HEAD, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.CHEST, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.LEGS, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.FEET, textureBox);
	}
	
	/**
	 * 防具モデルの表示非表示制御用
	 * @return
	 */
	@Override
	public boolean isArmorTypeVisible(int type) {
		return true;
	}
	
	/**
	 * 対象Entityの透明判定判断
	 * @return
	 */
	@Override
	public boolean isInvisible() {
		return this.owner.isInvisible();
	}
	
	/**
	 * 描画対象の輝度を取得する
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender() {
		return this.owner.getBrightnessForRender();
	}
	
	/**
	 * メイドさんモデルを設定する(名称)
	 */
	public void setTextureLittleMaid(String texture) {
		this.setTextureBoxLittleMaid(LMLibraryAPI.instance().getTextureManager().getLMTextureBox(texture));
	}
	
	/**
	 * 防具モデルを設定する(名称)
	 * @param textureBox
	 * @param slot
	 */
	public void setTextureArmor(EntityEquipmentSlot slot, String texture) {
		this.setTextureBoxArmor(slot, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(texture));
	}
	
	/**
	 * リトルメイドマルチモデル情報をNBTへ変換する
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		String maid = this.getTextureBoxLittleMaid().getTextureModelName();
		String armorHead = this.getTextureBoxArmor(EntityEquipmentSlot.HEAD).getTextureModelName();
		String armorChest = this.getTextureBoxArmor(EntityEquipmentSlot.CHEST).getTextureModelName();
		String armorLegs = this.getTextureBoxArmor(EntityEquipmentSlot.LEGS).getTextureModelName();
		String armorFeet = this.getTextureBoxArmor(EntityEquipmentSlot.FEET).getTextureModelName();
		
		//必要な情報のみNBT化
		nbt.setString("maid", maid);
		nbt.setInteger("color", this.color);
		nbt.setString("head", armorHead);
		nbt.setString("chest", armorChest);
		nbt.setString("legs", armorLegs);
		nbt.setString("feet", armorFeet);
		nbt.setBoolean("contract", this.contract);

		return nbt;
	}
	
	/**
	 * リトルメイドマルチモデル情報をNBTから復元する
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		
		String maid = nbt.getString("maid");
		String armorHead = nbt.getString("head");
		String armorChest = nbt.getString("chest");
		String armorLegs = nbt.getString("legs");
		String armorFeet = nbt.getString("feet");

		Integer color = nbt.getInteger("color");
		boolean contract = nbt.getBoolean("contract");
		
		//展開
		this.setTextureBoxLittleMaid(LMLibraryAPI.instance().getTextureManager().getLMTextureBox(maid));
		this.setTextureBoxArmor(EntityEquipmentSlot.HEAD, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorHead));
		this.setTextureBoxArmor(EntityEquipmentSlot.CHEST, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorChest));
		this.setTextureBoxArmor(EntityEquipmentSlot.LEGS, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorLegs));
		this.setTextureBoxArmor(EntityEquipmentSlot.FEET, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorFeet));
		
		this.setColor(color);
		this.setContract(contract);
		
	}

}

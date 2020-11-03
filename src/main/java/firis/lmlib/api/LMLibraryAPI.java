package firis.lmlib.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import firis.lmlib.LMLibrary;
import firis.lmlib.api.caps.IGuiTextureSelect;
import firis.lmlib.api.constant.EnumSound;
import firis.lmlib.api.manager.LMSoundManager;
import firis.lmlib.api.manager.LMTextureBoxManager;
import firis.lmlib.client.gui.LMGuiTextureSelect;
import firis.lmlib.common.network.LMLibNetworkHandler;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * LMLibraryのAPI
 * @author firis-games
 *
 */
public class LMLibraryAPI {
	
	/**
	 * APIインスタンス
	 */
	private static final LMLibraryAPI INSTANCE = new LMLibraryAPI();
	public static LMLibraryAPI instance() {
		return INSTANCE;
	}
	
	/**
	 * テクスチャマネージャー
	 */
	private LMTextureBoxManager textureManager = new LMTextureBoxManager();
	
	/**
	 * サウンドマネージャー
	 */
	private LMSoundManager soundManager = new LMSoundManager();
	
	/**
	 * テクスチャマネージャを取得する
	 * @return
	 */
	public LMTextureBoxManager getTextureManager() {
		return this.textureManager;
	}
	
	/**
	 * サウンドマネージャを取得する
	 * @return
	 */
	public LMSoundManager getSoundManager() {
		return this.soundManager;
	}
	
	/**
	 * リトルメイド用追加モーションを登録する
	 */
	public void registerLittleMaidMotion(ILMMotion lmMotion) {
		ModelLittleMaidBase.littleMaidMotions.add(lmMotion);
	}
	
	/**
	 * モーションなしのモーションindex
	 */
	public static int LMMOTION_NO_ACTION = 0;
	
	/**
	 * 有効なモーションIDを生成
	 */
	private static Map<Integer, String> lmMotionMap = null;
	private static Map<Integer, String> getLmMotionMap() {
		if (lmMotionMap == null) {
			//初期化
			lmMotionMap = new HashMap<>();
			int id = LMMOTION_NO_ACTION + 1;
			for (ILMMotion motion : ModelLittleMaidBase.littleMaidMotions) {
				if (motion.getMotionId() != null) {
					lmMotionMap.put(id++, motion.getMotionId());
				}
			}
		}
		return lmMotionMap;
	}
	
	/**
	 * モーションindexからモーションIDを取得する
	 * @param motionId
	 * @return
	 */
	public String getLMMotionId(Integer motionIdx) {
		if (getLmMotionMap().containsKey(motionIdx)) {
			return getLmMotionMap().get(motionIdx);
		}
		return null;
	}
	
	/**
	 * モーションIDからモーションindexを取得する
	 * @param motionId
	 * @return
	 */
	public int getLMMotionIndex(String motionId) {
		for (Entry<Integer, String> entry : getLmMotionMap().entrySet()) {
			if (entry.getValue().equals(motionId)) {
				return entry.getKey();
			}
		}
		return LMMOTION_NO_ACTION;
	}
	
	/**
	 * モーションIDが有効なモーションリストを取得する
	 * @return
	 */
	public List<String> getLMMotionIdList() {
		return getLmMotionMap().values().stream().collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * サウンドパックの利用可否を取得する
	 * @return
	 */
	public boolean isSoundPack() {
		return this.soundManager.isFoundSoundpack();
	}
	
	/**
	 * サウンドパックの再生用SoundEventを取得する
	 * サウンドパックがロードされていない場合はデフォルトボイスのSoundEventを返却する
	 * @param sound
	 * @param texture
	 * @param color
	 * @param isLivingVoiceRate
	 * @return
	 */
	public SoundEvent getSoundEvent(EnumSound sound, String texture, Integer color, boolean isLivingVoiceRate) {
		SoundEvent soundEvent = null;
		if (!this.isSoundPack()) {
			//デフォルトボイス
			soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getDefaultVoice()));
		} else {
			//サウンドパックボイス
			soundEvent = getSoundEventFromSoundPack(sound, texture, color, isLivingVoiceRate);
		}
		return soundEvent;
	}
	
	/**
	 * サウンドパックの再生用SoundEventを取得する
	 * @return
	 * 再生できない場合はnullを返却する
	 */
	public SoundEvent getSoundEventFromSoundPack(EnumSound sound, String texture, Integer color, boolean isLivingVoiceRate) {
		
		//Sound情報取得
		String soundName = this.soundManager.getSoundNameWithModel(sound, texture, color);
		if (soundName == null || soundName.isEmpty()) {
			return null;
		}
		
		// LivingVoiceのRateを判定する
		if (isLivingVoiceRate) {
			if ((sound.getId() & 0xff0) == 0x500) {
				//soundからサウンドパック名からRateを取得する
				String soundPackName = soundName.substring(0 , soundName.lastIndexOf("."));
				float ratio = this.soundManager.getLivingVoiceRatio(soundPackName);
				// Rate判断
				if (Math.random() > ratio) {
					return null;
				}
			}	
		}
		return new SoundEvent(new ResourceLocation(LMLibrary.MODID, soundName));
	}
	
	@SideOnly(Side.CLIENT)
	public void openGuiTextureSelect(GuiScreen owner, IGuiTextureSelect target, String screenTitle) {
		Minecraft.getMinecraft().displayGuiScreen(new LMGuiTextureSelect(owner, target, screenTitle));
	}
	
	
	/**
	 * 汎用NBTパケット送信処理登録
	 * @param networkKey
	 * @param consumer
	 * @return
	 */
	public boolean registerNetwork(String networkKey, Consumer<NBTTagCompound> consumer) {
		return LMLibNetworkHandler.register(networkKey, consumer);
	}
	
	/**
	 * サーバーへパケットを送信する
	 */
	public void sendPacketToServer(String networkKey, NBTTagCompound nbt) {
		LMLibNetworkHandler.sendPacketToServer(networkKey, nbt);
	}
	
	/**
	 * すべてのプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 */
	public void sendPacketToClientAll(String networkKey, NBTTagCompound nbt) {
		LMLibNetworkHandler.sendPacketToClientAll(networkKey, nbt);
	}
	
	/**
	 * 指定したプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 * @param player
	 */
	public void sendPacketToClientPlayer(String networkKey, NBTTagCompound nbt, EntityPlayer player) {
		LMLibNetworkHandler.sendPacketToClientPlayer(networkKey, nbt, player);
	}
}

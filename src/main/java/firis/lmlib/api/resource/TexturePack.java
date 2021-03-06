package firis.lmlib.api.resource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import firis.lmlib.api.constant.EnumColor;

/**
 * テクスチャパック管理用クラス
 * 
 * テクスチャパック単位でパラメータを保持する
 * @author firis-games
 *
 * 旧形式のフォーマットから新形式のフォーマットへ変換する想定
 * 
 */
public class TexturePack {
	
	/**
	 * テクスチャパック名
	 */
	protected String texturePackName = "";
	
	/**
	 * マルチモデル名
	 */
	protected String multiModelName = "";
	
	/**
	 * 通常メイドさんテクスチャ
	 */
	public Map<EnumColor, String> textureLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 野生メイドさんテクスチャ
	 */
	public Map<EnumColor, String> textureWildLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 通常メイドさんのデフォルト発光テクスチャ
	 */
	public String textureDefaultLightLittleMaid = "";
	
	/**
	 * 野生メイドさんのデフォルト発光テクスチャ
	 */
	public String textureDefaultLightWildLittleMaid = "";
	
	/**
	 * 通常メイドさんの発光テクスチャ
	 */
	public Map<EnumColor, String> textureLightLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 野生メイドさんの発光テクスチャ
	 */
	public Map<EnumColor, String> textureLightWildLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * メイドさんのGUIテクスチャ
	 */
	public String textureGuiBackground = "";
	
	/**
	 * メイドさんのインナーアーマーテクスチャ
	 * 0-9
	 */
	public Map<Integer, String> textureInnerArmor = new HashMap<>();

	/**
	 * メイドさんのアウターアーマーテクスチャ
	 * 0-9
	 */
	public Map<Integer, String> textureOuterArmor = new HashMap<>();
	
	/**
	 * メイドさんの発光インナーアーマーテクスチャ
	 * 0-9
	 */
	public Map<Integer, String> textureLightInnerArmor = new HashMap<>();

	/**
	 * メイドさんの発光アウターアーマーテクスチャ
	 * 0-9
	 */
	public Map<Integer, String> textureLightOuterArmor = new HashMap<>();

	
	/**
	 * メイドさんのアナザーアーマー名
	 */
	private Set<String> textureAnotherArmor = new HashSet<>();
	
	/**
	 * メイドさんのアナザーインナーアーマーテクスチャ
	 * 0-9
	 */
	private Map<String, Map<Integer, String>> textureAnotherInnerArmor = new HashMap<>();
	
	/**
	 * メイドさんのアナザーアウターアーマーテクスチャ
	 * 0-9
	 */
	private Map<String, Map<Integer, String>> textureAnotherOuterArmor = new HashMap<>();
	
	/**
	 * メイドさんのアナザー発光インナーアーマーテクスチャ
	 * 0-9
	 */
	private Map<String, Map<Integer, String>> textureAnotherLightInnerArmor = new HashMap<>();

	/**
	 * メイドさんのアナザー発光アウターアーマーテクスチャ
	 * 0-9
	 */
	private Map<String, Map<Integer, String>> textureAnotherLightOuterArmor = new HashMap<>();
	

	
	/**
	 * 旧タイプテクスチャ名
	 */
	private final static String oldLittleMaidTexture[] = {
		"mob_littlemaid0.png", "mob_littlemaid1.png",
		"mob_littlemaid2.png", "mob_littlemaid3.png",
		"mob_littlemaid4.png", "mob_littlemaid5.png",
		"mob_littlemaid6.png", "mob_littlemaid7.png",
		"mob_littlemaid8.png", "mob_littlemaid9.png",
		"mob_littlemaida.png", "mob_littlemaidb.png",
		"mob_littlemaidc.png", "mob_littlemaidd.png",
		"mob_littlemaide.png", "mob_littlemaidf.png"
	};
	
	/**
	 * 旧タイプ野生メイドテクスチャ名
	 * 現行は茶色の野生メイド
	 */
	private final static String oldWildLittleMaidTexture[] = {
			"mob_littlemaidw.png"
	};

	/**
	 * 旧タイプアーマーテクスチャ名
	 */
	private final static String oldArmorTexture[] = {
		"mob_littlemaid_a00.png", //インナー防具テクスチャ
		"mob_littlemaid_a01.png"  //アウター防具テクスチャ
	};
	
	/**
	 * 種類別アーマーのデフォルト名
	 */
	private final static String armorTypeDefault = "default";
	
	/**
	 * 初期化
	 * テクスチャパック配下のテクスチャ名をもとに必要な情報を作成する
	 */
	public TexturePack(String texturePackName, List<String> texturesPath) {
		
		//テクスチャパック初期化
		this.initTexturesPath(texturesPath);
		
		//テクスチャパック名とマルチモデル名を設定する
		int i = texturePackName.lastIndexOf("_");
		if (i > -1) {
			this.texturePackName = texturePackName.substring(0, i);
			this.multiModelName = texturePackName.substring(i + 1);
		} else {
			this.texturePackName = texturePackName;
			this.multiModelName = "";
		}
		
		//アナザーアーマーの判定
		//メインアーマーがない＋アナザーアーマーが一つの場合
		//アナザーアーマーをメインアーマーとして扱う
		if (this.textureInnerArmor.size() == 0
				&& this.textureOuterArmor.size() == 0
				&& this.textureLightInnerArmor.size() == 0
				&& this.textureLightOuterArmor.size() == 0
				&& this.textureAnotherArmor.size() == 1) {
			String armorType = this.textureAnotherArmor.iterator().next();
			//アーマー設定
			if (this.textureAnotherInnerArmor.containsKey(armorType)) {
				this.textureInnerArmor = this.textureAnotherInnerArmor.get(armorType);
			}
			if (this.textureAnotherOuterArmor.containsKey(armorType)) {
				this.textureOuterArmor = this.textureAnotherOuterArmor.get(armorType);
			}
			if (this.textureAnotherLightInnerArmor.containsKey(armorType)) {
				this.textureLightInnerArmor = this.textureAnotherLightInnerArmor.get(armorType);
			}
			if (this.textureAnotherLightOuterArmor.containsKey(armorType)) {
				this.textureLightOuterArmor = this.textureAnotherLightOuterArmor.get(armorType);
			}
			//アナザーアーマークリア
			this.textureAnotherArmor.clear();
			this.textureAnotherInnerArmor.clear();
			this.textureAnotherOuterArmor.clear();
			this.textureAnotherLightInnerArmor.clear();
			this.textureAnotherLightOuterArmor.clear();
			
		}
	}
	
	/**
	 * アナザーパック構築用コンストラクタ
	 */
	private TexturePack() {
	}
	
	/**
	 * テクスチャパスリストを初期化
	 */
	private void initTexturesPath(List<String> texturesPath) {
		
		//テクスチャパックの準備を行う
		for (String texturePath : texturesPath) {
			
			//テクスチャの種類を判断する
			int texId = getTextureId(texturePath);
			if (texId == -1) continue;
			
			//デフォルト系テクスチャIDを変換する
			texId = convertDefaultTextureId(texId);
			
			//テクスチャIDをもとに設定する
			
			//色毎のメイドさんテクスチャ
			//0～F
			if (isIntRange(texId, 0x00, 0x0F)) {
				EnumColor color = EnumColor.getColorFromInt(texId);
				this.textureLittleMaid.put(color, texturePath);
			}
			
			//通常メイドさんのデフォルト発光テクスチャ
			//0x13
			else if (isIntRange(texId, 0x13, 0x13)) {
				this.textureDefaultLightLittleMaid = texturePath;
			}
			
			//野生メイドさんのデフォルト発光テクスチャ
			//0x14
			else if (isIntRange(texId, 0x14, 0x14)) {
				this.textureDefaultLightWildLittleMaid = texturePath;
			}
			
			//GUI用テクスチャ
			//0x20
			else if (isIntRange(texId, 0x20, 0x20)) {
				this.textureGuiBackground = texturePath;
			}
			
			//色毎の野生メイドさんテクスチャ
			//30～3f
			else if (isIntRange(texId, 0x30, 0x3F)) {
				EnumColor color = EnumColor.getColorFromInt(texId - 0x30);
				this.textureWildLittleMaid.put(color, texturePath);
			}

			//インナーアーマーテクスチャ
			//0x40～49
			else if (isIntRange(texId, 0x40, 0x49)) {
				//アーマーモデル名取得
				String armorType = getArmorTypeName(texturePath);
				if (armorTypeDefault.equals(armorType)) {
					this.textureInnerArmor.put(texId - 0x40, texturePath);
				} else {
					//アナザータイプ
					this.textureAnotherArmor.add(armorType);
					if (!this.textureAnotherInnerArmor.containsKey(armorType)) {
						this.textureAnotherInnerArmor.put(armorType, new HashMap<>());
					}
					this.textureAnotherInnerArmor.get(armorType).put(texId - 0x40, texturePath);
				}
			}
			
			//アウターアーマーテクスチャ
			//0x50～59
			else if (isIntRange(texId, 0x50, 0x59)) {
				//アーマーモデル名取得
				String armorType = getArmorTypeName(texturePath);
				if (armorTypeDefault.equals(armorType)) {
					this.textureOuterArmor.put(texId - 0x50, texturePath);
				} else {
					//アナザータイプ
					this.textureAnotherArmor.add(armorType);
					if (!this.textureAnotherOuterArmor.containsKey(armorType)) {
						this.textureAnotherOuterArmor.put(armorType, new HashMap<>());
					}
					this.textureAnotherOuterArmor.get(armorType).put(texId - 0x50, texturePath);
				}
			}
			
			//色毎のメイドさんの発光テクスチャ
			//60～6F
			else if (isIntRange(texId, 0x60, 0x6F)) {
				EnumColor color = EnumColor.getColorFromInt(texId - 0x60);
				this.textureLightLittleMaid.put(color, texturePath);
			}
			
			//色毎の野生メイドさんの発光テクスチャ
			//70～7F
			else if (isIntRange(texId, 0x70, 0x7F)) {
				EnumColor color = EnumColor.getColorFromInt(texId - 0x70);
				this.textureLightWildLittleMaid.put(color, texturePath);
			}
			
			//発光インナーアーマーテクスチャ
			//0x80～89
			else if (isIntRange(texId, 0x80, 0x89)) {
				//アーマーモデル名取得
				String armorType = getArmorTypeName(texturePath);
				if (armorTypeDefault.equals(armorType)) {
					this.textureLightInnerArmor.put(texId - 0x80, texturePath);
				} else {
					//アナザータイプ
					this.textureAnotherArmor.add(armorType);
					if (!this.textureAnotherLightInnerArmor.containsKey(armorType)) {
						this.textureAnotherLightInnerArmor.put(armorType, new HashMap<>());
					}
					this.textureAnotherLightInnerArmor.get(armorType).put(texId - 0x80, texturePath);
				}
			}
			
			//発光アウターアーマーテクスチャ
			//0x90～99
			else if (isIntRange(texId, 0x90, 0x99)) {
				this.textureLightOuterArmor.put(texId - 0x90, texturePath);
				//アーマーモデル名取得
				String armorType = getArmorTypeName(texturePath);
				if (armorTypeDefault.equals(armorType)) {
					this.textureLightOuterArmor.put(texId - 0x90, texturePath);
				} else {
					//アナザータイプ
					this.textureAnotherArmor.add(armorType);
					if (!this.textureAnotherLightOuterArmor.containsKey(armorType)) {
						this.textureAnotherLightOuterArmor.put(armorType, new HashMap<>());
					}
					this.textureAnotherLightOuterArmor.get(armorType).put(texId - 0x90, texturePath);
				}
			}
		}
	}
	
	/**
	 * テクスチャパスからテクスチャのIDを取得する
	 * 
	 * 旧形式テクスチャIDも考慮する
	 * @return
	 */
	public static int getTextureId(String texturePath) {
		
		Integer texId = -1;
		
		//旧メイドテクスチャ
		for (int i = 0; i < oldLittleMaidTexture.length; i++) {
			if (texturePath.endsWith(oldLittleMaidTexture[i])) {
				//ノーマルメイドテクスチャの並びは同じ
				texId = i;
				break;
			}
		}
		//旧野生メイドテクスチャ
		for (int i = 0; i < oldWildLittleMaidTexture.length; i++) {
			if (texturePath.endsWith(oldWildLittleMaidTexture[i])) {
				//野生メイドさんと同じIDとする
				texId = 0x10;
				break;
			}
		}
		//旧アーマーテクスチャ
		for (int i = 0; i < oldArmorTexture.length; i++) {
			if (texturePath.endsWith(oldArmorTexture[i])) {
				if (i == 0) {
					//インナー防具					
					texId = 0x11;
				} else {
					//アウター防具
					texId = 0x12;
				}
				break;
			}
		}
		
		//新しい場合は_以降を取得して16進数へ変換する
		if (texId == -1) {
			Pattern pattern = Pattern.compile("_([0-9a-f]+).png");
			Matcher match = pattern.matcher(texturePath);
			if (match.find()) {
				texId = Integer.decode("0x" + match.group(1));
			}
		}
		
		return texId;
	}
	
	/**
	 * デフォルト系テクスチャIDを変換する
	 * ハードコーディングで設定する
	 * @param textureId
	 * @return
	 */
	private static int convertDefaultTextureId(int textureId) {
		int convertTextureId = textureId;
		
		//テクスチャIDの変換
		switch (textureId) {
		
		//*_10.png		：野生のメイドさん用のテクスチャ（*_3c.pngと同等）
		case 0x10:
			convertTextureId = 0x3c;
			break;
		
		//アーマーのテクスチャ、1はサイズ+0.1（default_40.pngと同等）
		case 0x11:
			convertTextureId = 0x40;
			break;
		
		//アーマーのテクスチャ、2はサイズ+0.5（default_50.pngと同等）
		case 0x12:
			convertTextureId = 0x50;
			break;
	
		//周囲の明るさに左右されない半透明のテクスチャ（default_80.pngと同等）
		case 0x15:
			convertTextureId = 0x80;
			break;
			
		//周囲の明るさに左右されない半透明のテクスチャ（default_90.pngと同等）
		case 0x16:
			convertTextureId = 0x90;
			break;
			
		}
		return convertTextureId;
	}
	
	/**
	 * 数値が範囲内かのチェックを行う
	 * @param value
	 * @param from
	 * @param to
	 * @return
	 */
	private static boolean isIntRange(int value , int from, int to) {
		return from <= value && value <= to;
	}
	
	/**
	 * テクスチャパスからアーマーモデルのタイプを取得する
	 * @param texturePath
	 * @return
	 */
	private static String getArmorTypeName(String texturePath) {
		return texturePath.substring(texturePath.lastIndexOf("/") + 1, texturePath.lastIndexOf("_"));
	}

	/**
	 * テクスチャパック名を取得する
	 * @return
	 */
	public String getTexturePackName() {
		return this.texturePackName;
	}
	
	/**
	 * 対応するマルチモデル名を取得する
	 * @return
	 */
	public String getMultiModelName() {
		return this.multiModelName;
	}
	
	/**
	 * アナザーアーマー用テクスチャパックを取得する
	 * @return
	 */
	public List<TexturePack> getAnotherArmorTexturePack() {
		List<TexturePack> anotherpackList = new ArrayList<>();
		
		for (String armorType : this.textureAnotherArmor) {
			//初期化
			TexturePack anotherpack = new TexturePack();
			anotherpack.texturePackName = this.texturePackName + "_" + armorType;
			anotherpack.multiModelName = this.multiModelName;
			
			//アーマー設定
			if (this.textureAnotherInnerArmor.containsKey(armorType)) {
				anotherpack.textureInnerArmor = this.textureAnotherInnerArmor.get(armorType);
			}
			if (this.textureAnotherOuterArmor.containsKey(armorType)) {
				anotherpack.textureOuterArmor = this.textureAnotherOuterArmor.get(armorType);
			}
			if (this.textureAnotherLightInnerArmor.containsKey(armorType)) {
				anotherpack.textureLightInnerArmor = this.textureAnotherLightInnerArmor.get(armorType);
			}
			if (this.textureAnotherLightOuterArmor.containsKey(armorType)) {
				anotherpack.textureLightOuterArmor = this.textureAnotherLightOuterArmor.get(armorType);
			}
			anotherpackList.add(anotherpack);
		}
		
		//クリア
		this.textureAnotherArmor.clear();
		this.textureAnotherInnerArmor.clear();
		this.textureAnotherOuterArmor.clear();
		this.textureAnotherLightInnerArmor.clear();
		this.textureAnotherLightOuterArmor.clear();
		
		return anotherpackList;
		
	}
}

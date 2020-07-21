package firis.lmlib.common.helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import firis.lmlib.LMLibrary;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

/**
 * 独自設定系のファイルの読み書き処理
 * @author firis-games
 *
 */
public class ResourceFileHelper {

	//ベースディレクトリパス
	public static Path RESOURCE_DIR = Paths.get("LittleMaidResource");
	
	/**
	 * MinecraftのHomeパスを取得する
	 * @return
	 */
	public static Path getMinecraftHomePath() {
		//Minecraftのホームパスを取得
		File minecraftHome = (File) FMLInjectionData.data()[6];
		return Paths.get(minecraftHome.toURI());
	}
	
	/**
	 * ファイルへ書き出す
	 * @param name
	 * @param json
	 */
	public static boolean writeToFile(String fileName, String write) {
		
		boolean ret = false;
		
		try {
			//出力
			List<String> jsonList = new ArrayList<>();
			jsonList.add(write);
			
			//Path
			Path filePath = Paths.get(RESOURCE_DIR.toString(), fileName);
			
			//ディレクトリがない場合は作成する
			if (!Files.isDirectory(RESOURCE_DIR)) {
				Files.createDirectories(RESOURCE_DIR);
			}
			
			//ファイルの上書き
			Files.write(filePath, jsonList, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
			ret = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * ファイルを読み込む
	 * @param fileName
	 * @return
	 */
	public static String readFromFile(String fileName) {
		
		String text = "";
		try {
			
			//Path
			Path filePath = Paths.get(RESOURCE_DIR.toString(), fileName);
			
			//存在チェック
			if (Files.notExists(filePath)) {
				return text;
			}
			
			//テキストとして読込
			List<String> fileList = Files.readAllLines(filePath);
			text = String.join("", fileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * Jsonファイルを読み込む
	 */
	public static <T> T readFromJson(String fileName, Class<T> clazz) {
		
		T jsonObject = null;
		
		try {
			
			//jsonファイルの読み込み
			String json = readFromFile(fileName);
			if ("".equals(json)) return jsonObject;
			
			//Jsonへパース
			Gson gson = new Gson();
			jsonObject = (T) gson.fromJson(json, clazz);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * Jsonファイルを書き込む
	 * @return
	 */
	public static <T> boolean writeToJson(String fileName, T jsonObject) {
		
		return ResourceFileHelper.writeToFile(fileName, jsonToString(jsonObject));
		
	}
	
	/**
	 * オブジェクトをjson文字列へ変換する
	 * @param jsonObject
	 * @return
	 */
	public static <T> String jsonToString(T jsonObject) {
		return new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.create().toJson(jsonObject);
	}
	
	/**
	 * リソースフォルダの存在チェック
	 * フォルダが存在しない場合は作成する
	 * @return
	 */
	public static boolean isDirectory() {
		try {
			if (!Files.isDirectory(RESOURCE_DIR)) {
				Files.createDirectories(RESOURCE_DIR);
				LMLibrary.logger.info("Create Dir : " + RESOURCE_DIR.toString());
				return false;
			}
		} catch (IOException e) {
			LMLibrary.logger.error("isDirectory Exception : " + RESOURCE_DIR.toString());
		}
		return true;
	}
}

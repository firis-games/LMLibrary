package firis.lmlib.core;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import firis.lmlib.common.helper.ResourceFileHelper;

/**
 * LittleMaid用クラスローダー
 * @author firis-games
 *
 */
public class LMClassLoader extends URLClassLoader {
	
	private static LMClassLoader classloader = null;
	
	public static LMClassLoader instance() {
		if (classloader == null) {
			classloader = new LMClassLoader();
		}
		return classloader;
	}
	
	/**
	 * Class.forNameラッパーメソッド
	 * LittleMaid用クラスローダー含む状態でClass検索を行う
	 * @param paramString
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String paramString) throws ClassNotFoundException {
		try {
			return Class.forName(paramString, true, LMClassLoader.instance());
		} catch (Error e) {
			throw new ClassNotFoundException(paramString + ":classForName_Error:[" + e.toString() + "]");
		}
	}
	
	private LMLibTransformer lmTransformer = new LMLibTransformer();
	
	/**
	 * コンストラクタ
	 * @param paramArrayOfURL
	 */
	private LMClassLoader() {
		super(getClassLoaderURL(), LMClassLoader.class.getClassLoader());
	}
	
	/**
	 * クラスローダー参照先を設定
	 * @return
	 */
	private static URL[] getClassLoaderURL() {
		//リソースパス
		Path modsPath = Paths.get(ResourceFileHelper.getMinecraftHomePath().toString(), ResourceFileHelper.RESOURCE_DIR.toString());
		//Pathリストを追加
		ArrayList<URL> urlList = null;
		try {
			urlList = Files.walk(modsPath)
					.filter(p -> !Files.isDirectory(p))
					.map(p -> {
						try {
							return p.toUri().toURL();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						return null;
					})
					.collect(Collectors.toCollection(ArrayList::new));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urlList.toArray(new URL[urlList.size()]);
	}
	
	/**
	 * class検索と登録
	 */
	@Override
	protected Class<?> findClass(final String paramString) throws ClassNotFoundException {
		InputStream inputstream = this.getResourceAsStream(paramString.replace(".", "/") + ".class");
		if (inputstream == null) {
			throw new ClassNotFoundException(paramString + ":inputstream:" + paramString.replace(".", "/") + ".class");
		}
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(inputstream);
		} catch (Exception e) {
			throw new ClassNotFoundException(paramString + ":toByteArray[" + e.toString() + "]");
		}
		if (bytes == null) {
			throw new ClassNotFoundException(paramString + ":bytes");
		}
		//クラス変換
		byte[] transBytes = lmTransformer.transform(paramString, paramString, bytes);
		try {
			return defineClass(paramString, transBytes, 0, transBytes.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(paramString + ":defineClass_Exception:[" + e.toString() + "]");
		} catch (Error e) {
			throw new ClassNotFoundException(paramString + ":defineClass_Error:[" + e.toString() + "]");
		}
	}
	
}

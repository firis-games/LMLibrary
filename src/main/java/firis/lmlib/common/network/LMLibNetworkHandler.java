package firis.lmlib.common.network;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import firis.lmlib.LMLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * NBTパケットを送信する汎用通信処理
 * @author firis-games
 *
 */
public class LMLibNetworkHandler {
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(LMLibrary.MODID);
	
	/**
	 * 通信を受け取り後に実行される通信処理をまとめて管理する
	 */
	protected static TreeMap<String, BiConsumer<NBTTagCompound, MessageContext>> consumerNetwork = new TreeMap<>();
	
	/**
	 * ネットワークキー管理
	 */
	protected static ArrayList<String> consumerNetworkKey = null;
	
	/**
	 * ネットワーク処理を登録する
	 * @param networkId
	 * @param consumer
	 * @return
	 */
	public static boolean register(String networkKey, BiConsumer<NBTTagCompound, MessageContext> consumer)
	{
		if (consumerNetwork.containsKey(networkKey)) {
			return false;
		}
		//登録
		consumerNetwork.put(networkKey, consumer);
		return true;
	}
	
	/**
	 * ネットワークキーからネットワークIDを取得する
	 * @param key
	 * @return
	 */
	public static int getIdFromKey(String key) {
		if (consumerNetworkKey == null) {
			consumerNetworkKey = new ArrayList<>();
			consumerNetworkKey.addAll(consumerNetwork.keySet());
		}
		return consumerNetworkKey.indexOf(key);
	}
	
	/**
	 * ネットワークIDからネットワークキーを取得する
	 * @param id
	 * @return
	 */
	public static String getKeyFromId(int id) {
		if (consumerNetworkKey == null) {
			consumerNetworkKey = new ArrayList<>();
			consumerNetworkKey.addAll(consumerNetwork.keySet());
		}
		return consumerNetworkKey.get(id);
	}
	
	/**
	 * 処理用の関数取得
	 * @param key
	 * @return
	 */
	public static BiConsumer<NBTTagCompound, MessageContext> getConsumer(int id) {
		return consumerNetwork.get(getKeyFromId(id));
	}
	
	/**
	 * パケット初期化
	 */
	public static void preInit() {
		
		int idx = 1;
		
		//Server to Client
		network.registerMessage(PacketSendNBTTagCompound.class, MessageSendNBTTagCompound.class, idx++, Side.CLIENT);
		
		//Client to Server
		network.registerMessage(PacketSendNBTTagCompound.class, MessageSendNBTTagCompound.class, idx++, Side.SERVER);
		
	}
	
	/**
	 * サーバーへパケットを送信する
	 */
	public static void sendPacketToServer(String networkKey, NBTTagCompound nbt) {
		network.sendToServer(new MessageSendNBTTagCompound(getIdFromKey(networkKey), nbt));
	}
	
	/**
	 * すべてのプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 */
	public static void sendPacketToClientAll(String networkKey, NBTTagCompound nbt) {
		network.sendToAll(new MessageSendNBTTagCompound(getIdFromKey(networkKey), nbt));
	}
	
	/**
	 * 指定したプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 * @param player
	 */
	public static void sendPacketToClientPlayer(String networkKey, NBTTagCompound nbt, EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			network.sendTo(new MessageSendNBTTagCompound(getIdFromKey(networkKey), nbt), (EntityPlayerMP) player);
		}
	}

}

package firis.lmlib.common.network;

import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * NBT形式のパケット送信用クラス
 * @author firis-games
 *
 */
public class PacketSendNBTTagCompound implements IMessageHandler<MessageSendNBTTagCompound, IMessage> {
	
	@Override
	public IMessage onMessage(MessageSendNBTTagCompound message, MessageContext ctx) {
		
		Consumer<NBTTagCompound> consumer = LMLibNetworkHandler.getConsumer(message.type);
		
		//実行
		consumer.accept(message.nbt);
		
		return null;
	}

}

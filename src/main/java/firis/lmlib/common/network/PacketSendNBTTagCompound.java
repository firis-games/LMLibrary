package firis.lmlib.common.network;

import java.util.function.BiConsumer;

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
		
		BiConsumer<NBTTagCompound, MessageContext> consumer = LMLibNetworkHandler.getConsumer(message.type);
		
		//実行
		consumer.accept(message.nbt, ctx);
		
		return null;
	}

}

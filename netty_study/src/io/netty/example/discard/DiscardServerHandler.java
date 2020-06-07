package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter{

	//클라이언트에서 받은 새로운 데이터을 받을 때마다 받은 메시지를 갖고 호출
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	
		ByteBuf in = (ByteBuf) msg;
		try{
			while(in.isReadable()){
				System.out.println((char)in.readByte());
				System.out.flush();
			}
		} finally{
			
			ReferenceCountUtil.release(msg);

		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

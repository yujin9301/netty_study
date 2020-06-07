package io.netty.example.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

	private int port;
	
	public DiscardServer(int port){
		this.port = port;
	}
	
	public void run() throws Exception {
		
		//Boss -> worker
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // 들어오는 연결
		EventLoopGroup workerGroup = new NioEventLoopGroup(); //연결 트래픽 담당
		
		try{
			
			//서버 셋팅
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>(
					
					) {
	
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
	
							ch.pipeline().addLast(new DiscardServerHandler());
							
						}
			}).option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			//Connection 바인드
			ChannelFuture f = b.bind(port).sync();
			
			f.channel().closeFuture().sync();
			
		}finally{
			
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			
		}
		
	}

	public static void main(String[] args) throws Exception{
		
		int port;
		
		if(args.length > 0 )
			port = Integer.parseInt(args[0]);
		else 
			port = 8090;
		    
		new DiscardServer(port).run();
	}
}

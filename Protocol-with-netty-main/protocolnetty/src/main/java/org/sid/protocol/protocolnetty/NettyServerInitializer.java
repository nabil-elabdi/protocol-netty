package org.sid.protocol.protocolnetty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NettyServerInitializer  extends ChannelInitializer<SocketChannel>{

    @Autowired
    private GpsServerHandler gpsServerHandler;

    @Bean
    public ChannelInitializer<SocketChannel> initializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();

                // Add a length field-based frame decoder and encoder to handle framing
                pipeline.addLast(new LengthFieldBasedFrameDecoder(6047, 0, 6, 0, 6));
                pipeline.addLast(new LengthFieldPrepender(9));

                // Add your GPS server handler
                pipeline.addLast(gpsServerHandler);
            }
        };
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                // Add any other handlers you might need in the pipeline
                .addLast(new GpsServerHandler());
    }
}

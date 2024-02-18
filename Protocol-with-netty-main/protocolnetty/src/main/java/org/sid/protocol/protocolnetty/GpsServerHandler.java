package org.sid.protocol.protocolnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class GpsServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] byteArray = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(byteArray);
            String hexString = bytesToHexString(byteArray);
            System.out.println("Received Bytes: " + hexString);
            String dataString = new String(byteArray, StandardCharsets.UTF_8);
            System.out.println("Received Data: " + dataString);
            
            ByteBuf responseBuf = ctx.alloc().buffer();
            responseBuf.writeBoolean(true);
            ctx.writeAndFlush(responseBuf);
        }
    }
    private void writeBooleanResponse(ChannelHandlerContext ctx, boolean responseValue) {
        DataOutputStream dos = new DataOutputStream(new ByteBufOutputStream(ctx.alloc().buffer()));
        try {
            // Write a boolean value
            dos.writeBoolean(true);
            // Send the response
            ctx.writeAndFlush(dos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String bytesToHexString(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X ", b));
        }
        return hexStringBuilder.toString().trim();
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

package com.sobey.util;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class MyClient {

    private Session session;
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
    /**
     * 连接关闭调用的方法
     * @throws Exception
     */
    @OnClose
    public void onClose() throws Exception{
    }

    /**
     * 关闭链接方法
     * @param
     * @throws IOException
     */
    public void closeSocket() throws IOException{
        this.session.close();
    }

    /**
     * 发送消息方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }
    //启动客户端并建立链接
    public void start(String uri) throws IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            this.session = container.connectToServer(MyClient.class, URI.create(uri));
        } catch (Exception e) {
            closeSocket();
            e.printStackTrace();
        }
    }
}

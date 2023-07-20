package cn.itcast.websocket;

import cn.itcast.service.GetDataService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个WebSocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/uiwebSocket")
public class UiWebSocket {
    // 静态变量，用于记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象，若要实现服务端与单一客户端通信，可以使用它
    private static CopyOnWriteArraySet<UiWebSocket> webSocketSet = new CopyOnWriteArraySet<UiWebSocket>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 简历连接成功时调用
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this); //加入set中
        addOnlineCount();  //在线数加1
        System.out.println("有新连接加入！当前在线人数为：" + getOnlineCount());
        onMessage("",session);
    }

    //连接断开时调用方法
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  // 从set中删除
        subOnlineCount();
        System.out.println("有一连接关闭！ 当前在线人数为："+ getOnlineCount());
    }

    GetDataService getDataService = new GetDataService();

    // 收到客户端消息后调用的方法
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("来自客户端的消息："+ message);
        // 群发消息
        for (final UiWebSocket item : webSocketSet){
            try{
                while (true){
                    // item.sendMessage("从Redis中查询数据：" + getDataService.getAll());
                    item.sendMessage(getDataService.getData());
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }
    // 出错时调用
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    // 根据自己需要添加的方法
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        UiWebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        UiWebSocket.onlineCount--;
    }

}

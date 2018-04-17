package server.ws;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint")
public class WsServer {
	public static Set<Session> allSessions = new HashSet<Session>(); 
	
	static{
		sendRandomMessageToAll();
	}
	
	@OnOpen
	public void onOpen(Session session){
		allSessions.add(session);
	}
	
	@OnClose
	public void onClose(Session session){
		allSessions.remove(session);
	}
	
	@OnMessage
	public String onMessage(String message, Session session){
		return message;
	}

	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}
	
	private static void sendRandomMessageToAll(){
		new Thread(){
			@Override
			public void run(){
				while(true){
					for(Session s : allSessions){
						try {
							s.getBasicRemote().sendText("" + (new Random().nextInt()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}
}

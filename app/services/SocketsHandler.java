package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.Plane;
import play.libs.Json;
import play.mvc.WebSocket;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by erezy on 11/9/2014.
 */
public class SocketsHandler {
    private  Map<String, Plane> planes;
    private  List<WebSocket.Out<JsonNode>> clients = new CopyOnWriteArrayList<WebSocket.Out<JsonNode>>();


    public List<WebSocket.Out<JsonNode>> getClients() {
        return clients;
    }
    public  void add(WebSocket.Out<JsonNode> out){
        clients.add(out);
        System.out.println("add: clients size "+ clients.size());
    }

    public  void remove(WebSocket.Out<JsonNode> out){
        clients.remove(out);
        System.out.println("remove: clients size "+ clients.size());
    }


    private  void update(Plane plane) {
        for (WebSocket.Out<JsonNode> out : clients) {
            out.write(Json.toJson(plane));
        }
    }

    public void updatePlanes() {

        double random1,random2,longitude,latitude;
        Plane plane;
        int i;
        for (i = 0; i < planes.size(); i++) {
            plane = planes.get(PlaneGenerator.ID_NAME + i);
            random1 = Math.random() * 0.002 + 0.999;
            random2 = Math.random() * 0.002 + 0.999;
            longitude = Double.valueOf(plane.getLongitude());
            latitude = Double.valueOf(plane.getLatitude());
            plane.setLatitude(String.valueOf(latitude * random2));
            plane.setLongitude(String.valueOf(longitude * random1));
        }
        for (i = 0; i < planes.size(); i++) {
            update(planes.get(PlaneGenerator.ID_NAME+i));
        }
    }

    public void setPlanes(Map<String,Plane> planes) {
        if(planes == null){
            this.planes.clear();
        }else {
            this.planes = planes;
        }
    }
}

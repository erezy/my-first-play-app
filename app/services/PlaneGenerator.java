package services;

import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;

import models.Plane;
import play.libs.Akka;
import play.mvc.WebSocket;

import java.util.HashMap;
import java.util.Map;

import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;


/**
 * Created by erezy on 11/9/2014.
 */
public class PlaneGenerator {

    private static final int PLANES_NUM = 2000;
    private static final int INTERVAL_TIME = 500;
    public static final String ID_NAME = "plane:";
    private static Map<String, Plane> planesMap = new HashMap<String,Plane>();
    public static final String SHAPE = "../Apps/Sandcastle/images/whiteShapes.png";
    private static SocketsHandler socketsHandler = new SocketsHandler();
    private static PlaneGenerator generator;
    Cancellable cancellable;


    private PlaneGenerator(){
    }
    public  static PlaneGenerator getPlaneGenerator(){
        if (generator == null) {
            synchronized (PlaneGenerator.class) {
                if (generator == null) {
                    generator = new PlaneGenerator();
                }
            }
        }
        return generator;
    }

    public  void  start(WebSocket.Out<JsonNode> out){
        socketsHandler.add(out);
        if(planesMap.size()== 0) {
            synchronized (this) {
                if(planesMap.size()== 0) {
                    buildPlanesMap();
                    setTimer();
                }
            }
        }

    }

    private void setTimer() {
        System.out.println("set timeout1");

        cancellable = Akka.system().scheduler().schedule(Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                                            Duration.create(INTERVAL_TIME, TimeUnit.MILLISECONDS),     //Frequency 30 minutes
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    socketsHandler.updatePlanes();
                                                }
                                            },
                                            Akka.system().dispatcher()
                                    );

        System.out.println("set timeout2");
    }


    private  void buildPlanesMap() {
        Plane plane;
        double lon = -75;
        double lan = 40;
        double rand1, rand2;
        for (int i = 0; i < PLANES_NUM; i++) {
            rand1 = Math.random() * 0.02 + 0.99;
            rand2 = Math.random() * 0.02 + 0.99;
            plane = new Plane();
            plane.setId(ID_NAME + i);
            plane.setColorId(String.valueOf(i%4));
            plane.setLatitude(String.valueOf(lan * rand2));
            plane.setLongitude(String.valueOf(lon * rand1));
            plane.setShape(SHAPE);
            planesMap.put(plane.getId(), plane);

        }
        socketsHandler.setPlanes(planesMap);
    }

    public void close(WebSocket.Out<JsonNode> out) {
        System.out.println("Close Connection2");

        socketsHandler.remove(out);
        if(socketsHandler.getClients().size() == 0){
            System.out.println("End timer");
            cancellable.cancel();
            planesMap.clear();
            socketsHandler.setPlanes(null);
        }

    }
}

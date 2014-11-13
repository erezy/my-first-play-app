package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F;
import play.mvc.*;
import play.mvc.Http.*;
import services.PlaneGenerator;
import views.html.*;

public class Application extends Controller {


    public static WebSocket<JsonNode> socket() {
        return WebSocket.whenReady((in, out) -> {
            // For each event received on the socket,
            in.onMessage(System.out::println);

            // When the socket is closed.
            in.onClose(() -> PlaneGenerator.getPlaneGenerator().close(out));

            // Send a single 'Hello!' message
           // out.write("Hello!");
            PlaneGenerator.getPlaneGenerator().start(out);
        });
    }


    /* public static WebSocket<JsonNode> socket() {
         return WebSocket.withActor(MyWebSocketActor::props);
     }*/
    public static F.Promise<Result> index() {
        //return redirect(controllers.routes.Application.sayHello("Bob"));
        return F.Promise.promise(() -> intensiveComputation()).map((String i) -> ok(index.render("EREZ - Your new application is ready. " + i)));
       //return ok(index.render("EREZ - Your new application is ready. " + promiseOfInt.get(0)));
    }

    private static String intensiveComputation(){return "SHALOM";}
    @BodyParser.Of(BodyParser.Json.class)
    public static Result sayHello(String name) {
        RequestBody body = request().body();
        return ok("Got name: " + name + ", body: "+ body.asJson());
        //return ok(index.render(name + " - Your new application is ready."));
    }


}

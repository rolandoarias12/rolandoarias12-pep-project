package Controller;

import javax.management.RuntimeErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.DAO;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::handlePostMsg);
        app.delete("/messages/{id}", this::handleDeleteMsgById);
        app.get("/accounts/{id}/messages", this::handleGetAllMsgByUserId);
        app.get("/messages", this::handleGetAllMsg);
        app.get("/messages/{id}", this::handleGetMsgById);
        app.patch("/messages/{id}", this::handlePatchMsgById);
        app.post("/login", this::handleTryLogin);
        app.post("/register", this::handleTryRegister);

        return app;
    }

    private void handlePostMsg(Context ctx) {
        try{
            var msg = mapper.readValue(ctx.body(), Message.class);

            var userExists = DAO.createMsg(msg);
            if (!userExists){
                ctx.status(400);
                return;
            }

            ctx.result(
                mapper.writeValueAsString(msg)
            );

            ctx.status(200);
        }
        catch(Throwable e){
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteMsgById(Context ctx) {
        try{
            var id = Integer.parseInt(ctx.pathParam("id"));

            var msg = DAO.deleteMsg(id);
            if (msg == null){
                ctx.status(200);
                return;
            }

            ctx.result(mapper.writeValueAsString(msg));
            ctx.status(200);
        }
        catch(Throwable e){
            throw new RuntimeException(e);
        }
    }

    private void handleGetAllMsgByUserId(Context ctx){
        try{
            var id = Integer.parseInt(ctx.pathParam("id"));

            var allMsg = DAO.getAllMsgByUserId(id);
            
            ctx.result(mapper.writeValueAsString(allMsg));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetAllMsg(Context ctx){
        try{
            var allMsg = DAO.getAllMsg();
            
            ctx.result(mapper.writeValueAsString(allMsg));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetMsgById(Context ctx){
        try{
            var id = Integer.parseInt(ctx.pathParam("id"));

            var msg = DAO.getMsgById(id);
            if (msg == null){
                ctx.status(200);
                return;
            }
            
            ctx.result(mapper.writeValueAsString(msg));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePatchMsgById(Context ctx){
        try{
            var id = Integer.parseInt(ctx.pathParam("id"));

            var msg = mapper.readValue(ctx.body(), Message.class);
            msg.setMessage_id(id);
            
            var msgExists = DAO.updateMsg(msg);
            if (!msgExists){
                ctx.status(400);
                return;
            }
            
            ctx.result(mapper.writeValueAsString(msg));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTryLogin(Context ctx){
        try{
            var acc = mapper.readValue(ctx.body(), Account.class);
            
            var valid = DAO.tryLogIn(acc);
            if (!valid){
                ctx.status(401);
                return;
            }
            
            ctx.result(mapper.writeValueAsString(acc));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTryRegister(Context ctx){
        try{
            var acc = mapper.readValue(ctx.body(), Account.class);
            
            var accExists = DAO.tryRegister(acc);
            if (!accExists){
                ctx.status(400);
                return;
            }
            
            ctx.result(mapper.writeValueAsString(acc));
            ctx.status(200);
        } 
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
package DAO;

import java.util.List;

import org.eclipse.jetty.server.Authentication.User;

import Model.Account;
import Model.Message;
import Service.Service;

public class DAO {
    public static Message getMsgById(int id){
        try{
            return Service.getMsg(id);
        }
        catch (Throwable e){
            return new Message(0, e.getMessage(), 0);
        }
    }

    public static boolean createMsg(Message msg){
        try{
            if (!Service.userIdExists(msg.getPosted_by())){
                return false;
            }
            if (msg.getMessage_text().isBlank()){
                return false;
            }
            if (msg.getMessage_text().length() > 255){
                return false;
            }

            var id = Service.insertMsg(
                msg.getPosted_by(), 
                msg.getMessage_text(), 
                msg.getTime_posted_epoch()
            );
            msg.setMessage_id(id);
            return true;
        }
        catch (Throwable e){
            msg.message_text = e.getMessage();
            return true;
        }
    }

    public static Message deleteMsg(int id){
        try{
            var oldMsg = Service.getMsg(id);
            if (oldMsg == null){
                return null;
            }

            Service.deleteMsg(id);

            return oldMsg;
        }
        catch (Throwable e){
            return new Message(0, e.getMessage(), 0);
        }
    }

    public static List<Message> getAllMsgByUserId(int id){
        try{
            return Service.getAllMsgByUserId(id);
        }
        catch (Throwable e){
            return List.of(new Message(0, e.getMessage(), 0));
        }
    }

    public static List<Message> getAllMsg(){
        try{
            return Service.getAllMsg();
        }
        catch (Throwable e){
            return List.of(new Message(0, e.getMessage(), 0));
        }
    }

    public static boolean updateMsg(Message msg){
        try{
            var oldMsg = Service.getMsg(msg.getMessage_id());
            if (oldMsg == null){
                return false;
            }
            if (msg.getMessage_text().isBlank()){
                return false;
            }
            if (msg.getMessage_text().length() > 255){
                return false;
            }

            msg.setPosted_by(oldMsg.getPosted_by());
            msg.setTime_posted_epoch(oldMsg.getTime_posted_epoch());

            Service.updateMsg(msg.getMessage_id(), msg.getMessage_text());
            return true;
        }
        catch (Throwable e){
            msg.setMessage_text(e.getMessage());
            return true;
        }
    }

    public static boolean tryLogIn(Account acc){
        try{
            var id = Service.getIdByUsername(acc.getUsername());
            if (id == null){
                return false;
            }
            if (!Service.userHasPassword(acc.getUsername(), acc.getPassword())){
                return false;
            }
            acc.account_id = id;
            return true;
        }
        catch (Throwable e){
            acc.setUsername(e.getMessage());
            return true;
        }
    }

    public static boolean tryRegister(Account acc){
        try{
            var oldId = Service.getIdByUsername(acc.getUsername());
            if (oldId != null){
                return false;
            }

            if (acc.getUsername().isBlank()){
                return false;
            }
            if (acc.getPassword().length() < 4){
                return false;
            }

            var id = Service.insertUsernameAndPassword(
                acc.getUsername(), 
                acc.getPassword()
            );

            acc.account_id = id;
            return true;
        }
        catch (Throwable e){
            acc.setUsername(e.getMessage());
            return true;
        }
    }
}

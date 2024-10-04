package Service;

import static org.mockito.ArgumentMatchers.matches;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class Service {
    public static int insertMsg(int postedBy, String messageTxt, long timePostedEpoch) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement(
            "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);", 
            Statement.RETURN_GENERATED_KEYS
        );
        stm.setInt(1, postedBy);
        stm.setString(2, messageTxt);
        stm.setLong(3, timePostedEpoch);
        stm.executeUpdate();

        var rs = stm.getGeneratedKeys();
        rs.next();
        return rs.getInt("message_id");
    }

    public static Message getMsg(int id) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM message WHERE posted_by=?");
        stm.setInt(1, id);
        var rs = stm.executeQuery();

        while (rs.next()){
            return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getNString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
        return null;
    }

    public static void deleteMsg(int id) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("DELETE FROM message WHERE posted_by=?;");
        stm.setInt(1, id);
        stm.execute();
    }

    public static void updateMsg(int id, String newMsgText) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("UPDATE message SET message_text=? WHERE posted_by=?;");
        stm.setString(1, newMsgText);
        stm.setInt(2, id);
        stm.execute();
    }

    public static List<Message> getAllMsgByUserId(int id) throws SQLException{
        var result = new ArrayList<Message>();

        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM message WHERE posted_by=?;");
        stm.setInt(1, id);
        var rs = stm.executeQuery();

        while (rs.next()){
            result.add(
                new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getNString("message_text"),
                    rs.getLong("time_posted_epoch")
                )
            );
        }

        return result;
    }

    public static List<Message> getAllMsg() throws SQLException{
        var result = new ArrayList<Message>();

        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM message;");
        var rs = stm.executeQuery();

        while (rs.next()){
            result.add(
                new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getNString("message_text"),
                    rs.getLong("time_posted_epoch")
                )
            );
        }

        return result;
    }
    

    public static boolean userHasPassword(String username, String password) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM account WHERE username=? and password=?;");
        stm.setString(1, username);
        stm.setString(2, password);

        var rs = stm.executeQuery();

        while (rs.next()) {
            return true;
        }
        return false;
    }

    public static Integer getIdByUsername(String username) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM account WHERE username=?;");
        stm.setString(1, username);
        var rs = stm.executeQuery();

        while (rs.next()) {
            return rs.getInt("account_id");
        }
        return null;
    }

    public static Boolean userIdExists(int id) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement("SELECT * FROM account WHERE account_id=?;");
        stm.setInt(1, id);
        var rs = stm.executeQuery();

        while (rs.next()) {
            return true;
        }
        return false;
    }

    public static int insertUsernameAndPassword(String username, String password) throws SQLException{
        var cnn = ConnectionUtil.getConnection();

        var stm = cnn.prepareStatement(
            "INSERT INTO account(username, password) VALUES (?, ?);",
            Statement.RETURN_GENERATED_KEYS
        );
        stm.setString(1, username);
        stm.setString(2, password);
        stm.executeUpdate();

        var rs = stm.getGeneratedKeys();
        rs.next();
        return rs.getInt("account_id");
    }
}

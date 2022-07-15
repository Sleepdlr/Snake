package snake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Viktória
 */
public class Database {
    /**
     * Tábla és adatbázis létrehozása, ha még nincs.
     * @return Connection
     */
    public static Connection ConnectDB(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection dat = DriverManager.getConnection("jdbc:sqlite:data/db.sqlite");
            
            String sql = "CREATE TABLE IF NOT EXISTS stat (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    name text NOT NULL,\n"
                + "    point integer NOT NULL\n"
                + ");";
            Statement stmt = dat.createStatement();
            stmt.execute(sql);
            
            return dat;
        } catch(Exception ex){
            return null;
        }
    }
}
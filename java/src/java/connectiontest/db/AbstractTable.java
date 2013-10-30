/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectiontest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 *
 * @author david
 */
abstract public class AbstractTable {
    
    private String name;
    private Connection connection;
    
    public AbstractTable(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    public String getName() {
        return name;
    }
    
    public abstract ResultContainer getColumns() throws SQLException, NamingException;
    
    public final ResultContainer getRows() throws SQLException, NamingException {
        return Database.getData("SELECT * FROM "+getName()+" LIMIT 100", null);
    }
    public final int getRowCount() throws SQLException, NamingException {
        Connection c = Database.getConnection();
        PreparedStatement stmnt = c.prepareStatement("SELECT count(*) FROM "+getName());
        ResultSet res = stmnt.executeQuery();
        int count = 0;
        
        if(res.next()) {
            count = res.getInt(1);
        }
        res.close();
        stmnt.close();
        c.close();
        
        return count;
    }
  

}
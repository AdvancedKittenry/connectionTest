/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectiontest.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 *
 * @author david
 */
public class MysqlTable extends AbstractTable {
    
    public MysqlTable(String name, Connection c) {
        super(name, c);
    }

    @Override
    public ResultContainer getColumns() throws NamingException, SQLException {
        return Database.getData("DESCRIBE "+getName(), null);
    }
    
}

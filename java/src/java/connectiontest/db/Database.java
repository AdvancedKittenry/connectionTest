/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectiontest.db;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 *
 * @author david
 */
public class Database {

    public static Connection getConnection() throws NamingException, SQLException {
        InitialContext cxt = new InitialContext();
        DataSource dataSource = (DataSource) cxt.lookup( "java:/comp/env/jdbc/tietokanta" );
        return dataSource.getConnection();
    }
    
    public static boolean hasConnection() {
        try {
            Connection c = getConnection();
            return c != null;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static List<AbstractTable> getTables() throws SQLException, NamingException {
        Connection c = Database.getConnection();
        String url = c.getMetaData().getURL();
        String query = null;
        ArrayList<AbstractTable> tables = new ArrayList<AbstractTable>();
        boolean isPostgres = false;
        
        if (url.startsWith("jdbc:postgresql")) {
            query = "select tablename from pg_tables where tableowner != 'postgres' order by tablename";
            isPostgres = true;
        }
        if (url.startsWith("jdbc:mysql")) {
            query = "SHOW TABLES";
        }
        
        if (query == null) {
            throw new UnsupportedOperationException("Database unsupported.");
        }
        
        PreparedStatement stmnt = c.prepareStatement(query);
        ResultSet tableData = stmnt.executeQuery();
        
        while(tableData.next()) {
            AbstractTable t;
            if (isPostgres) {
                t = new PostgreSqlTable(tableData.getString(1), c);
            } else {
                t = new MysqlTable(tableData.getString(1), c);
            }
            tables.add(t);
        }
        tableData.close();
        stmnt.close();
        c.close();
        
        return tables;
    }
    
    public static ResultContainer getData(String query, Object[] args) throws SQLException, NamingException {
        Connection c = Database.getConnection();        
        PreparedStatement stmnt = c.prepareStatement(query);
        int i = 1;
        if (args != null) {
            for (Object o : args) {
                stmnt.setObject(i, o);
                i++;
            }
        }

        ResultSet res = stmnt.executeQuery();
        ResultContainer data = new ResultContainer(res);

        res.close();
        stmnt.close();
        c.close();
        
        return data;
    }
}
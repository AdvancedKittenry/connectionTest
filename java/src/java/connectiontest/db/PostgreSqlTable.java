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
public class PostgreSqlTable extends AbstractTable {
       
    public PostgreSqlTable(String name, Connection c) {
        super(name, c);
    }

    @Override
    public ResultContainer getColumns() throws NamingException, SQLException {
        String[] args = new String[1];
        args[0] = getName();
        String q = "SELECT\n"+
          "    f.attname AS name,\n"+
          "    f.attnotnull AS notnull,\n"+
          "    pg_catalog.format_type(f.atttypid,f.atttypmod) AS type,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'p' THEN 't'\n"+
          "        ELSE 'f'\n"+
          "    END AS primarykey,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'u' THEN 't'\n"+
          "        ELSE 'f'\n"+
          "    END AS uniquekey,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'f' THEN g.relname\n"+
          "    END AS foreignkey,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'f' THEN p.confkey\n"+
          "    END AS foreignkey_fieldnum,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'f' THEN g.relname\n"+
          "    END AS foreignkey,\n"+
          "    CASE\n"+
          "        WHEN p.contype = 'f' THEN p.conkey\n"+
          "    END AS foreignkey_connnum,\n"+
          "    CASE\n"+
          "        WHEN f.atthasdef = 't' THEN d.adsrc\n"+
          "    END AS default\n"+
          "FROM pg_attribute f\n"+
          "    JOIN pg_class c ON c.oid = f.attrelid\n"+
          "    JOIN pg_type t ON t.oid = f.atttypid\n"+
          "    LEFT JOIN pg_attrdef d ON d.adrelid = c.oid AND d.adnum = f.attnum\n"+
          "    LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n"+
          "    LEFT JOIN pg_constraint p ON p.conrelid = c.oid AND f.attnum = ANY (p.conkey)\n"+
          "    LEFT JOIN pg_class AS g ON p.confrelid = g.oid\n"+
          "WHERE c.relkind = 'r'::char\n"+
          "    AND n.nspname = 'public'\n"+
          "    AND c.relname = ?  -- Replace with Schema name\n"+
          "    AND f.attnum > 0 ORDER BY f.attnum;";
        return Database.getData(q, args);
    }
    
}

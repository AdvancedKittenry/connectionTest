/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectiontest.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class ResultContainer {
    
    List<String> columns;
    List<List<String>> rows;
    
    public ResultContainer(ResultSet res) throws SQLException {
        ResultSetMetaData metaData = res.getMetaData();
        int colCount = metaData.getColumnCount();
        columns = new ArrayList<String>(colCount);
        rows = new ArrayList<List<String>>();
        
        for(int i = 0; i < colCount; i++) {
            columns.add(i,metaData.getColumnLabel(i+1));
        }
        
        while(res.next()) {
            List<String> row = new ArrayList<String>();
            for (String name : columns) {
                row.add(res.getString(name));
            }
            rows.add(row);
        }
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }
    
    public int getSize() {
        return rows.size();
    }
    
}

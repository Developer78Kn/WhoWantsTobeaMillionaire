/* @author Zsolt Ollé */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class DBModel implements IModel {
    private Connection conn;

    private PreparedStatement getAllKerdesPstmt;
    
    public DBModel(Connection conn) throws SQLException {
        this.conn = conn;
        getAllKerdesPstmt = conn.prepareStatement("SELECT * FROM kerdes");
    }
    
    
    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public Vector<Kerdes> getAllKerdes() throws SQLException {
        Vector<Kerdes> kerdesek = new Vector<>();
        
        ResultSet rs = getAllKerdesPstmt.executeQuery();
        while (rs.next()) {
            kerdesek.add(new Kerdes(
                    rs.getString("kerdes"),
                    rs.getString("valasz0"), 
                    rs.getString("valasz1"), 
                    rs.getString("valasz2"), 
                    rs.getString("valasz3"),                     
                    rs.getInt("helyesvalasz"))
            );
            
        }        
        return kerdesek;
    }
    
}

/* @author Zsolt Ollé */
package model;

import java.sql.SQLException;
import java.util.Vector;


public interface IModel {
    void close() throws SQLException;
    
    Vector<Kerdes> getAllKerdes() throws SQLException;
    
    
}

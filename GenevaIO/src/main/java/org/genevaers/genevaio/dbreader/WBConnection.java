package org.genevaers.genevaio.dbreader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * A DatabaseConnection implementation that uses the exising 
 * workbench database connection
 */
public class WBConnection extends DatabaseConnection{

    private Connection sqlConnection;

    public WBConnection() {
    }

    @Override
    public void connect() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connect'");
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isConnected'");
    }

    //@Override
    public List<Integer> getExistingFolderIds(String folderIds) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExistingFolderIds'");
    }

    //@Override
    public List<Integer> getViewIdsFromFolderIds(String folderIds) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getViewIdsFromFolderIds'");
    }

    @Override
    public ResultSet getResults(PreparedStatement ps) throws SQLException {
        return ps.executeQuery();
   }

    public void setSQLConnection(Connection c) {
        sqlConnection = c;
    }

    @Override
    public Connection getConnection() {
        return sqlConnection;
    }

    @Override
    public void closeStatement(PreparedStatement ps) throws SQLException {
        ps.close();
    }

    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
            return sqlConnection.prepareStatement(query);
    }
    
}

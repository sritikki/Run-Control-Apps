package org.genevaers.runcontrolgenerator.repositorybuilders;

import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.genevaio.dbreader.DatabaseConnection.DbType;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.genevaio.dbreader.WBConnection;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

public class WBConnectionBuilder implements RepositoryBuilder{

    private WBConnection databaseConnection;

	public WBConnectionBuilder() {
		databaseConnection = new WBConnection();
    }

    @Override
    public Status run() {
		Status retval = Status.OK;
		DatabaseConnectionParams conParams = new DatabaseConnectionParams();
		conParams.setDatabase(RunControlConfigration.getParm(RunControlConfigration.DB_DATABASE));
		conParams.setDbType(DbType.POSTGRES);
		conParams.setEnvironmentID(RunControlConfigration.getParm(RunControlConfigration.ENVIRONMENT_ID));
		conParams.setPort(RunControlConfigration.getParm(RunControlConfigration.DB_PORT));
		conParams.setServer(RunControlConfigration.getParm(RunControlConfigration.DB_SERVER));
		conParams.setSchema(RunControlConfigration.getParm(RunControlConfigration.DB_SCHEMA));
		conParams.setUsername(System.getenv("PG_USERID"));
		conParams.setPassword(System.getenv("PG_PASSWORD"));
		DBReader dbr = new DBReader();
		dbr.addFromDatabaseConnection(databaseConnection, conParams);
		return retval;
    }
    
}

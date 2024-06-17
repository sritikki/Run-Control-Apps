package org.genevaers.runcontrolgenerator.repositorybuilders;

import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.genevaio.dbreader.DatabaseConnection.DbType;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

public class DB2Builder implements RepositoryBuilder{

    public DB2Builder() {
    }

    @Override
    public Status run() {
		Status retval = Status.OK;
		DatabaseConnectionParams conParams = new DatabaseConnectionParams();
		conParams.setDatabase(RunControlConfigration.getParm(RunControlConfigration.DB_DATABASE));
		conParams.setDbType(DbType.DB2);
		conParams.setEnvironmentID(RunControlConfigration.getParm(RunControlConfigration.ENVIRONMENT_ID));
		conParams.setPort(RunControlConfigration.getParm(RunControlConfigration.DB_PORT));
		conParams.setServer(RunControlConfigration.getParm(RunControlConfigration.DB_SERVER));
		conParams.setFolderIds(RunControlConfigration.getParm(RunControlConfigration.DBFLDRS));
		conParams.setViewIds(RunControlConfigration.getParm(RunControlConfigration.DBVIEWS));
		conParams.setSchema(RunControlConfigration.getParm(RunControlConfigration.DB_SCHEMA));
		conParams.setUsername(System.getenv("TSO_USERID"));
		conParams.setPassword(System.getenv("TSO_PASSWORD"));
		DBReader dbr = new DBReader();
		dbr.addViewsToRepository(conParams);
		return dbr.hasErrors() ? Status.ERROR : Status.OK;
    }
    
}

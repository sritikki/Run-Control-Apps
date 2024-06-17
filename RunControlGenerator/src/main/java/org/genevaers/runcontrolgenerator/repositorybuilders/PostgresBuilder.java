package org.genevaers.runcontrolgenerator.repositorybuilders;

import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.genevaio.dbreader.DatabaseConnection.DbType;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

public class PostgresBuilder implements RepositoryBuilder{

    public PostgresBuilder() {
    }

    @Override
    public Status run() {
		DatabaseConnectionParams conParams = new DatabaseConnectionParams();
		conParams.setDatabase(RunControlConfigration.getParm(RunControlConfigration.DB_DATABASE));
		conParams.setDbType(DbType.POSTGRES);
		conParams.setEnvironmentID(RunControlConfigration.getParm(RunControlConfigration.ENVIRONMENT_ID));
		conParams.setPort(RunControlConfigration.getParm(RunControlConfigration.DB_PORT));
		conParams.setServer(RunControlConfigration.getParm(RunControlConfigration.DB_SERVER));
		conParams.setFolderIds(RunControlConfigration.getParm(RunControlConfigration.DBFLDRS));
		conParams.setViewIds(RunControlConfigration.getParm(RunControlConfigration.DBVIEWS));
		conParams.setSchema(RunControlConfigration.getParm(RunControlConfigration.DB_SCHEMA));
		conParams.setUsername(System.getenv("PG_USERID"));
		conParams.setPassword(System.getenv("PG_PASSWORD"));
		DBReader dbr = new DBReader();
		dbr.addViewsToRepository(conParams);
		return dbr.hasErrors() ? Status.ERROR : Status.OK;
    }
    
}

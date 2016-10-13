package com.dci.intellij.dbn.database.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.dci.intellij.dbn.database.DatabaseInterfaceProvider;
import com.dci.intellij.dbn.database.common.DatabaseMetadataInterfaceImpl;

public class OracleMetadataInterface extends DatabaseMetadataInterfaceImpl {

    public OracleMetadataInterface(DatabaseInterfaceProvider provider) {
        super("oracle_metadata_interface.xml", provider);
    }

    @Override
    public ResultSet loadDatabaseTriggerSourceCode(String ownerName, String triggerName, Connection connection) throws SQLException {
        return loadObjectSourceCode(ownerName, triggerName, "TRIGGER", connection);
    }

    public ResultSet loadDatasetTriggerSourceCode(String tableOwner, String tableName, String ownerName, String triggerName, Connection connection) throws SQLException {
        return loadObjectSourceCode(ownerName, triggerName, "TRIGGER", connection);
    }

    public String createDateString(Date date) {
        String dateString = META_DATE_FORMAT.get().format(date);
        return "to_date('" + dateString + "', 'yyyy-mm-dd HH24:MI:SS')";
    }


}

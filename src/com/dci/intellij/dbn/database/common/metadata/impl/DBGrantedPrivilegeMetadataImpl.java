package com.dci.intellij.dbn.database.common.metadata.impl;

import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dci.intellij.dbn.database.common.metadata.def.DBGrantedPrivilegeMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGrantedPrivilegeMetadataImpl extends DBObjectMetadataBase implements DBGrantedPrivilegeMetadata {
    public DBGrantedPrivilegeMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getGrantedPrivilegeName() throws SQLException {
        return resultSet.getString("GRANTED_PRIVILEGE_NAME");
    }

    @Override
    public String getUserName() throws SQLException {
        return resultSet.getString("USER_NAME");
    }

    @Override
    public String getRoleName() throws SQLException {
        return resultSet.getString("ROLE_NAME");
    }

    @Override
    public boolean isAdminOption() throws SQLException {
        return resultSet.getString("IS_ADMIN_OPTION").equals("Y");
    }
}

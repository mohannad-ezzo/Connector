/*
 *  Copyright (c) 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       SAP SE - add private properties to contract definition
 *
 */

package org.eclipse.edc.connector.store.sql.contractdefinition.schema;

import org.eclipse.edc.connector.store.sql.contractdefinition.schema.postgres.ContractDefinitionMapping;
import org.eclipse.edc.spi.query.QuerySpec;
import org.eclipse.edc.sql.translation.SqlQueryStatement;

import static java.lang.String.format;

public class BaseSqlDialectStatements implements ContractDefinitionStatements {

    @Override
    public String getDeleteByIdTemplate() {
        return executeStatement().delete(getContractDefinitionTable(), getIdColumn());
    }

    @Override
    public String getDeletePropertyByIdTemplate() {
        return executeStatement().delete(getContractDefinitionPropertyTable(), getPropertyContractDefinitionIdFkColumn());
    }

    @Override
    public String getFindByTemplate() {
        return format("SELECT * FROM %s WHERE %s = ?", getContractDefinitionTable(), getIdColumn());
    }

    @Override
    public String getInsertTemplate() {
        return executeStatement()
                .column(getIdColumn())
                .column(getAccessPolicyIdColumn())
                .column(getContractPolicyIdColumn())
                .jsonColumn(getAssetsSelectorColumn())
                .column(getCreatedAtColumn())
                .insertInto(getContractDefinitionTable());
    }

    @Override
    public String getInsertPropertyTemplate() {
        return executeStatement()
                .column(getPropertyContractDefinitionIdFkColumn())
                .column(getContractDefinitionPropertyNameColumn())
                .column(getContractDefinitionPropertyValueColumn())
                .column(getContractDefinitionPropertyTypeColumn())
                .insertInto(getContractDefinitionPropertyTable());
    }

    @Override
    public String getCountTemplate() {
        return format("SELECT COUNT (%s) FROM %s WHERE %s = ?",
                getIdColumn(),
                getContractDefinitionTable(),
                getIdColumn());
    }

    @Override
    public String getFindPropertyByIdTemplate() {
        return format("SELECT * FROM %s WHERE %s = ?",
                getContractDefinitionPropertyTable(),
                getPropertyContractDefinitionIdFkColumn());
    }

    @Override
    public String getUpdateTemplate() {
        return executeStatement()
                .column(getIdColumn())
                .column(getAccessPolicyIdColumn())
                .column(getContractPolicyIdColumn())
                .jsonColumn(getAssetsSelectorColumn())
                .column(getCreatedAtColumn())
                .update(getContractDefinitionTable(), getIdColumn());

    }

    @Override
    public SqlQueryStatement createQuery(QuerySpec querySpec) {
        var select = format("SELECT * FROM %s", getContractDefinitionTable());
        return new SqlQueryStatement(select, querySpec, new ContractDefinitionMapping(this));
    }

    protected String getSelectStatement() {
        return "SELECT * FROM " + getContractDefinitionTable();
    }
}

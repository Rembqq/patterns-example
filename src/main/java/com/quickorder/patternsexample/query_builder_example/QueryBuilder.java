package com.quickorder.patternsexample.query_builder_example;

//
//          Query Builder Example #1
//          No external libraries used

public class QueryBuilder {
    private StringBuilder query = new StringBuilder();
    private boolean hasWhere = false;

    public QueryBuilder select(String... columns) {
        query.append("SELECT ");
        query.append(String.join(", ", columns));
        return this;
    }

    public QueryBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    public QueryBuilder where(String condition) {
        if (!hasWhere) {
            query.append(" WHERE ");
            hasWhere = true;
        } else {
            query.append(" AND ");
        }
        query.append(condition);
        return this;
    }

    public QueryBuilder orderBy(String column, boolean ascending) {
        query.append(" ORDER BY ").append(column);
        query.append(ascending ? " ASC" : " DESC");
        return this;
    }

    public String build() {
        return query.toString();
    }
}

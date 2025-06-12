package com.quickorder.patternsexample.query_builder_example;

public class Main {
    public static void main(String[] args) {

        //
        //          Query Builder Usage
        //


        QueryBuilder qb = new QueryBuilder();
        String sql = qb.select("id", "name")
                .from("users")
                .where("age > 18")
                .where("email IS NOT NULL")
                .orderBy("name", true)
                .build();
    }
}

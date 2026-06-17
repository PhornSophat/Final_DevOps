package com.example.demo;

import org.hibernate.dialect.H2Dialect;

public class H2TestDialect extends H2Dialect {
    public boolean supportsLikeEscapeCharacter() {
        return false;
    }
}
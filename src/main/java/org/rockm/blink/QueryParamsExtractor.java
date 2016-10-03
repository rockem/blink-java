package org.rockm.blink;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParamsExtractor {

    private static final String EXP_SEPARATOR = "&";
    private final String query;

    public QueryParamsExtractor(String queryStr) {
        this.query = queryStr;
    }

    public Map<String, String> toMap() {
        return Arrays.stream(allQueryParams()).collect(Collectors.toMap(
                p -> p.split("=")[0], p -> p.split("=")[1]
        ));
    }

    private String[] allQueryParams() {
        if (query == null) {
            return new String[0];
        }
        return query.split(EXP_SEPARATOR);
    }

}

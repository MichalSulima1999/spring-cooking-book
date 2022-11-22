package com.example.recipebook.search;

public enum SearchOperation {

    CONTAINS, DOES_NOT_CONTAIN, EQUAL, NOT_EQUAL, BEGINS_WITH,
    DOES_NOT_BEGIN_WITH, ENDS_WITH, DOES_NOT_END_WITH,
    NULL, NOT_NULL, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN,
    LESS_THAN_EQUAL, IS_TRUE, IS_FALSE, ANY, ALL;
    public static final String[] SIMPLE_OPERATION_SET = {
            "cn", "nc", "eq", "ne", "bw", "bn", "ew",
            "en", "nu", "nn", "gt", "ge", "lt", "le", "ist", "isf"};

    public static SearchOperation getDataOption(final String
                                                        dataOption) {
        return switch (dataOption) {
            case "all" -> ALL;
            case "any" -> ANY;
            default -> null;
        };
    }

    public static SearchOperation getSimpleOperation(
            final String input) {
        return switch (input) {
            case "cn" -> CONTAINS;
            case "nc" -> DOES_NOT_CONTAIN;
            case "eq" -> EQUAL;
            case "ne" -> NOT_EQUAL;
            case "bw" -> BEGINS_WITH;
            case "bn" -> DOES_NOT_BEGIN_WITH;
            case "ew" -> ENDS_WITH;
            case "en" -> DOES_NOT_END_WITH;
            case "nu" -> NULL;
            case "nn" -> NOT_NULL;
            case "gt" -> GREATER_THAN;
            case "ge" -> GREATER_THAN_EQUAL;
            case "lt" -> LESS_THAN;
            case "le" -> LESS_THAN_EQUAL;
            case "ist" -> IS_TRUE;
            case "isf" -> IS_FALSE;
            default -> null;
        };
    }
}
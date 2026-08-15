package utils;

public final class Constants {

    public static final String MSSQL_IP = fromEnv("BP_MSSQL_IP");
    public static final String MSSQL_DATABASE = fromEnv("BP_MSSQL_DATABASE");
    public static final String MSSQL_USERNAME = fromEnv("BP_MSSQL_USERNAME");
    public static final String MSSQL_PASSWORD = fromEnv("BP_MSSQL_PASSWORD");

    private Constants() {}

    private static String fromEnv(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value;
    }
}

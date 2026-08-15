package querybuilder;

import java.util.List;

public record QueryBuildResult(String sql, List<String> validationErrors, String compileError) {

    public QueryBuildResult {
        validationErrors = List.copyOf(validationErrors);
    }

    public static QueryBuildResult compiled(String sql) {
        return new QueryBuildResult(sql, List.of(), null);
    }

    public static QueryBuildResult invalid(List<String> validationErrors) {
        return new QueryBuildResult(null, validationErrors, null);
    }

    public static QueryBuildResult failed(String compileError) {
        return new QueryBuildResult(null, List.of(), compileError);
    }

    public boolean isSuccess() {
        return sql != null;
    }
}

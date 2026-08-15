package querybuilder.compiler;

public record CompilationResult(String sql, String error) {

    public static CompilationResult success(String sql) {
        return new CompilationResult(sql, null);
    }

    public static CompilationResult failure(String error) {
        return new CompilationResult(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }
}

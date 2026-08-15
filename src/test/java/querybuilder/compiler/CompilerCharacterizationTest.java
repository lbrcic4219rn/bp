package querybuilder.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import querybuilder.validator.Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class CompilerCharacterizationTest {

    private static final Path GOLDEN = Path.of("src", "test", "resources", "golden-compiler.txt");

    private static final String QUERY = "Query";
    private static final String SELECT = "Select";
    private static final String COUNT = "Count";
    private static final String GROUP_BY = "GroupBy";
    private static final String HAVING = "Having";
    private static final String WHERE = "Where";

    private static final List<List<String>> CASES =
            List.of(
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"departments\").Select(\"manager_id\",\"location_id\")",
                            QUERY,
                            SELECT),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"departments\").Select(\"manager_id\").OrderBy(\"manager_id\")",
                            QUERY,
                            SELECT,
                            "OrderBy"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").OrderByDesc(\"last_name\")",
                            QUERY,
                            SELECT,
                            "OrderByDesc"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\")",
                            QUERY,
                            SELECT,
                            WHERE),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\").AndWhere(\"manager_id\",\"=\",\"100\")",
                            QUERY,
                            SELECT,
                            WHERE,
                            "AndWhere"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\").OrWhere(\"manager_id\",\"=\",\"100\")",
                            QUERY,
                            SELECT,
                            WHERE,
                            "OrWhere"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"min_salary\").WhereBetween(\"min_salary\",\"1000\",\"5000\")",
                            QUERY,
                            SELECT,
                            "WhereBetween"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").WhereIn(\"job_id\").ParameterList(\"IT_PROG\",\"SA_REP\")",
                            QUERY,
                            SELECT,
                            "WhereIn"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"jobs\").Select(\"job_title\").Join(\"jobs\").On(\"jobs.job_id\",\"=\",\"employees.job_id\")",
                            QUERY,
                            SELECT,
                            "Join"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\")",
                            QUERY,
                            SELECT,
                            COUNT,
                            GROUP_BY),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\")",
                            QUERY,
                            SELECT,
                            COUNT,
                            GROUP_BY,
                            HAVING),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\").AndHaving(\"br\",\"<\",\"9\")",
                            QUERY,
                            SELECT,
                            COUNT,
                            GROUP_BY,
                            HAVING,
                            "AndHaving"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\").OrHaving(\"br\",\"<\",\"9\")",
                            QUERY,
                            SELECT,
                            COUNT,
                            GROUP_BY,
                            HAVING,
                            "OrHaving"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"min_salary\").Avg(\"min_salary\",\"prosek\")",
                            QUERY,
                            SELECT,
                            "Avg"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"min_salary\").Max(\"min_salary\",\"maks\")",
                            QUERY,
                            SELECT,
                            "Max"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"min_salary\").Min(\"min_salary\",\"mini\")",
                            QUERY,
                            SELECT,
                            "Min"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").WhereStartsWith(\"last_name\",\"A\")",
                            QUERY,
                            SELECT,
                            "WhereStartsWith"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").WhereEndsWith(\"last_name\",\"z\")",
                            QUERY,
                            SELECT,
                            "WhereEndsWith"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"last_name\").WhereContains(\"last_name\",\"an\")",
                            QUERY,
                            SELECT,
                            "WhereContains"),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"jobs\").Select(\"job_title\").Join(\"employees\").On(\"jobs.job_id\",\"=\",\"employees.job_id\")",
                            QUERY,
                            SELECT,
                            "Join"),
                    List.of("a", " a = new Query(\"employees\").Select(\"last_name\")", SELECT),
                    List.of(
                            "a",
                            " a = new"
                                + " Query(\"employees\").Select(\"hire_date\").Date(\"hire_date\",\"17/06/2003\")",
                            QUERY,
                            SELECT,
                            "Date"));

    private String report() {
        StringBuilder sb = new StringBuilder();
        for (List<String> testCase : CASES) {
            String name = testCase.get(0);
            String content = testCase.get(1);
            List<String> functionNames = testCase.subList(2, testCase.size());

            Query query = new Query(content);
            query.setName(name);
            query.getFunctions().addAll(functionNames);

            sb.append("CASE ").append(functionNames).append('\n');
            Compiler compiler = new Compiler();
            try {
                sb.append("  RESULT: ").append(compiler.compileQuery(query)).append('\n');
            } catch (RuntimeException e) {
                sb.append("  THROWN: ").append(e.getClass().getSimpleName()).append('\n');
            }
        }
        return sb.toString();
    }

    @Test
    void compilerOutputMatchesGolden() throws IOException {
        String actual = report();
        if (!Files.exists(GOLDEN)) {
            Files.createDirectories(GOLDEN.getParent());
            Files.writeString(GOLDEN, actual);
            throw new IOException("golden baseline created at " + GOLDEN + "; re-run to verify");
        }
        assertEquals(Files.readString(GOLDEN), actual);
    }
}

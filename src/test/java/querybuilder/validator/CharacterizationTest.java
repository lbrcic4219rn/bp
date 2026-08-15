package querybuilder.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import querybuilder.validator.rule.IRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class CharacterizationTest {

    private static final Path GOLDEN = Path.of("src", "test", "resources", "golden.txt");

    private static final List<String> CORPUS =
            List.of(
                    "var a = new Query(\"departments\").Select(\"manager_id\", \"location_id\")",
                    "var a = new"
                        + " Query(\"departments\").Select(\"manager_id\").OrderBy(\"manager_id\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").OrderByDesc(\"last_name\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\").AndWhere(\"manager_id\",\"=\",\"100\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").Where(\"job_id\",\"=\",\"IT_PROG\").OrWhere(\"manager_id\",\"=\",\"100\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"min_salary\").WhereBetween(\"min_salary\",\"1000\",\"5000\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").WhereIn(\"job_id\").ParameterList(\"IT_PROG\",\"SA_REP\")",
                    "var a = new Query(\"employees\").Select(\"last_name\").WhereIn(\"job_id\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").ParameterList(\"IT_PROG\")",
                    "var a = new"
                        + " Query(\"jobs\").Select(\"job_title\").Join(\"jobs\").On(\"jobs.job_id\",\"=\",\"employees.job_id\")",
                    "var a = new Query(\"jobs\").Select(\"job_title\").Join(\"jobs\")",
                    "var a = new"
                        + " Query(\"jobs\").Select(\"job_title\").On(\"jobs.job_id\",\"=\",\"employees.job_id\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\")",
                    "var a = new"
                            + " Query(\"employees\").Select(\"job_id\").Having(\"br\",\">\",\"5\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\").AndHaving(\"br\",\"<\",\"9\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\").GroupBy(\"job_id\").Having(\"br\",\">\",\"5\").OrHaving(\"br\",\"<\",\"9\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"min_salary\").Avg(\"min_salary\",\"prosek\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"min_salary\").Max(\"min_salary\",\"maks\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"min_salary\").Min(\"min_salary\",\"mini\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"hire_date\").Date(\"hire_date\",\"17/06/2003\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").WhereStartsWith(\"last_name\",\"A\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").WhereEndsWith(\"last_name\",\"z\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"last_name\").WhereContains(\"last_name\",\"an\")",
                    "var a = new Query(\"jobs\").Select(\"job_id\") var b = new"
                            + " Query(\"employees\").Select(\"job_id\").WhereInQ(\"job_id\",\"a\")",
                    "var a = new Query(\"jobs\").Select(\"job_id\") var b = new"
                            + " Query(\"employees\").Select(\"job_id\").WhereEqQ(\"job_id\",\"a\")",
                    "var b = new"
                        + " Query(\"employees\").Select(\"job_id\").WhereInQ(\"job_id\",\"zzz\")",
                    "new Query(\"departments\").Select(\"manager_id\")",
                    "",
                    "var a = new Query(\"departments\"",
                    "var a = new Query().Select()",
                    "var a = new Query(\"departments\").Select()",
                    "var 1a = new Query(\"departments\").Select(\"manager_id\")",
                    "var a = new Query(\"departments\").Bogus(\"x\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"hire_date\").Date(\"hire_date\",\"2003-06-17\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\").Count(\"employees\",\"br\")",
                    "var a = new"
                        + " Query(\"employees\").Select(\"job_id\",\"last_name\").Count(\"employees\",\"br\").GroupBy(\"job_id\")");

    private String report() {
        StringBuilder sb = new StringBuilder();
        for (String source : CORPUS) {
            appendSourceReport(sb, source);
        }
        appendVarNameReport(sb);
        return sb.toString();
    }

    private void appendSourceReport(StringBuilder sb, String source) {
        Validator validator = new Validator();
        sb.append("SOURCE: ").append(source).append('\n');

        List<Query> queries = validator.divideQueries(source);
        for (Query q : queries) {
            sb.append("  QUERY name=")
                    .append(q.getName())
                    .append(" content=")
                    .append(q.getContent())
                    .append(" valid=")
                    .append(q.getValidFunctions())
                    .append('\n');
        }

        for (Query q : queries) {
            appendRuleRun(sb, validator, q);
        }

        sb.append("  FEEDBACK: ").append(validator.getFeedbackReport()).append('\n');
        sb.append("  VALIDAFTER: ");
        for (Query q : queries) {
            sb.append(q.getValidFunctions()).append(' ');
        }
        sb.append('\n');
    }

    private void appendRuleRun(StringBuilder sb, Validator validator, Query q) {
        for (IRule rule : validator.rules()) {
            try {
                rule.checkRule(q, validator.functionAndAttributes(), validator);
            } catch (RuntimeException e) {
                sb.append("  THROWN ")
                        .append(rule.getClass().getSimpleName())
                        .append(": ")
                        .append(e.getClass().getSimpleName())
                        .append('\n');
            }
        }
    }

    private void appendVarNameReport(StringBuilder sb) {
        for (int statementNo = 0; statementNo < 3; statementNo++) {
            for (String name : List.of("a", "1a", "a_b", "", "abc", "a1")) {
                sb.append("CHECKVAR ")
                        .append(statementNo)
                        .append(' ')
                        .append(name)
                        .append(" -> ")
                        .append(new Validator().checkVarName(name, statementNo))
                        .append('\n');
            }
        }
    }

    @Test
    void behaviourMatchesGolden() throws IOException {
        String actual = report();
        if (!Files.exists(GOLDEN)) {
            Files.createDirectories(GOLDEN.getParent());
            Files.writeString(GOLDEN, actual);
            throw new IOException("golden baseline created at " + GOLDEN + "; re-run to verify");
        }
        assertEquals(Files.readString(GOLDEN), actual);
    }
}

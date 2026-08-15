package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Compiler implements ICompiler {

    private static final String SELECT = "Select";
    private static final String COUNT = "Count";
    private static final Set<String> ALIAS_FUNCTIONS = Set.of("Avg", COUNT, "Min", "Max", "Date");
    private static final String JOIN_TABLE_MISMATCH =
            "In Join() must be the same table name as in Query()";
    private static final String NOTHING_TO_COMPILE = "No query to compile";

    @Override
    public CompilationResult compile(List<Query> queries) {
        Map<String, String> queryResults = new LinkedHashMap<>();
        String lastResult = null;

        for (Query query : queries) {
            String result = compileQuery(query, queryResults);
            if (result == null) {
                return CompilationResult.failure(JOIN_TABLE_MISMATCH);
            }
            queryResults.put(query.getName(), result);
            lastResult = result;
        }

        if (lastResult == null) {
            return CompilationResult.failure(NOTHING_TO_COMPILE);
        }
        return CompilationResult.success(lastResult);
    }

    String compileQuery(Query q) {
        return compileQuery(q, Map.of());
    }

    private String compileQuery(Query q, Map<String, String> queryResults) {
        List<Function> functions = new ArrayList<>();
        Map<String, String> aliasesValues = new LinkedHashMap<>();
        String tableNameFromQuery = "";
        String tableNameFromJoin = "";

        for (String name : q.getFunctions()) {
            Function function = createFunction(name);
            if (function == null) {
                continue;
            }
            functions.add(function);
            function.parseQuery(q);
            if (function instanceof QueryFunction queryFunction) {
                tableNameFromQuery = queryFunction.getTableName();
            }
            if (function instanceof JoinFunction joinFunction) {
                tableNameFromJoin = joinFunction.getTableName();
            }
            if (ALIAS_FUNCTIONS.contains(name)) {
                aliasesValues.put(function.getAlias(), function.getRes());
                function.setAlias("");
            }
        }

        sortFunctions(functions);

        StringBuilder result = new StringBuilder();
        if (!functions.getFirst().getName().equalsIgnoreCase(SELECT)) {
            result.append("SELECT * ");
        }
        if (!tableNameFromJoin.isEmpty()
                && !tableNameFromJoin.equalsIgnoreCase(tableNameFromQuery)) {
            return null;
        }

        for (Function f : functions) {
            appendFunction(result, f, aliasesValues, queryResults);
        }
        return result.toString();
    }

    private void appendFunction(
            StringBuilder result,
            Function f,
            Map<String, String> aliasesValues,
            Map<String, String> queryResults) {
        if (isAggregation(f) && !result.toString().equalsIgnoreCase("SELECT ")) {
            result.append(", ");
        }
        resolveAlias(f, aliasesValues);
        if (f instanceof DateFunction) {
            return;
        }
        result.append(f.getRes());
        if (f instanceof WhereInQFunction wif) {
            result.append("(")
                    .append(findQueryValueWithKey(wif.getSubQuery(), queryResults))
                    .append(") ");
        }
        if (f instanceof WhereEqQFunction wef) {
            result.append("(")
                    .append(findQueryValueWithKey(wef.getSubQuery(), queryResults))
                    .append(") ");
        }
    }

    private boolean isAggregation(Function f) {
        return f instanceof AvgFunction
                || f instanceof MaxFunction
                || f instanceof MinFunction
                || f instanceof CountFunction;
    }

    private void resolveAlias(Function f, Map<String, String> aliasesValues) {
        if (f.getAlias().isEmpty()) {
            return;
        }
        String alias = f.getAlias();
        String value = getValueFromAlias(alias, aliasesValues);
        if (value != null) {
            String[] strArray = value.split(" ");
            f.setRes(f.getRes().replaceAll(alias, strArray[f.getAliasPosition()]));
        }
    }

    private Function createFunction(String name) {
        return switch (name) {
            case "Query" -> new QueryFunction("Query", 3);
            case SELECT -> new SelectFunction(SELECT, 1);
            case "OrderBy" -> new OrderByFunction("OrderBy", 12);
            case "OrderByDesc" -> new OrderByDescFunction("OrderByDesc", 12);
            case "Where" -> new WhereFunction("Where", 5);
            case "OrWhere" -> new OrWhereFunction("OrWhere", 6);
            case "AndWhere" -> new AndWhereFunction("AndWhere", 6);
            case "WhereBetween" -> new WhereBetweenFunction("WhereBetween", 5);
            case "WhereIn" -> new WhereInFunction("WhereIn", 5);
            case "Join" -> new JoinFunction("Join", 4);
            case "WhereEndsWith" -> new WhereEndsWithFunction("WhereEndsWith", 5);
            case "WhereStartsWith" -> new WhereStartsWithFunction("WhereStartsWith", 5);
            case "WhereContains" -> new WhereContainsFunction("WhereContains", 5);
            case "Avg" -> new AvgFunction("Avg", 2);
            case COUNT -> new CountFunction(COUNT, 2);
            case "Min" -> new MinFunction("Min", 2);
            case "Max" -> new MaxFunction("Max", 2);
            case "GroupBy" -> new GroupByFunction("GroupBy", 9);
            case "Having" -> new HavingFunction("Having", 10);
            case "AndHaving" -> new AndHavingFunction("AndHaving", 11);
            case "OrHaving" -> new OrHavingFunction("OrHaving", 11);
            case "WhereInQ" -> new WhereInQFunction("WhereInQ", 5);
            case "WhereEqQ" -> new WhereEqQFunction("WhereEqQ", 5);
            case "Date" -> new DateFunction("Date", 4);
            default -> null;
        };
    }

    public void sortFunctions(List<Function> functions) {
        functions.sort(Comparator.comparingInt(Function::getPriority));
    }

    public String findQueryValueWithKey(String name, Map<String, String> queryResults) {
        for (Map.Entry<String, String> entry : queryResults.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String getValueFromAlias(String alias, Map<String, String> alVal) {
        for (Map.Entry<String, String> entry : alVal.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(alias)) {
                return entry.getValue();
            }
        }
        return null;
    }
}

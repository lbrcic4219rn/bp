package querybuilder.compiler;

import app.AppCore;

import observer.Notification;
import observer.enums.NotificationCode;

import querybuilder.validator.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Compiler implements ICompiler {

    private static final String SELECT = "Select";
    private static final String COUNT = "Count";
    private static final Set<String> ALIAS_FUNCTIONS = Set.of("Avg", COUNT, "Min", "Max", "Date");

    private final List<Function> functions = new ArrayList<>();
    private final Map<String, String> queryResults =
            new HashMap<>(); // key je naziv query-ja, value je parsirani string->sluzi za
    // subquery-je
    private final Map<String, String> aliasesValues = new HashMap<>();
    private String solution;

    @Override
    public void parseText(List<Query> queries) {
        for (Query q : queries) {
            String result = compileQuery(q);
            if (result == null) {
                AppCore.getInstance()
                        .notifySubscribers(
                                new Notification(
                                        NotificationCode.ERROR,
                                        "In Join() must be the same table name as in Query()"));
                return;
            }
            queryResults.put(q.getName(), result);
            this.solution = result;
            this.functions.clear();
            this.aliasesValues.clear();
        }
        AppCore.getInstance().readDataFromTable(this.solution);
    }

    String compileQuery(Query q) {
        String tableNameFromQuery = "";
        String tableNameFromJoin = "";

        for (String name : q.getFunctions()) {
            Function function = createFunction(name);
            if (function == null) {
                continue;
            }
            this.functions.add(function);
            function.parseQuery(q);
            if (function instanceof QueryFunction queryFunction) {
                tableNameFromQuery = queryFunction.getTableName();
            }
            if (function instanceof JoinFunction joinFunction) {
                tableNameFromJoin = joinFunction.getTableName();
            }
            if (ALIAS_FUNCTIONS.contains(name)) {
                this.aliasesValues.put(function.getAlias(), function.getRes());
                function.setAlias("");
            }
        }

        this.sortFunctions(functions);

        StringBuilder result = new StringBuilder();
        if (!functions.getFirst().getName().equalsIgnoreCase(SELECT)) {
            result.append("SELECT * ");
        }
        if (!tableNameFromJoin.isEmpty()
                && !tableNameFromJoin.equalsIgnoreCase(tableNameFromQuery)) {
            this.functions.clear();
            return null;
        }

        for (Function f : this.functions) {
            appendFunction(result, f);
        }
        return result.toString();
    }

    private void appendFunction(StringBuilder result, Function f) {
        if (isAggregation(f) && !result.toString().equalsIgnoreCase("SELECT ")) {
            result.append(", ");
        }
        resolveAlias(f);
        if (f instanceof DateFunction) {
            return;
        }
        result.append(f.getRes());
        if (f instanceof WhereInQFunction wif) {
            result.append("(")
                    .append(findQueryValueWithKey(wif.getSubQuery(), this.queryResults))
                    .append(") ");
        }
        if (f instanceof WhereEqQFunction wef) {
            result.append("(")
                    .append(findQueryValueWithKey(wef.getSubQuery(), this.queryResults))
                    .append(") ");
        }
    }

    private boolean isAggregation(Function f) {
        return f instanceof AvgFunction
                || f instanceof MaxFunction
                || f instanceof MinFunction
                || f instanceof CountFunction;
    }

    private void resolveAlias(Function f) {
        if (f.getAlias().isEmpty()) {
            return;
        }
        String alias = f.getAlias();
        String value = getValueFromAlias(alias, this.aliasesValues);
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
        functions.sort((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));
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

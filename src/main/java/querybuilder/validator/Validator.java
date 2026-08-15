package querybuilder.validator;

import lombok.Getter;

import querybuilder.validator.rule.AggregationAliasMustExistIfHaving;
import querybuilder.validator.rule.ArgNumberRule;
import querybuilder.validator.rule.DateFormatRule;
import querybuilder.validator.rule.IRule;
import querybuilder.validator.rule.JoinOnRule;
import querybuilder.validator.rule.NonAggregationElementsInGroupByRule;
import querybuilder.validator.rule.OnlyAggregationElementsInHavingRule;
import querybuilder.validator.rule.SubQueryVarMustExist;
import querybuilder.validator.rule.SyntaxRule;
import querybuilder.validator.rule.WhereInParameterListRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Validator implements IValidator {

    private static final Map<String, Integer> FUNCTION_ARITY =
            Map.ofEntries(
                    Map.entry("new Query", 1),
                    Map.entry("Select", -1),
                    Map.entry("OrderBy", -1),
                    Map.entry("OrderByDesc", -1),
                    Map.entry("Where", 3),
                    Map.entry("OrWhere", 3),
                    Map.entry("AndWhere", 3),
                    Map.entry("WhereBetween", 3),
                    Map.entry("WhereIn", 1),
                    Map.entry("ParameterList", -1),
                    Map.entry("Join", 1),
                    Map.entry("On", 3),
                    Map.entry("Date", 2),
                    Map.entry("WhereEndsWith", 2),
                    Map.entry("WhereStartsWith", 2),
                    Map.entry("WhereContains", 2),
                    Map.entry("Avg", 2),
                    Map.entry("Count", 2),
                    Map.entry("Min", 2),
                    Map.entry("Max", 2),
                    Map.entry("GroupBy", -1),
                    Map.entry("Having", 3),
                    Map.entry("AndHaving", 3),
                    Map.entry("OrHaving", 3),
                    Map.entry("WhereInQ", 2),
                    Map.entry("WhereEqQ", 2));

    private static final List<IRule> RULES =
            List.of(
                    new SyntaxRule(),
                    new ArgNumberRule(),
                    new DateFormatRule(),
                    new WhereInParameterListRule(),
                    new JoinOnRule(),
                    new AggregationAliasMustExistIfHaving(),
                    new OnlyAggregationElementsInHavingRule(),
                    new NonAggregationElementsInGroupByRule(),
                    new SubQueryVarMustExist());

    @Getter private final List<String> feedbackReport = new ArrayList<>();
    @Getter private List<Query> queries = new ArrayList<>();

    @Override
    public ValidationResult validate(String source) {
        clearFeedbackReport();
        this.queries = divideQueries(source);

        for (Query query : queries) {
            for (IRule rule : RULES) {
                rule.checkRule(query, FUNCTION_ARITY, this);
            }
            if (feedbackReport.isEmpty()) {
                collectFunctions(query);
            }
        }

        return new ValidationResult(queries, feedbackReport);
    }

    private void collectFunctions(Query query) {
        for (String validFunction : query.getValidFunctions()) {
            if (validFunction.equals("new Query")) {
                query.getFunctions().add("Query");
            } else if (!validFunction.equals("ParameterList")) {
                query.getFunctions().add(validFunction);
            }
        }
    }

    public List<Query> divideQueries(String str) {
        List<Query> dividedQueries = new ArrayList<>();
        String[] variables = str.split("\\bvar\\b");
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].equals(str)) {
                pushFeedback("No variables declared");
                return dividedQueries;
            }
            String tmp = variables[i].replaceAll("\\s", "");
            if (i == 0 && !tmp.isEmpty()) {
                pushFeedback("First query missing var");
                return dividedQueries;
            }
            if (i != 0 && checkVarName(tmp, i)) {
                addQuery(dividedQueries, variables[i], tmp);
            }
        }
        return dividedQueries;
    }

    private void addQuery(List<Query> dividedQueries, String variable, String tmp) {
        Query query = new Query(variable);
        query.setName(tmp.substring(0, tmp.indexOf('=')));
        if (dividedQueries.contains(query)) {
            pushFeedback("Variables can not have the same name");
            return;
        }
        dividedQueries.add(query);
    }

    List<IRule> rules() {
        return RULES;
    }

    Map<String, Integer> functionAndAttributes() {
        return FUNCTION_ARITY;
    }

    public void pushFeedback(String error) {
        this.feedbackReport.add(error);
    }

    public void clearFeedbackReport() {
        this.feedbackReport.clear();
    }

    public boolean checkVarName(String str, int statementNo) {
        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            if (current == '=') {
                if (i == 0) {
                    pushFeedback("Var name not specified for statement no: " + statementNo);
                    return false;
                }
                return true;
            }
            if (!isAllowedNameChar(current, i)) {
                pushFeedback(nameCharError(i, statementNo));
                return false;
            }
        }
        return true;
    }

    private boolean isAllowedNameChar(char current, int position) {
        if (position == 0) {
            return Character.isLetter(current);
        }
        return Character.isLetter(current) || Character.isDigit(current) || current == '_';
    }

    private String nameCharError(int position, int statementNo) {
        if (position == 0) {
            return "Variable name cant start with special characters or numbers statement no: "
                    + statementNo;
        }
        return "Variable name can not contain special characters other than '_' statement no: "
                + statementNo;
    }
}

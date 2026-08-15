package querybuilder.validator;

import app.AppCore;

import lombok.Getter;

import observer.Notification;
import observer.enums.NotificationCode;

import querybuilder.validator.rule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Validator implements IValidator {
    @Getter private final Stack<String> feedbackReport = new Stack<>();
    private final Map<String, Integer> functionAndAttributes = new HashMap<>();
    private final List<IRule> rules = new ArrayList<>();
    @Getter private List<Query> queries = new ArrayList<>();

    public Validator() {

        functionAndAttributes.put("new Query", 1);
        functionAndAttributes.put("Select", -1);
        functionAndAttributes.put("OrderBy", -1);
        functionAndAttributes.put("OrderByDesc", -1);
        functionAndAttributes.put("Where", 3);
        functionAndAttributes.put("OrWhere", 3);
        functionAndAttributes.put("AndWhere", 3);
        functionAndAttributes.put("WhereBetween", 3);
        functionAndAttributes.put("WhereIn", 1);
        functionAndAttributes.put("ParametarList", -1);
        functionAndAttributes.put("Join", 1);
        functionAndAttributes.put("On", 3);
        functionAndAttributes.put("Date", 2);
        functionAndAttributes.put("WhereEndsWith", 2);
        functionAndAttributes.put("WhereStartsWith", 2);
        functionAndAttributes.put("WhereContains", 2);
        functionAndAttributes.put("Avg", 2);
        functionAndAttributes.put("Count", 2);
        functionAndAttributes.put("Min", 2);
        functionAndAttributes.put("Max", 2);
        functionAndAttributes.put("GroupBy", -1);
        functionAndAttributes.put("Having", 3);
        functionAndAttributes.put("AndHaving", 3);
        functionAndAttributes.put("OrHaving", 3);
        functionAndAttributes.put("WhereInQ", 2);
        functionAndAttributes.put("WhereEqQ", 2);
        rules.add(0, new SyntaxRule());
        rules.add(1, new ArgNumberRule());
        rules.add(2, new DateFormatRule());
        rules.add(3, new WhereInParametarListRule());
        rules.add(4, new JoinOnRule());
        rules.add(5, new AggregationAliasMustExistIfHaving());
        rules.add(6, new OnlyAggregationElementsInHavingRule());
        rules.add(7, new NonAggregationElementsInGroupByRule());
        rules.add(8, new SubQueryVarMustExist());
    }

    @Override
    public void check(String str) {

        clearFeedbackReport();
        this.queries = divideQueries(str);

        for (Query query : queries) {
            for (IRule rule : rules) {
                rule.checkRule(query, functionAndAttributes, this);
            }
            if (feedbackReport.isEmpty()) {
                collectFunctions(query);
            }
        }

        if (feedbackReport.isEmpty()) {
            AppCore.getInstance().getQueryBuilder().getCompiler().parseText(queries);
        } else {
            AppCore.getInstance()
                    .notifySubscribers(
                            new Notification(NotificationCode.VALIDATOR_ERROR, feedbackReport));
        }
    }

    private void collectFunctions(Query query) {
        for (String validFunction : query.getValidFunctions()) {
            if (validFunction.equals("new Query")) {
                query.getFunctions().add("Query");
            } else if (!validFunction.equals("ParametarList")) {
                query.getFunctions().add(validFunction);
            }
        }
    }

    public List<Query> divideQueries(String str) {
        List<Query> dividedQueries = new ArrayList<>();
        String[] variables = str.split("var");
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
            feedbackReport.push("Variables can not have the same name");
            return;
        }
        dividedQueries.add(query);
    }

    List<IRule> rules() {
        return rules;
    }

    Map<String, Integer> functionAndAttributes() {
        return functionAndAttributes;
    }

    public void pushFeedback(String error) {
        this.feedbackReport.push(error);
    }

    public void clearFeedbackReport() {
        this.feedbackReport.clear();
    }

    public boolean checkVarName(String str, int statementNo) {
        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            if (current == '=') {
                if (i == 0) {
                    feedbackReport.push("Var name not specified for statement no: " + statementNo);
                    return false;
                }
                return true;
            }
            if (!isAllowedNameChar(current, i)) {
                feedbackReport.push(nameCharError(i, statementNo));
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

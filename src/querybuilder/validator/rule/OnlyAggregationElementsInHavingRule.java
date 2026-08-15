package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OnlyAggregationElementsInHavingRule implements IRule {

    private static final String COUNT = "Count";
    private static final String HAVING = "Having";
    private static final String OR_HAVING = "OrHaving";
    private static final String AND_HAVING = "AndHaving";
    private static final List<String> AGGREGATIONS = List.of("Avg", COUNT, "Min", "Max");

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        List<String> valid = query.getValidFucntions();
        String content = query.getContent();

        boolean hasAggregation = hasAnyAggregation(valid);
        boolean hasHaving = valid.contains(HAVING);
        boolean hasAndHaving = valid.contains(AND_HAVING);
        boolean hasOrHaving = valid.contains(OR_HAVING);

        if (!(hasHaving || hasOrHaving || hasAndHaving)) {
            return;
        }

        List<String> havingArguments = new ArrayList<>();
        int count = 0;
        if (valid.contains("Avg")) {
            havingArguments.addAll(Arrays.asList(FunctionArguments.of(content, HAVING)));
            count++;
        }
        if (hasOrHaving) {
            havingArguments.addAll(Arrays.asList(FunctionArguments.of(content, OR_HAVING)));
            count++;
        }
        if (hasAndHaving) {
            havingArguments.addAll(Arrays.asList(FunctionArguments.of(content, AND_HAVING)));
            count++;
        }

        if (!hasHaving) {
            validator.pushFeedback(
                    "Cant use AndHaving and or OrHaving if having is not declared query: "
                            + query.getName());
            return;
        }
        if (!hasAggregation) {
            validator.pushFeedback(
                    "No valid aggregation arguments/functions found but has having (having can only"
                            + " use aggregation function alias query: "
                            + query.getName());
            return;
        }

        if (countMatches(aggregationArguments(valid, content), havingArguments) < count) {
            validator.pushFeedback(
                    "When using having andhaving orhaving only aggregation values can be used in"
                            + " query: "
                            + query.getName());
        }
    }

    private boolean hasAnyAggregation(List<String> valid) {
        for (String aggregation : AGGREGATIONS) {
            if (valid.contains(aggregation)) {
                return true;
            }
        }
        return false;
    }

    private List<String> aggregationArguments(List<String> valid, String content) {
        List<String> aggregationArguments = new ArrayList<>();
        for (String aggregation : AGGREGATIONS) {
            if (valid.contains(aggregation)) {
                aggregationArguments.add(FunctionArguments.of(content, aggregation)[1]);
            }
        }
        return aggregationArguments;
    }

    private int countMatches(List<String> aggregationArguments, List<String> havingArguments) {
        int flag = 0;
        for (String aggregationArgument : aggregationArguments) {
            if (havingArguments.contains(aggregationArgument)) {
                flag++;
            }
        }
        return flag;
    }
}

package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NonAggregationElementsInGroupByRule implements IRule {

    private static final String COUNT = "Count";
    private static final String SELECT = "Select";
    private static final String GROUP_BY = "GroupBy";
    private static final List<String> AGGREGATIONS = List.of("Avg", COUNT, "Min", "Max");

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        List<String> valid = query.getValidFunctions();

        List<String> aggregationArguments = new ArrayList<>();
        for (String aggregation : AGGREGATIONS) {
            if (valid.contains(aggregation)) {
                aggregationArguments.addAll(
                        Arrays.asList(FunctionArguments.of(content, aggregation)));
            }
        }
        if (aggregationArguments.isEmpty()) {
            return;
        }
        if (!valid.contains(SELECT)) {
            validator.pushFeedback(
                    "Select Function is not declared and or is wrong -- Non Aggregation elements"
                            + " must me in GroupBY query: "
                            + query.getName());
            return;
        }
        if (!valid.contains(GROUP_BY)) {
            validator.pushFeedback(
                    "GroupBy function is not declared and or is wrong -- Non Aggregation elements"
                            + " must me in GroupBY query: "
                            + query.getName());
            return;
        }

        List<String> selectArguments =
                new ArrayList<>(Arrays.asList(FunctionArguments.of(content, SELECT)));
        for (String aggregationArgument : aggregationArguments) {
            selectArguments.remove(aggregationArgument);
        }

        List<String> groupByArguments = Arrays.asList(FunctionArguments.of(content, GROUP_BY));
        for (String selectArgument : selectArguments) {
            if (!groupByArguments.contains(selectArgument)) {
                validator.pushFeedback(selectArgument + " from select must be in groupBy function");
            }
        }
    }
}

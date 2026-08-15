package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.List;
import java.util.Map;

public class AggregationAliasMustExistIfHaving implements IRule {

    private static final String COUNT = "Count";
    private static final String FOUND_FOR_QUERY = " found for query: ";
    private static final List<String> AGGREGATIONS = List.of("Avg", COUNT, "Min", "Max");

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        if (!hasAnyHaving(query)) {
            return;
        }
        for (String aggregation : AGGREGATIONS) {
            if (query.getValidFucntions().contains(aggregation)) {
                checkAlias(query, supportedFunctions, validator, aggregation);
            }
        }
    }

    private boolean hasAnyHaving(Query query) {
        List<String> valid = query.getValidFucntions();
        return valid.contains("Having")
                || valid.contains("AndHaving")
                || valid.contains("OrHaving");
    }

    private void checkAlias(
            Query query,
            Map<String, Integer> supportedFunctions,
            Validator validator,
            String aggregation) {
        String[] args = FunctionArguments.of(query.getContent(), aggregation);
        if (supportedFunctions.get(aggregation) != args.length) {
            validator.pushFeedback(
                    "When using having aggregation function("
                            + aggregation
                            + ") must contain alias--"
                            + FOUND_FOR_QUERY
                            + query.getName());
            query.getValidFucntions().remove(aggregation);
        }
    }
}

package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubQueryVarMustExist implements IRule {

    private static final String WHERE_EQ_Q = "WhereEqQ";
    private static final String WHERE_IN_Q = "WhereInQ";

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        List<String> valid = query.getValidFunctions();
        boolean hasWhereInQ = valid.contains(WHERE_IN_Q);
        boolean hasWhereEqQ = valid.contains(WHERE_EQ_Q);

        if (!hasWhereEqQ && !hasWhereInQ) {
            return;
        }

        List<String> queryNeeded = new ArrayList<>();
        if (hasWhereEqQ) {
            queryNeeded.add(FunctionArguments.of(content, WHERE_EQ_Q)[1]);
        }
        if (hasWhereInQ) {
            queryNeeded.add(FunctionArguments.of(content, WHERE_IN_Q)[1]);
        }

        List<Query> queries = validator.getQueries();
        for (String needed : queryNeeded) {
            if (!isDeclared(needed, queries)) {
                validator.pushFeedback(
                        "Query: " + needed + " not found for query: " + query.getName());
            }
        }
    }

    private boolean isDeclared(String needed, List<Query> queries) {
        for (Query candidate : queries) {
            if (needed.replaceAll("\\s", "").equals(candidate.getName())) {
                return true;
            }
        }
        return false;
    }
}

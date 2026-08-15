package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereStartsWithFunction extends Function {

    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "LIKE ";
    private static final String PATTERN_PART_3 = "%";
    private String res = "";

    public WhereStartsWithFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereStartsWith");
        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN_PART_1;
        res += args.getFirst() + " ";
        res += PATTERN_PART_2;
        res += "'";
        res += args.get(1);
        res += PATTERN_PART_3;
        res += "'";

        setRes(res);
        return res;
    }
}

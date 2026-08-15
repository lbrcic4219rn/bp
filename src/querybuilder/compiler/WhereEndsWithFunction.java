package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereEndsWithFunction extends Function {
    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "LIKE ";
    private static final String PATTERN_PART_3 = "%";
    private String res = "";

    public WhereEndsWithFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereEndsWith");
        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN_PART_1;
        res += args.getFirst() + " ";
        res += PATTERN_PART_2;
        res += "'";
        res += PATTERN_PART_3;
        res += args.get(1);
        res += "'";

        setRes(res);
        return res;
    }
}

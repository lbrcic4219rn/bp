package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class MaxFunction extends Function {

    private static final String PATTERN_PART_1 = "MAX";
    private static final String PATTERN_PART_2 = "AS";
    private String res = "";

    public MaxFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(), "Max");
        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN_PART_1;
        res += "(";
        res += args.getFirst();
        res += ") ";
        res += PATTERN_PART_2 + " ";
        res += args.get(1) + " ";

        setAlias(args.get(1));
        setRes(res);
        return res;
    }
}

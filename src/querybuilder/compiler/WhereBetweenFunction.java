package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereBetweenFunction extends Function {
    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "BETWEEN ";
    private static final String PATTERN_PART_3 = "AND ";
    private String res = "";

    public WhereBetweenFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereBetween");

        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN_PART_1;
        res += args.getFirst();
        res += " ";
        res += PATTERN_PART_2;
        res += " ";
        res += args.get(1);
        res += " ";
        res += PATTERN_PART_3;
        res += " ";
        res += args.get(2);
        res += " ";

        setRes(res);
        return res;
    }
}

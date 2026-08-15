package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class MinFunction extends Function {

    private static final String PATTERN_PART_1 = "MIN";
    private static final String PATTERN_PART_2 = "AS";
    private String res = "";

    public MinFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(), "Min");
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

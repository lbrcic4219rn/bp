package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class GroupByFunction extends Function {

    private static final String PATTERN = "GROUP BY ";
    private String res = "";

    public GroupByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "GroupBy");

        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN;
        res += args.getFirst();
        res += " ";
        setRes(res);
        return res;
    }
}

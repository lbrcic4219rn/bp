package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class GroupByFunction extends Function{

    private String pattern = "GROUP BY ";
    private String res = "";

    public GroupByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "GroupBy");

        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += pattern;
        res += args.get(0);
        res += " ";
        setRes(res);
        return res;
    }
}

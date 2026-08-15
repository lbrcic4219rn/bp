package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class AvgFunction extends Function{

    private String pattern_part1 = "AVG";
    private String pattern_part2 = "AS";
    private String res = "";

    public AvgFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(),"Avg");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern_part1;
        res += "(";
        res += args.get(0);
        res += ") ";
        res += this.pattern_part2 + " ";
        res += args.get(1) + " ";

        setAlias(args.get(1));
        setRes(res);
        return res;

    }

}

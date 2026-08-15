package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class WhereContainsFunction extends Function{

    private String pattern_part1 = "WHERE ";
    private String pattern_part2 = "LIKE ";
    private String pattern_part3 = "%";
    private String res = "";

    public WhereContainsFunction(String name, int priority) {
        super(name, priority);

    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(),"WhereContains");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern_part1;
        res += args.get(0) + " ";
        res += this.pattern_part2;
        res += "'";
        res += this.pattern_part3;
        res += args.get(1);
        res += this.pattern_part3;
        res += "'";

        setRes(res);
        return res;
    }
}

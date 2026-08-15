package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class WhereBetweenFunction extends Function{
    private String pattern_part1 = "WHERE ";
    private String patter_part2 = "BETWEEN ";
    private String patter_part3 = "AND ";
    private String res = "";

    public WhereBetweenFunction(String name, int priority) {
        super(name,priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereBetween");

        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern_part1;
        res += args.get(0);
        res += " ";
        res += this.patter_part2;
        res += " ";
        res += args.get(1);
        res += " ";
        res += this.patter_part3;
        res += " ";
        res += args.get(2);
        res += " ";

        setRes(res);
        return res;
    }
}

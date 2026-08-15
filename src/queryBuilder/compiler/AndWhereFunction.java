package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class AndWhereFunction extends Function{
    private String pattern = "AND ";
    private String res = "";

    public AndWhereFunction(String name, int priority) {
        super(name,priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "AndWhere");

        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());
        res += this.pattern;
        res += args.get(0);
        res += " ";
        res += args.get(1);
        res += " ";

        if(args.get(1).equalsIgnoreCase("like") || args.get(1).equalsIgnoreCase("LIKE")){
            res += "'";
            res += args.get(2);
            res += "'";
            res += " ";
        }else{
            res += args.get(2);
            res += " ";
        }

        setAlias(args.get(2));
        setAlias_position(0);

        setRes(res);
        return res;
    }
}

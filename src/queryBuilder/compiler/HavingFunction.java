package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class HavingFunction extends Function{

    String pattern = "HAVING ";
    String res = "";

    public HavingFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "Having");

        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        this.res += pattern;
        this.res += args.get(0) + " ";
        this.res += args.get(1) + " ";
        this.res += args.get(2) + " ";

        setAlias(args.get(0));
        setAlias_position(0);

        setRes(res);
        return this.res;
    }

}

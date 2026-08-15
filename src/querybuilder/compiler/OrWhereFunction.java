package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrWhereFunction extends Function {
    private static final String PATTERN = "OR ";
    private String res = "";

    public OrWhereFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrWhere");

        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += PATTERN;
        res += args.getFirst();
        res += " ";
        res += args.get(1);
        res += " ";

        if (args.get(1).equalsIgnoreCase("like") || args.get(1).equalsIgnoreCase("LIKE")) {
            res += "'";
            res += args.get(2);
            res += "'";
            res += " ";
        } else {
            res += args.get(2);
            res += " ";
        }

        setAlias(args.get(2));
        setAliasPosition(0);

        setRes(res);
        return res;
    }
}

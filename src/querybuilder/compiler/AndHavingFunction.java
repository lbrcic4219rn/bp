package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class AndHavingFunction extends Function {
    private static final String PATTERN = "AND ";
    private String res = "";

    public AndHavingFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "AndHaving");

        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        this.res += PATTERN;
        this.res += args.getFirst() + " ";
        this.res += args.get(1) + " ";
        this.res += args.get(2);

        setAlias(args.getFirst());
        setAliasPosition(0);

        setRes(res);
        return this.res;
    }
}

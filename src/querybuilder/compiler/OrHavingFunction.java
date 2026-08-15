package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrHavingFunction extends Function {

    private static final String PATTERN = "OR ";
    private String res = "";

    public OrHavingFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrHaving");

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

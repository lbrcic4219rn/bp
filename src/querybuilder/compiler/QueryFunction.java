package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class QueryFunction extends Function {

    private static final String PATTERN = "FROM ";
    private String res = "";
    @Getter private String tableName;

    public QueryFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(), "Query");
        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        this.res += PATTERN;
        this.res += args.getFirst();
        this.res += " ";

        setRes(res);
        this.tableName = args.getFirst();
        return res;
    }

    @Override
    public String getRes() {
        return res;
    }
}

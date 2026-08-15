package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class QueryFunction extends Function {

    private static final String PATTERN = "FROM ";
    private String res = ""; // uvek je na kraju formata npr:  FROM Departments ;
    private String tableName;

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

    public String getRes() {
        return res;
    }

    public String getTableName() {
        return tableName;
    }
}

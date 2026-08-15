package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class QueryFunction extends Function {

    @Getter private String tableName;

    public QueryFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "Query");

        this.tableName = args.getFirst();
        String res = "FROM %s ".formatted(this.tableName);

        setRes(res);
        return res;
    }
}

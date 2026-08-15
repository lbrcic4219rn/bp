package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class WhereEqQFunction extends Function {

    @Getter private String subQuery;

    public WhereEqQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "WhereEqQ");

        this.subQuery = args.get(1);
        String res = "WHERE %s = ".formatted(args.getFirst());

        setRes(res);
        return res;
    }
}

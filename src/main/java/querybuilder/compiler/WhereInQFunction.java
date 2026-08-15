package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class WhereInQFunction extends Function {

    @Getter private String subQuery;

    public WhereInQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "WhereInQ");

        this.subQuery = args.get(1);
        String res = "WHERE %s IN ".formatted(args.getFirst());

        setRes(res);
        return res;
    }
}

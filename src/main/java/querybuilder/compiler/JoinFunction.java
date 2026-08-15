package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class JoinFunction extends Function {

    @Getter private String tableName;

    public JoinFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        this.tableName = extractArguments(q.getContent(), "Join").getFirst();

        List<String> args = extractArguments(q.getContent(), "On");

        String qualified = args.get(2);
        String joinedTable = qualified.substring(0, qualified.indexOf('.'));
        String res = "JOIN %s ON %s ".formatted(joinedTable, String.join("", args));

        setRes(res);
        return res;
    }
}

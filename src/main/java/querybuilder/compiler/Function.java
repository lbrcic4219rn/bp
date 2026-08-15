package querybuilder.compiler;

import lombok.Getter;
import lombok.Setter;

import querybuilder.validator.Query;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Function {

    private final int priority;
    private final String name;
    private String res = "";
    private String alias = "";
    private int aliasPosition;

    protected Function(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    protected abstract String parseQuery(Query q);

    protected List<String> extractArguments(String content, String functionName) {

        List<String> args = new ArrayList<>();
        int open = content.indexOf('(', content.indexOf(functionName));
        String help = "";
        if (open != -1) {
            help = content.substring(open + 1, content.indexOf(')', open + 1));
        }

        StringBuilder arg = new StringBuilder();
        for (int i = 0; i < help.length(); i++) {
            char current = help.charAt(i);
            if (current == ',') {
                args.add(arg.toString());
                arg.setLength(0);
            } else if (current != '"' && current != ' ') {
                arg.append(current);
            }
        }
        args.add(arg.toString());
        return args;
    }

    protected String quoteIfLike(String operator, String value) {
        return operator.equalsIgnoreCase("like") ? "'" + value + "'" : value;
    }
}

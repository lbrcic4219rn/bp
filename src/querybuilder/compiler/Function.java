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
    private String res = "";
    private final String name;
    private String alias = "";
    private int aliasPosition;

    protected Function(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    protected abstract String parseQuery(Query q);

    public int getIndexInStringWhereStarts(String str, String lookingFor) {
        return str.indexOf(lookingFor);
    }

    public List<String> getArgsThatAreNotAllString(int idx, String s) {

        List<String> args = new ArrayList<>();
        int open = s.indexOf('(', idx);
        String help = "";
        if (open != -1) {
            help = s.substring(open + 1, s.indexOf(')', open + 1));
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

    @Override
    public String toString() {
        return this.name;
    }
}

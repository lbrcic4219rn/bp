package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereInFunction extends Function {
    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "IN ";

    public WhereInFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereIn");
        List<String> argsWhereIn = getArgsThatAreNotAllString(idx, q.getContent());

        idx = getIndexInStringWhereStarts(q.getContent(), "ParametarList");
        List<String> args = getArgsThatAreNotAllString(idx, q.getContent());

        StringBuilder sb = new StringBuilder(PATTERN_PART_1);
        sb.append(argsWhereIn.getFirst()).append(" ").append(PATTERN_PART_2).append("(");
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i));
            if (i != args.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") ");

        String res = sb.toString();
        setRes(res);
        return res;
    }
}

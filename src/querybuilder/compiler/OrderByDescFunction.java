package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrderByDescFunction extends Function {

    private static final String PATTERN_PART_1 = "ORDER BY ";
    private static final String PATTERN_PART_2 = "DESC ";

    public OrderByDescFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrderByDesc");
        List<String> args = getArgsThatAreNotAllString(idx, q.getContent());

        StringBuilder sb = new StringBuilder(PATTERN_PART_1);
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i)).append(i != args.size() - 1 ? ", " : " ");
        }
        sb.append(" ").append(PATTERN_PART_2);

        String res = sb.toString();
        setRes(res);
        return res;
    }
}

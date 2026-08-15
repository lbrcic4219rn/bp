package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrderByFunction extends Function {

    private static final String PATTERN = "ORDER BY ";

    public OrderByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrderBy");
        List<String> args = getArgsThatAreNotAllString(idx, q.getContent());

        StringBuilder sb = new StringBuilder(PATTERN);
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i)).append(i != args.size() - 1 ? ", " : " ");
        }

        String res = sb.toString();
        setRes(res);
        return res;
    }
}

package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

@Getter
public class JoinFunction extends Function {
    private static final String PATTERN_PART_1 = "JOIN ";
    private static final String PATTERN_PART_2 = "ON ";
    private String tableName;

    public JoinFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "Join");
        List<String> argsJoin = getArgsThatAreNotAllString(idx, q.getContent());
        this.tableName = argsJoin.getFirst();

        idx = getIndexInStringWhereStarts(q.getContent(), "On");
        List<String> args = getArgsThatAreNotAllString(idx, q.getContent());

        StringBuilder sb = new StringBuilder(PATTERN_PART_1);

        String name = args.get(2);
        int cnt = 0;
        while (name.charAt(cnt) != '.') {
            cnt++;
        }
        sb.append(name, 0, cnt).append(" ").append(PATTERN_PART_2);

        for (String arg : args) {
            sb.append(arg);
        }
        sb.append(" ");

        String res = sb.toString();
        setRes(res);
        return res;
    }
}

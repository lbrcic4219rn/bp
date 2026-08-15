package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class JoinFunction extends Function{
    private String pattern_part1 = "JOIN ";
    private String patter_part2 = "ON ";
    private String res = "";
    private String tableName;

    public JoinFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        ArrayList<String> args_join;
        int idx = getIndexInStringWhereStarts(q.getContent(), "Join");

        args_join = getArgsThatAreNotAllString(idx, q.getContent());
        this.tableName = args_join.get(0);

        idx = getIndexInStringWhereStarts(q.getContent(), "On");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());
        res += this.pattern_part1;
        //primer za ovo parsiranje:
        //new Query("jobs").Select("job_title","min_salary","email").Join("jobs").On("jobs.job_id", "=","employees.job_id")
        //r ce biti employees
        String name = args.get(2);
        String r = "";
        int cnt = 0;
        while(name.charAt(cnt) != '.'){
            r += name.charAt(cnt);
            cnt++;
        }
        //
        res += r;
        res += " ";
        res += this.patter_part2;

        for(int i = 0; i < args.size(); i++){
            this.res += args.get(i);
        }
        res += " ";
        setRes(res);
        return res;
    }

    public String getTableName() {
        return tableName;
    }
}

package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class OrderByDescFunction extends Function{

    private String pattern_part1 = "ORDER BY ";
    private String pattern_part2 = "DESC ";
    private String res = "";// npr: ORDER BY MANAGER_ID

    public OrderByDescFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrderByDesc");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += pattern_part1;

        for(int i = 0; i < args.size(); i++){
            if(i != args.size()-1){
                this.res += args.get(i);
                this.res += ", ";
            }else{
                this.res += args.get(i);
                this.res += " ";
            }
        }

        res += " ";
        res += pattern_part2;
        setRes(res);
        return res;
    }
}

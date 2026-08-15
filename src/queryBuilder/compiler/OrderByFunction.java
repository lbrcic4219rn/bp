package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class OrderByFunction extends Function{

    private String pattern = "ORDER BY ";
    private String res = "";// npr: ORDER BY MANAGER_ID

    public OrderByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "OrderBy");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += pattern;

        for(int i = 0; i < args.size(); i++){
            if(i != args.size()-1){
                this.res += args.get(i);
                this.res += ", ";
            }else{
                this.res += args.get(i);
                this.res += " ";
            }
        }

        setRes(res);
        return res;
    }


}

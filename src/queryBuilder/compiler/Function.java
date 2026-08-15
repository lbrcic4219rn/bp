package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public abstract class Function {

    private int priority;
    private String res = "";
    private String name;
    private String alias = "";
    private int alias_position;

    public Function(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    protected abstract String parseQuery(Query q);

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public int getAlias_position() {
        return alias_position;
    }

    public void setAlias_position(int alias_position) {
        this.alias_position = alias_position;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }
    public int getIndexInStringWhereStarts(String str, String lookingFor){
        return str.indexOf(lookingFor);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<String> getArgsThatAreNotAllString(int idx, String s){//dohvata argumente u zagradi; argumenti mogu a ne moraju da budu pod "", bitno je samo
                                                                            //da su razdvojeni zarezima
        String help = "";
        ArrayList<String> args = new ArrayList<>();
        for(int i = idx; i < s.length();i++){
            if(s.charAt(i) != '('){
                continue;
            }
            i++;
            while(s.charAt(i) != ')'){
                help += s.charAt(i);
                i++;
            }
            break;
        }
        int cnt = 0;
        String arg = "";
        for(int i = 0; i < help.length();i++){
            if(help.charAt(i) == '\"' || help.charAt(i) == ' '){//DODATA PROVERA ZA SPACE
                continue;
            }
            if(help.charAt(i) == ','){
                args.add(arg);
                arg = "";
                continue;
            }
            arg += help.charAt(i);
        }
        args.add(arg);
        return args;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

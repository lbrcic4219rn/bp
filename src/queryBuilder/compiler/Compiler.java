package queryBuilder.compiler;

import app.AppCore;
import database.Database;
import database.MSSQLrepository;
import observer.Notification;
import observer.enums.NotificationCode;
import queryBuilder.validator.Query;

import java.sql.PreparedStatement;
import java.util.*;

public class Compiler implements ICompiler{

    private ArrayList<Function> functions = new ArrayList<>();
    private Map<String,String> queryResults = new HashMap<>();//key je naziv query-ja, value je parsirani string->sluzi za subquery-je
    private Map<String,String> aliases_values = new HashMap<>();
    private String solution;

    @Override
    public void parseText(ArrayList<Query> queries) {

        String tableNameFromQuery = "";
        String tableNameFromJoin = "";

        String result = "";
        for(Query q: queries){
            for(String f: q.getFunctions()){
                switch (f){
                    case "Query":
                        Function f1 = new QueryFunction("Query", 3);
                        this.functions.add(f1);
                        f1.parseQuery(q);
                        tableNameFromQuery = ((QueryFunction)f1).getTableName();
                        break;
                    case "Select":
                        Function f2 = new SelectFunction("Select", 1);
                        this.functions.add(f2);
                        f2.parseQuery(q);
                        break;
                    case "OrderBy":
                        Function f3 = new OrderByFunction("OrderBy", 12);
                        this.functions.add(f3);
                        f3.parseQuery(q);
                        break;
                    case "OrderByDesc":
                        Function f4 = new OrderByDescFunction("OrderByDesc", 12);
                        this.functions.add(f4);
                        f4.parseQuery(q);
                        break;
                    case "Where":
                        Function f5 = new WhereFunction("Where", 5);
                        this.functions.add(f5);
                        f5.parseQuery(q);
                        break;
                    case "OrWhere":
                        Function f6 = new OrWhereFunction("OrWhere", 6);
                        this.functions.add(f6);
                        f6.parseQuery(q);
                        break;
                    case "AndWhere":
                        Function f7 = new AndWhereFunction("AndWhere", 6);
                        this.functions.add(f7);
                        f7.parseQuery(q);
                        break;
                    case "WhereBetween":
                        Function f8 = new WhereBetweenFunction("WhereBetween", 5);
                        this.functions.add(f8);
                        f8.parseQuery(q);
                        break;
                    case "WhereIn":
                        Function f9 = new WhereInFunction("WhereIn", 5);
                        this.functions.add(f9);
                        f9.parseQuery(q);
                        break;
                    case "Join":
                        Function f10 = new JoinFunction("Join", 4);
                        this.functions.add(f10);
                        f10.parseQuery(q);
                        tableNameFromJoin = ((JoinFunction)f10).getTableName();
                        break;
                    case "WhereEndsWith":
                        Function f11 = new WhereEndsWithFunction("WhereEndsWith", 5);
                        this.functions.add(f11);
                        f11.parseQuery(q);
                        break;
                    case "WhereStartsWith":
                        Function f12 = new WhereStartsWithFunction("WhereStartsWith", 5);
                        this.functions.add(f12);
                        f12.parseQuery(q);
                        break;
                    case "WhereContains":
                        Function f13 = new WhereContainsFunction("WhereContains", 5);
                        this.functions.add(f13);
                        f13.parseQuery(q);
                        break;
                    case "Avg":
                        Function f14 = new AvgFunction("Avg", 2);
                        this.functions.add(f14);
                        f14.parseQuery(q);
                        this.aliases_values.put(f14.getAlias(), f14.getRes());
                        f14.setAlias("");
                        break;
                    case "Count":
                        Function f15 = new CountFunction("Count", 2);
                        this.functions.add(f15);
                        f15.parseQuery(q);
                        this.aliases_values.put(f15.getAlias(), f15.getRes());
                        f15.setAlias("");
                        break;
                    case "Min":
                        Function f16 = new MinFunction("Min", 2);
                        this.functions.add(f16);
                        f16.parseQuery(q);
                        this.aliases_values.put(f16.getAlias(), f16.getRes());
                        f16.setAlias("");
                        break;
                    case "Max":
                        Function f17 = new MaxFunction("Max", 2);
                        this.functions.add(f17);
                        f17.parseQuery(q);
                        this.aliases_values.put(f17.getAlias(), f17.getRes());
                        f17.setAlias("");
                        break;
                    case "GroupBy":
                        Function f18 = new GroupByFunction("GroupBy", 9);
                        this.functions.add(f18);
                        f18.parseQuery(q);
                        break;
                    case "Having":
                        Function f19 = new HavingFunction("Having", 10);
                        this.functions.add(f19);
                        f19.parseQuery(q);
                        break;
                    case "AndHaving":
                        Function f20 = new AndHavingFunction("AndHaving", 11);
                        this.functions.add(f20);
                        f20.parseQuery(q);
                        break;
                    case "OrHaving":
                        Function f21 = new OrHavingFunction("OrHaving", 11);
                        this.functions.add(f21);
                        f21.parseQuery(q);
                        break;
                    case "WhereInQ":
                        Function f22 = new WhereInQFunction("WhereInQ", 5);
                        this.functions.add(f22);
                        f22.parseQuery(q);
                        break;
                    case "WhereEqQ":
                        Function f23 = new WhereEqQFunction("WhereEqQ", 5);
                        this.functions.add(f23);
                        f23.parseQuery(q);
                        break;
                    case "Date":
                        Function f24 = new DateFunction("Date", 4);
                        this.functions.add(f24);
                        f24.parseQuery(q);
                        this.aliases_values.put(f24.getAlias(), f24.getRes());
                        f24.setAlias("");
                        break;
                    default:
                        break;
                }
            }
            this.sortFunctions(functions);
            if(!functions.get(0).getName().equalsIgnoreCase("Select")){//ukoliko functions ne sadrzi f-ju select, a ona ako postoji ce uvek biti prva posto
                // je najveceg prioriteta, znaci da korisnik zeli sve kolone iz tabele
                result += "SELECT * ";
            }
            if(!tableNameFromJoin.equalsIgnoreCase("")){//provera ako je koriscena f-ja join, da li je pravilno uneseno ime tabele
                if(!tableNameFromJoin.equalsIgnoreCase(tableNameFromQuery)){
                    tableNameFromJoin = "";
                    tableNameFromQuery = "";
                    result = "";
                    this.functions.clear();
                    AppCore.getInstance().notifySubscribers(new Notification(NotificationCode.ERROR,"In Join() must be the same table name as in Query()"));
                    return;
                }
                tableNameFromJoin = "";
                tableNameFromQuery = "";
            }
            for(Function f: this.functions){

                if((f instanceof AvgFunction || f instanceof MaxFunction || f instanceof  MinFunction || f instanceof CountFunction) &&
                        (!result.equalsIgnoreCase("SELECT "))){
                    result += ", ";
                }
                if(!f.getAlias().equalsIgnoreCase("")){
                    String s = f.getRes();
                    String alias = f.getAlias();
                    String value = getValueFromAlias(alias, this.aliases_values);

                    if(value != null){
                        String str_array[] = value.split(" ");
                        int alias_pos = f.getAlias_position();
                        s = s.replaceAll(alias, str_array[alias_pos]);
                        f.setRes(s);
                    }
                }

                if(f instanceof DateFunction){
                    continue;
                }

                result += f.getRes();

                if(f instanceof WhereInQFunction){
                    WhereInQFunction wif = (WhereInQFunction)f;
                    result += "(";
                    String help = this.findQueryValueWithKey(wif.getSub_query(),this.queryResults);
                    result += help;
                    result += ") ";
                }
                if(f instanceof WhereEqQFunction){
                    WhereEqQFunction wef = (WhereEqQFunction)f;
                    result += "(";
                    String help = this.findQueryValueWithKey(wef.getSub_query(),this.queryResults);
                    result += help;
                    result += ") ";
                }
            }
            queryResults.put(q.getName(),result);
            this.solution = result;
            //System.out.println(this.solution);
            result = "";
            this.functions.clear();
            this.aliases_values.clear();
        }
        //ako ima vise query-ja uvek se izvrsava samo onaj poslednji
        //ako zelis da upotrebis subquery, definisi ga prvog i prosledi ga u drugom query-ju;
        AppCore.getInstance().readDataFromTable(this.solution);
    }

    public void sortFunctions(ArrayList<Function> functions){
        //select, avg min max count, query, join, on, where, group by, having, orderby--> f-je redom sa prioritetom od najvaznije do najmanje vazne

        Collections.sort(functions, new Comparator<Function>() {
            @Override
            public int compare(Function o1, Function o2) {
                if(o1.getPriority() >= o2.getPriority()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }
    public String findQueryValueWithKey(String name, Map<String, String> queryResults){

        for (String key: queryResults.keySet()) {
           if(key.equalsIgnoreCase(name)){
               return queryResults.get(key);
           }
        }
        return null;
    }
    public String getValueFromAlias(String alias, Map<String, String> al_val){
        for (String key: al_val.keySet()) {
            if(key.equalsIgnoreCase(alias)){
                return al_val.get(key);
            }
        }
        return null;
    }
}

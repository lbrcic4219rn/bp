package queryBuilder.validator;

import app.AppCore;
import observer.Notification;
import observer.enums.NotificationCode;
import queryBuilder.validator.rule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

//SAMPLE QUERRT
//var a = new Query("departments").Select("manager_id", "location_id").OrderBy("manager_id")

public class Validator implements IValidator{
    private boolean valid = true;
    private Stack<String> feedbackReport = new Stack<>();
    private HashMap<String, Integer> functionAndAttributes = new HashMap<>();
    private ArrayList<IRule> rules = new ArrayList<>();
    private ArrayList<Query> queries = new ArrayList<>();

    /** Inicira validne f-je i rules za proveru*/
    public Validator() {
        // -1 proizvoljan br atributa
        //join mora da prati on
        //wehrein mora da prati parameterList
        functionAndAttributes.put("new Query", 1);
        functionAndAttributes.put("Select", -1);
        functionAndAttributes.put("OrderBy", -1);
        functionAndAttributes.put("OrderByDesc", -1);
        functionAndAttributes.put("Where", 3);
        functionAndAttributes.put("OrWhere", 3);
        functionAndAttributes.put("AndWhere", 3);
        functionAndAttributes.put("WhereBetween", 3);
        functionAndAttributes.put("WhereIn", 1);
        functionAndAttributes.put("ParametarList", -1);
        functionAndAttributes.put("Join", 1);
        functionAndAttributes.put("On", 3);
        functionAndAttributes.put("Date", 2);
        functionAndAttributes.put("WhereEndsWith", 2);
        functionAndAttributes.put("WhereStartsWith", 2);
        functionAndAttributes.put("WhereContains", 2);
        functionAndAttributes.put("Avg", 2);
        functionAndAttributes.put("Count", 2);
        functionAndAttributes.put("Min", 2);
        functionAndAttributes.put("Max", 2);
        functionAndAttributes.put("GroupBy", -1);
        functionAndAttributes.put("Having", 3);
        functionAndAttributes.put("AndHaving", 3);
        functionAndAttributes.put("OrHaving", 3);
        functionAndAttributes.put("WhereInQ", 2);
        functionAndAttributes.put("WhereEqQ", 2);
        rules.add(0,new SyntaxRule());
        rules.add(1,new ArgNumberRule());
        rules.add(2,new DateFormatRule());
        rules.add(3, new WhereInParametarListRule());
        rules.add(4, new JoinOnRule());
        rules.add(5,new AggregationAliasMustExistIfHaving());
        rules.add(6,new OnlyAggregationElementsInHavingRule());
        rules.add(7,new NonAggregationElementsInGroupByRule());
        rules.add(8,new SubQueryVarMustExist());

    }

    @Override
    public void check(String str) {

        /** Pocinje se sa praznim feedbackom */
        clearFeedbackReport();
        /** Razdvaja String na vise kverija u koliko postoje vraca iskljucivo one koje zadovoljavaju var imeprom = */

        this.queries = divideQueries(str);

        for (int i = 0; i < queries.size(); i++){
            for (int j = 0; j < rules.size(); j++){
                rules.get(j).checkRule(queries.get(i), functionAndAttributes, this);
            }
            if(feedbackReport.isEmpty()){
                for (int k = 0; k < queries.get(i).getValidFucntions().size(); k++){
                    if(queries.get(i).getValidFucntions().get(k).equals("new Query")){
                        queries.get(i).getFunctions().add("Query");
                        continue;
                    }
                    if(queries.get(i).getValidFucntions().get(k).equals("ParametarList")){
                        continue;
                    }
                    queries.get(i).getFunctions().add(queries.get(i).getValidFucntions().get(k));
                }

            }
        }

        if(feedbackReport.isEmpty()){
            AppCore.getInstance().getQueryBuilder().getCompiler().parseText(queries);
        }else{
            AppCore.getInstance().notifySubscribers(new Notification(NotificationCode.VALIDATOR_ERROR, feedbackReport));
        }
    }
    public ArrayList<Query> divideQueries(String str){
        ArrayList<Query> queries = new ArrayList<>();
        //System.out.println("Divide querise data: ");
        String [] variables = str.split("var");
        for(int i = 0; i < variables.length; i++){
            if(variables[i] == str) {
                pushFeedback("No variables declared");
                return queries;
            }
            String tmp = variables[i].replaceAll("\\s", "");
            if( i == 0 && tmp.length() != 0){

                pushFeedback("First query missing var");
                return queries;
            }
            if(i != 0 && checkVarName(tmp, i)) {
                boolean flag = true;
                Query query = new Query(variables[i]);
                query.setName(tmp.substring(0, tmp.indexOf("=")));
                if (queries.isEmpty()){
                    queries.add(query);
                    continue;
                }
                for(int j = 0; j < queries.size(); j++) {
                    //System.out.println(query.getName() + " : " + queries.get(j).getName());
                    if (query.equals(queries.get(j))) {
                        feedbackReport.push("Variables can not have the same name");
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    queries.add(query);
            }
            //System.out.println("calls: " + variables[i]);
        }
        return queries;
    }

    public ArrayList<Query> getQueries() {
        return queries;
    }

    public Stack<String> getFeedbackReport() {
        return feedbackReport;
    }
    public void setFeedbackReport(Stack<String> feedbackReport) {
        this.feedbackReport = feedbackReport;
    }
    public void pushFeedback(String error){
        this.feedbackReport.push(error);
    }
    public void clearFeedbackReport(){
        this.feedbackReport.clear();
    }
    public boolean checkVarName(String str, int statementNo){
        for(int i = 0; i < str.length(); i++){
            //Ako je na nultom mestu "=" to znaci da ne postoji deklaracija
            if(str.charAt(i) == '='){
                if(i == 0){
                    feedbackReport.push("Var name not specified for statement no: " + statementNo);
                    return false;
                }
                break;
            }
            if(i == 0){
                if(!Character.isLetter(str.charAt(i))){
                    feedbackReport.push("Variable name cant start with special characters or numbers statement no: " + statementNo);
                    return false;
                }
            }else{
                if(!(Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i)) || str.charAt(i) == '_')){
                    feedbackReport.push("Variable name can not contain special characters other than '_' statement no: " + statementNo);
                    return false;
                }
            }
        }
        return true;
    }
}

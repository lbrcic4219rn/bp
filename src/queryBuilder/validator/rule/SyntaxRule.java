package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.HashMap;

public class SyntaxRule implements IRule {
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        //System.out.println("Querry name " + query.getName());
        String content = query.getContent();
        content = content.substring(content.indexOf('=') + 1, content.length());
        String fucntionName="";
        //Nasao sve funkcije koje se koriste samo fali prvo slovo
        String [] calledFunctions = content.split("\\.[A-Z]");
        for (int i = 0; i < calledFunctions.length; i++){
            //System.out.println("functions: " + calledFunctions[i]);
            int br = 1;
            if(i == 0) br = 0;
            String fixedFunctionName = content.substring(content.indexOf(calledFunctions[i]) - br, content.indexOf(calledFunctions[i]) + calledFunctions[i].length());
            String trucedFName = fixedFunctionName.replaceAll("\\s", "");
            if(fixedFunctionName.indexOf('(') == -1){
                validator.pushFeedback("Missing ( for fucntion: " + fixedFunctionName);
                continue;
            } else if(fixedFunctionName.indexOf(')') == -1){
                validator.pushFeedback("Missing ) for fucntion: " + fixedFunctionName);
                continue;
            } else if(trucedFName.indexOf(')') + 1 < trucedFName.length()){
                if(trucedFName.indexOf('.') == -1)
                    validator.pushFeedback("Missing . opperator for fucntion: " + fixedFunctionName);
                else
                    validator.pushFeedback("Unknown function: " + fixedFunctionName);
                continue;
            } else{
                fucntionName = fixedFunctionName.substring(0, fixedFunctionName.indexOf('('));
                if(i == 0){
                    if(fucntionName.indexOf("new") == -1){
                        validator.pushFeedback("Missing keyword new");
                        continue;
                    }else{
                        String tmp = fucntionName.replaceAll("\\s", "");
                        tmp = "new " + tmp.substring(tmp.indexOf("new") + 3, tmp.length());
                        fucntionName = tmp;
                    }
                }
                if(supportedFunctions.get(fucntionName) == null){
                    validator.pushFeedback("Unknown function: " + fucntionName);
                    continue;
                }
            }
            query.getValidFucntions().add(fucntionName);
        }
    }
    private int charOccurrences(String str, char c){
        int res = 0;
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == c)
                res++;
        return res;
    }
}

package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;

public class SyntaxRule implements IRule {

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        content = content.substring(content.indexOf('=') + 1);
        String[] calledFunctions = content.split("\\.[A-Z]");

        for (int i = 0; i < calledFunctions.length; i++) {
            String accepted =
                    resolveFunction(content, calledFunctions[i], i, supportedFunctions, validator);
            if (accepted != null) {
                query.getValidFucntions().add(accepted);
            }
        }
    }

    private String resolveFunction(
            String content,
            String calledFunction,
            int index,
            Map<String, Integer> supportedFunctions,
            Validator validator) {
        int offset = index == 0 ? 0 : 1;
        int start = content.indexOf(calledFunction);
        String fixedFunctionName =
                content.substring(start - offset, start + calledFunction.length());
        String trucedFName = fixedFunctionName.replaceAll("\\s", "");

        if (fixedFunctionName.indexOf('(') == -1) {
            validator.pushFeedback("Missing ( for fucntion: " + fixedFunctionName);
            return null;
        }
        if (fixedFunctionName.indexOf(')') == -1) {
            validator.pushFeedback("Missing ) for fucntion: " + fixedFunctionName);
            return null;
        }
        if (trucedFName.indexOf(')') + 1 < trucedFName.length()) {
            if (trucedFName.indexOf('.') == -1) {
                validator.pushFeedback("Missing . opperator for fucntion: " + fixedFunctionName);
            } else {
                validator.pushFeedback("Unknown function: " + fixedFunctionName);
            }
            return null;
        }

        String functionName = fixedFunctionName.substring(0, fixedFunctionName.indexOf('('));
        if (index == 0) {
            if (!functionName.contains("new")) {
                validator.pushFeedback("Missing keyword new");
                return null;
            }
            String tmp = functionName.replaceAll("\\s", "");
            functionName = "new " + tmp.substring(tmp.indexOf("new") + 3);
        }
        if (supportedFunctions.get(functionName) == null) {
            validator.pushFeedback("Unknown function: " + functionName);
            return null;
        }
        return functionName;
    }
}

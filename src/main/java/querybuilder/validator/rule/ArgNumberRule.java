package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.List;
import java.util.Map;

public class ArgNumberRule implements IRule {

    private static final String FOR_FUNCTION = " for function: ";
    private static final List<String> ALIAS_FUNCTIONS =
            List.of("Avg", "Count", "Min", "Max", "Having", "AndHaving", "OrHaving");

    private enum SingleArgResult {
        KEEP,
        REMOVE,
        UNDECIDED
    }

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        query.getValidFunctions()
                .removeIf(
                        currentFunction ->
                                !isValid(query, supportedFunctions, validator, currentFunction));
    }

    private boolean isValid(
            Query query,
            Map<String, Integer> supportedFunctions,
            Validator validator,
            String currentFunction) {
        String args = rawArguments(query.getContent(), currentFunction);
        int requiredArgNumber = supportedFunctions.get(currentFunction);

        if (args.indexOf(',') == -1 && args.isEmpty()) {
            validator.pushFeedback(
                    "Function can not have zero arguments for function: " + currentFunction);
            return false;
        }

        if (args.indexOf(',') == -1) {
            SingleArgResult result =
                    checkSingleArgument(
                            args,
                            requiredArgNumber,
                            currentFunction,
                            supportedFunctions,
                            validator);
            if (result != SingleArgResult.UNDECIDED) {
                return result == SingleArgResult.KEEP;
            }
        }

        return checkEachArgument(
                args, requiredArgNumber, currentFunction, supportedFunctions, validator);
    }

    private SingleArgResult checkSingleArgument(
            String args,
            int requiredArgNumber,
            String currentFunction,
            Map<String, Integer> supportedFunctions,
            Validator validator) {
        int numberOfQuotes = quoteCount(args);

        if (numberOfQuotes != 0 && numberOfQuotes != 2) {
            validator.pushFeedback(
                    "Problem with quotes and or , operator for function: " + currentFunction);
            return SingleArgResult.REMOVE;
        }
        if (numberOfQuotes > 0 && isNotQuoted(args)) {
            validator.pushFeedback(
                    "Quotes need to be at the start and end of a string for attribute: "
                            + args
                            + FOR_FUNCTION
                            + currentFunction);
            return SingleArgResult.REMOVE;
        }
        if (requiredArgNumber != 1 && requiredArgNumber != -1) {
            if (ALIAS_FUNCTIONS.contains(currentFunction)
                    && supportedFunctions.get(currentFunction) - 1 == 1) {
                return SingleArgResult.KEEP;
            }
            validator.pushFeedback(
                    "Expected "
                            + requiredArgNumber
                            + " arguments but only got 1 for function: "
                            + currentFunction);
            return SingleArgResult.REMOVE;
        }
        return SingleArgResult.UNDECIDED;
    }

    private boolean checkEachArgument(
            String args,
            int requiredArgNumber,
            String currentFunction,
            Map<String, Integer> supportedFunctions,
            Validator validator) {
        String[] arguments = args.split(",");
        int supported = supportedFunctions.get(currentFunction);
        boolean invalid = false;

        for (String argument : arguments) {
            int argumentQuoteNo = quoteCount(argument);
            if (argumentQuoteNo != 0 && argumentQuoteNo != 2) {
                validator.pushFeedback(
                        "Problem with quotes need to be ether 0 for int and 2 for string values"
                                + " attribute: "
                                + argument
                                + FOR_FUNCTION
                                + currentFunction);
                invalid = true;
            } else if (argumentQuoteNo > 0 && isNotQuoted(argument)) {
                validator.pushFeedback(
                        "Quotes need to be at the start and end of a string for attribute: "
                                + argument
                                + FOR_FUNCTION
                                + currentFunction);
                invalid = true;
            } else if (supported != arguments.length
                    && supported != -1
                    && !(ALIAS_FUNCTIONS.contains(currentFunction)
                            && supported - 1 == arguments.length)) {
                validator.pushFeedback(
                        "Expected"
                                + requiredArgNumber
                                + " arguments but got: "
                                + arguments.length
                                + FOR_FUNCTION
                                + currentFunction);
                invalid = true;
            }
        }
        return !invalid;
    }

    private String rawArguments(String content, String currentFunction) {
        int start = content.indexOf(currentFunction);
        String functionWithArgs = content.substring(start, content.indexOf(')', start) + 1);
        return functionWithArgs
                .substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')'))
                .replaceAll("\\s", "");
    }

    private boolean isNotQuoted(String value) {
        return value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"';
    }

    private int quoteCount(String str) {
        int res = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '"') {
                res++;
            }
        }
        return res;
    }
}

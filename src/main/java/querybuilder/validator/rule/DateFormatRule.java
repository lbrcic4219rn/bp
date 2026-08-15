package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateFormatRule implements IRule {

    private static final Logger LOGGER = Logger.getLogger(DateFormatRule.class.getName());

    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        boolean hasDate = query.getValidFunctions().contains("Date");
        if (hasDate) {
            String functionWithArgs =
                    content.substring(
                            content.indexOf("Date"),
                            content.indexOf(')', content.indexOf("Date")) + 1);
            String args =
                    functionWithArgs
                            .substring(
                                    functionWithArgs.indexOf('(') + 1,
                                    functionWithArgs.indexOf(')'))
                            .replaceAll("\\s", "");
            String[] singleArgArray = args.split(",");
            LOGGER.log(
                    Level.FINE,
                    "Date data: {0} {1}",
                    new Object[] {singleArgArray[0], singleArgArray[1]});
            String date = singleArgArray[0];
            if (date.charAt(0) != '"' || date.charAt(date.length() - 1) != '"') {
                validator.pushFeedback(
                        "Date argument (1st) needs to be surrounded by quotes in query: "
                                + query.getName());
                query.getValidFunctions().remove("Date");
                return;
            }
            date = date.substring(1, date.length() - 1);
            String[] dateSegments = date.split("/");
            if (dateSegments.length != 3) {
                validator.pushFeedback(
                        "Date argument (1st) needs to be in the following format: dd/mm/yyyy for"
                                + " query: "
                                + query.getName());
                query.getValidFunctions().remove("Date");
                return;
            }
            if (dateSegments[0].length() != 2
                    || dateSegments[1].length() != 2
                    || dateSegments[2].length() != 4) {
                validator.pushFeedback(
                        "Date argument (1st) needs to be in the following format: dd/mm/yyyy for"
                                + " query: "
                                + query.getName());
                query.getValidFunctions().remove("Date");
            }
        }
    }
}

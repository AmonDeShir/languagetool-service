package pl.amon;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.Category;
import org.languagetool.rules.CategoryId;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.patterns.AbstractPatternRule;


public class Service {
  public static void main(String[] args) {
    System.out.println(Service.check(args[0], args[1]));
  }

  public static String check(String lang, String data) {
    try {
      JLanguageTool tool = new JLanguageTool(Languages.getLanguageForShortCode(lang));
      return ruleListToString(tool.check(data));
    }
    catch (Exception e) {
      return String.format("{ error: \" %s \" }", e.toString());
    }
  }

  private static String ruleListToString(List<RuleMatch> rules) {
    final Map<String, ?> config = Collections.emptyMap();
    JsonBuilderFactory factory = Json.createBuilderFactory(config);

    JsonArrayBuilder array = factory.createArrayBuilder();
    
    for (RuleMatch match : rules) {
      Rule rule = match.getRule();
      Category category = rule.getCategory();
      CategoryId catId = category.getId();
      String ruleSubId = "";

      if (rule instanceof AbstractPatternRule) {
        ruleSubId = ((AbstractPatternRule) rule).getSubId();

        if (ruleSubId == null) {
          ruleSubId = "";
        }
      }

      JsonObjectBuilder matchBuilder = factory.createObjectBuilder()
        .add("offset", match.getFromPos())
        .add("length", match.getToPos()-match.getFromPos())
        .add("message", match.getMessage())
        .add("shortMessage", match.getShortMessage() == null ? "" : match.getShortMessage())
        .add("replacements", array(factory, match.getSuggestedReplacements()))
        .add("ruleId", rule.getId())
        .add("ruleDescription", rule.getDescription())
        .add("ruleIssueType", rule.getLocQualityIssueType().toString())
        .add("ruleSubId", ruleSubId)
        .add("ruleUrl", rule.getUrl() == null ? "" : rule.getUrl().toString())
        .add("ruleCategoryId", catId == null ? "" : catId.toString());

      array.add(matchBuilder);
    }

    JsonObjectBuilder builder = factory.createObjectBuilder().add("match", array);
    return builder.build().toString();
  }

  private static JsonArrayBuilder array(JsonBuilderFactory factory, List<String> data) {
    JsonArrayBuilder array = factory.createArrayBuilder();

    for (String obj : data) {
      array.add(obj);
    }

    return array;
  }
}
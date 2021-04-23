package edu.utec.sdk.moveon.core.request.search;

import java.util.List;

public class SearchFilter {

  private String groupOp;
  private List<Rule> rules;

  public String getGroupOp() {
    return groupOp;
  }

  public void setGroupOp(String groupOp) {
    this.groupOp = groupOp;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }
}

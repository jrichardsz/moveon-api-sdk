package edu.utec.sdk.moveon.core.request.search;

public class SearchRequest {

  private String method;
  private String entity;
  private String action;
  private SearchPreferences searchPreferences;
  private SearchFilter searchFilters;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getEntity() {
    return entity;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public SearchPreferences getSearchPreferences() {
    return searchPreferences;
  }

  public void setSearchPreferences(SearchPreferences searchPreferences) {
    this.searchPreferences = searchPreferences;
  }

  public SearchFilter getSearchFilters() {
    return searchFilters;
  }

  public void setSearchFilters(SearchFilter searchFilters) {
    this.searchFilters = searchFilters;
  }

}

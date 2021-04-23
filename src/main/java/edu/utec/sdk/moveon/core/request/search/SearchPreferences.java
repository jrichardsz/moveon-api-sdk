package edu.utec.sdk.moveon.core.request.search;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchPreferences {

  private String filters;
  private String visibleColumns;
  private String locale;
  private String sortName;
  private String sortOrder;
  private String search;
  private String page;
  private String rows;

  public String getFilters() {
    return filters;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  private String sidx;
  private String sord;

  public String getVisibleColumns() {
    return visibleColumns;
  }

  public void setVisibleColumns(String visibleColumns) {
    this.visibleColumns = visibleColumns;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getSortName() {
    return sortName;
  }

  public void setSortName(String sortName) {
    this.sortName = sortName;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  @JsonProperty("_search")
  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }

  public String getSidx() {
    return sidx;
  }

  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  public String getSord() {
    return sord;
  }

  public void setSord(String sord) {
    this.sord = sord;
  }
}

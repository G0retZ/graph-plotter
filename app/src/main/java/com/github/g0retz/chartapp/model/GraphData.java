package com.github.g0retz.chartapp.model;

import java.util.List;

public class GraphData {

  public final List<Long> domain;
  public final List<Graph> coDomain;

  public GraphData(List<Long> domain, List<Graph> coDomain) {
    if (coDomain == null || coDomain.isEmpty()) {
      throw new IllegalArgumentException(String.format("Codomain is empty: %s", coDomain));
    }
    if (domain == null || domain.isEmpty()) {
      throw new IllegalArgumentException(String.format("Domain is absent: %s", domain));
    }
    long tmp = Long.MIN_VALUE;
    for (long value : domain) {
      if (value < tmp) {
        throw new IllegalArgumentException(
            String.format("Domain values are not consistent: ...%d, %d...", tmp, value)
        );
      }
      tmp = value;
    }
    this.domain = domain;
    this.coDomain = coDomain;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GraphData graphData = (GraphData) o;

    if (!domain.equals(graphData.domain)) {
      return false;
    }
    return coDomain.equals(graphData.coDomain);
  }

  @Override
  public int hashCode() {
    int result = domain.hashCode();
    result = 31 * result + coDomain.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "GraphData{" +
        "domain=" + domain +
        ", coDomain=" + coDomain +
        '}';
  }
}

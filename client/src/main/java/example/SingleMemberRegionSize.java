package example;

public class SingleMemberRegionSize {

  private String regionName;

  private long size;

  private String histogram;

  public SingleMemberRegionSize() {}

  public SingleMemberRegionSize(String regionName, long size, String histogram) {
    this.regionName = regionName;
    this.size = size;
    this.histogram = histogram;
  }

  public String getRegionName() {
    return this.regionName;
  }

  public long getSize() {
    return this.size;
  }

  public String getHistogram() {
    return this.histogram;
  }

  public String toString() {
    return new StringBuilder()
      .append(getClass().getSimpleName())
      .append("[")
      .append("regionName=")
      .append(this.regionName)
      .append("; size=")
      .append(this.size)
      .append("; histogram=")
      .append(this.histogram)
      .append("]")
      .toString();
  }

}

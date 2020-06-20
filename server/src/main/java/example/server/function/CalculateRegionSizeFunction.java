package example.server.function;

import example.SingleMemberRegionSize;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Declarable;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

import org.apache.geode.internal.size.ObjectGraphSizer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateRegionSizeFunction implements Function, Declarable {
  
  @Override
  public void execute(FunctionContext context) {
    // Get the region names argument
    Object[] arguments = (Object[]) context.getArguments();
    String regionNamesArg = (String) arguments[0];
    String[] regionNames = regionNamesArg.split(",");
    context.getCache().getLogger().info("Executing function=" + getId() + "; regionNames=" + Arrays.toString(regionNames));

    // Iterate each input region
    List<SingleMemberRegionSize> regionSizes = new ArrayList<>();
    for (String regionName : regionNames) {
      // Get the region
      Region region = context.getCache().getRegion(regionName);

      // Calculate the region size
      if (region == null) {
        context.getCache().getLogger().warning("Region " + region + " doesn't exist so its size cannot be calculated");
      } else {
        context.getCache().getLogger().info("Calculating size of " + region);
        SingleMemberRegionSize regionSize = calculateSize(context.getCache(), region);
        context.getCache().getLogger().info("Size of " + region.getName() + " is " + NumberFormat.getInstance().format(regionSize.getSize())+ " bytes");
        context.getCache().getLogger().info("Histogram for " + region.getName() + " is:\n" + regionSize.getHistogram());
        regionSizes.add(regionSize);
      }
    }

    // Send the region sizes response
    context.getResultSender().lastResult(regionSizes);
  }

  private SingleMemberRegionSize calculateSize(Cache cache, Region region) {
    SingleMemberRegionSize size = null;
    ObjectGraphSizer.ObjectFilter filter = new RegionObjectFilter(cache);
    try {
      long regionSize = ObjectGraphSizer.size(region, filter, false);
      String regionHistogram = ObjectGraphSizer.histogram(region, filter, false);
      size = new SingleMemberRegionSize(region.getName(), regionSize, regionHistogram);
    } catch (Exception e) {
      cache.getLogger().warning("Caught the following exception attempting to calculate the size of region=" + region.getFullPath(), e);
    }
    return size;
  }

  @Override
  public String getId() {
    return getClass().getSimpleName();
  }
}

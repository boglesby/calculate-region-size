package example.server.function;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.DiskStore;

import org.apache.geode.cache.control.ResourceManager;

import org.apache.geode.distributed.DistributedLockService;
import org.apache.geode.distributed.DistributedMember;
import org.apache.geode.distributed.DistributedSystem;

import org.apache.geode.distributed.internal.ClusterDistributionManager;
import org.apache.geode.internal.OneTaskOnlyExecutor;
import org.apache.geode.internal.cache.CachePerfStats;
import org.apache.geode.internal.cache.DiskRegionStats;
import org.apache.geode.internal.cache.PartitionedRegionStats;

import org.apache.geode.internal.cache.persistence.PersistentMemberManager;

import org.apache.geode.internal.cache.eviction.HeapLRUStatistics;

import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RegionObjectFilter implements ObjectFilter {

  private final Cache cache;

  public RegionObjectFilter(Cache cache) {
    this.cache = cache;
  }
  
	private boolean logAllClasses = false;
	
	private boolean logRejectedClasses = false;
	
	private boolean logAcceptedClasses = false;
	
	public boolean accept(Object parent, Object object) {
		boolean accept = true;
		if (object instanceof Cache
			|| object instanceof CachePerfStats
			|| object instanceof Class
			|| object instanceof ClusterDistributionManager
			|| object instanceof DiskRegionStats
			|| object instanceof DiskStore
			|| object instanceof DistributedLockService
			|| object instanceof DistributedMember
			|| object instanceof DistributedSystem
			|| object instanceof Logger
			|| object instanceof HeapLRUStatistics
			|| object instanceof OneTaskOnlyExecutor
			|| object instanceof PartitionedRegionStats
			|| object instanceof PersistentMemberManager
			|| object instanceof ResourceManager
			|| object instanceof ScheduledThreadPoolExecutor) {
			if (this.logAllClasses || this.logRejectedClasses) {
        logObject(parent, object, "Rejecting");
			}
			accept = false;
		} else {
			if (this.logAllClasses || this.logAcceptedClasses) {
        logObject(parent, object, "Accepting");
			}
		}
		return accept;
	}

	private void logObject(Object parent, Object object, String message) {
    String parentClassName = null;
    if (parent != null) {
      parentClassName = parent.getClass().getName();
    }
    StringBuilder builder = new StringBuilder();
    builder
      .append(message)
      .append(" object=")
      .append(object)
      .append(" objectIdentity=")
      .append(System.identityHashCode(object))
      .append("; objectClass=")
      .append(object.getClass().getName())
      .append("; parent=")
      .append(parent)
      .append(" parentIdentity=")
      .append(System.identityHashCode(parent))
      .append("; parentClass=")
      .append(parentClassName);
    this.cache.getLogger().info(builder.toString());
  }
}
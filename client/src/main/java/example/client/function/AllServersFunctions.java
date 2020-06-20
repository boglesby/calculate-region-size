package example.client.function;

import example.SingleMemberRegionSize;
import org.apache.geode.distributed.DistributedMember;
import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnServers;

import java.util.List;
import java.util.Map;

@OnServers(resultCollector = "allServersResultCollector")
public interface AllServersFunctions {

  @FunctionId("CalculateRegionSizeFunction")
  Map<DistributedMember, List<SingleMemberRegionSize>> calculateRegionSize(String regionName);
}

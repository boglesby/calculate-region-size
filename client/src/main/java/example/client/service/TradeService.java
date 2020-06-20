package example.client.service;

import example.SingleMemberRegionSize;
import example.client.domain.CusipHelper;
import example.client.domain.Trade;
import example.client.function.AllServersFunctions;
import example.client.repository.PartitionedTradeRepository;
import example.client.repository.ReplicatedTradeRepository;
import org.apache.geode.distributed.DistributedMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TradeService {

  @Autowired
  protected ReplicatedTradeRepository replicatedRepository;

  @Autowired
  protected PartitionedTradeRepository partitionedRepository;

  @Autowired
  protected AllServersFunctions allServersFunctions;

  protected static final Random random = new Random();

  protected static final Logger logger = LoggerFactory.getLogger(TradeService.class);

  public void put(int numEntries, int entrySize) {
    logger.info("Putting {} trades of size {} bytes", numEntries, entrySize);
    for (int i=0; i<numEntries; i++) {
      long createTime = System.currentTimeMillis();
      Trade trade = new Trade(String.valueOf(i), CusipHelper.getCusip(), random.nextInt(100), new BigDecimal(BigInteger.valueOf(random.nextInt(100000)), 2), new byte[entrySize], createTime, createTime);
      this.replicatedRepository.save(trade);
      this.partitionedRepository.save(trade);
      logger.info("Saved " + trade);
    }
  }

  public void calculateRegionSize(String regionName) {
    Map<DistributedMember,List<SingleMemberRegionSize>> regionSizes = this.allServersFunctions.calculateRegionSize(regionName);
    logRegionSizes(regionName, regionSizes);
  }

  private void logRegionSizes(String regionName, Map<DistributedMember,List<SingleMemberRegionSize>> regionSizes) {
    StringBuilder builder = new StringBuilder();
    String[] regionNames = regionName.split(",");
    builder.append("\nMember sizes for region").append(regionNames.length == 1 ? " " : "s ").append(Arrays.toString(regionNames)).append(":\n\n");
    String regionSizesStr = regionSizes.entrySet()
      .stream()
      .map(entry -> getMemberSizes(entry))
      .collect(Collectors.joining("\n\n", builder.toString(), ""));
    logger.info(regionSizesStr);
  }

  private String getMemberSizes(Map.Entry<DistributedMember,List<SingleMemberRegionSize>> entry) {
    StringBuilder builder = new StringBuilder();
    builder.append("member=").append(entry.getKey()).append("\n\t");
    String memberRegionSizes = entry.getValue()
      .stream()
      .map(size -> getMemberSizes(size))
      .collect(Collectors.joining("\n\t", builder.toString(), ""));
    return memberRegionSizes;
  }

  private String getMemberSizes(SingleMemberRegionSize size) {
    return new StringBuilder()
      .append("regionName=")
      .append(size.getRegionName())
      .append("; size=")
      .append(NumberFormat.getInstance().format(size.getSize()))
      .append(" bytes")
      .toString();
  }
}

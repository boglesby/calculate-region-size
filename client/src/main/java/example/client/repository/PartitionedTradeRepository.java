package example.client.repository;

import example.client.domain.Trade;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;

@Region("PartitionedTrade")
public interface PartitionedTradeRepository extends GemfireRepository<Trade, String> {
}
package example.client;

import example.client.service.TradeService;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.geode.boot.autoconfigure.ContinuousQueryAutoConfiguration;

import java.util.List;

@SpringBootApplication(exclude = ContinuousQueryAutoConfiguration.class) // disable subscriptions
public class Client {

  @Autowired
  private TradeService service;

  public static void main(String[] args) {
    new SpringApplicationBuilder(Client.class)
      .build()
      .run(args);
  }

  @Bean
  ApplicationRunner runner() {
    return args -> {
      List<String> operations = args.getOptionValues("operation");
      String operation = operations.get(0);
      String parameter1 = (args.containsOption("parameter1")) ? args.getOptionValues("parameter1").get(0) : null;
      switch (operation) {
        case "load-regions":
          this.service.put(Integer.parseInt(parameter1), 1024);
          break;
      case "calculate-region-size":
        this.service.calculateRegionSize(parameter1);
        break;
    }};
  }

  @Bean("ReplicatedTrade")
  ClientRegionFactoryBean replicatedTradeRegion(GemFireCache cache) {
    ClientRegionFactoryBean<?, ?> regionFactory = new ClientRegionFactoryBean<>();
    regionFactory.setCache(cache);
    regionFactory.setShortcut(ClientRegionShortcut.PROXY);
    return regionFactory;
  }


  @Bean("PartitionedTrade")
  ClientRegionFactoryBean partitionedTradeRegion(GemFireCache cache) {
    ClientRegionFactoryBean<?, ?> regionFactory = new ClientRegionFactoryBean<>();
    regionFactory.setCache(cache);
    regionFactory.setShortcut(ClientRegionShortcut.PROXY);
    return regionFactory;
  }
}

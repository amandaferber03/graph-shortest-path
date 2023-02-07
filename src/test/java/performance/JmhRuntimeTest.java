
package performance;

import hw8.Config;
import hw8.graph.Graph;
import hw8.spp.StreetSearcher;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import performance.profiler.GcProfiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class JmhRuntimeTest {

  private File data;
  private HashMap<String, String[]> endpoints;
  private Graph<String, String> graph;
  private StreetSearcher sst;

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(JmhRuntimeTest.class.getSimpleName())
        .addProfiler(GcProfiler.class)
        .build();

    new Runner(opt).run();
  }

  @Setup(Level.Invocation)
  public void setUp() {
    StreetSearcher.VERBOSE = false;

    endpoints = new HashMap<>();
    endpoints.put("JHU to Druid Lake", new String[]{"-76.6175,39.3296", "-76.6383,39.3206"});
    endpoints.put("7-11 to Druid Lake", new String[]{"-76.6214,39.3212", "-76.6383,39.3206"});
    endpoints.put("Inner Harbor to JHU", new String[]{"-76.6107,39.2866", "-76.6175,39.3296"});

    data = new File(Config.class.getResource("/" + "baltimore.streets.txt").getFile());

    graph = Config.getGraph();
    sst = Config.getStreetSearcher(graph);
    try {
      sst.loadNetwork(data);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Benchmark
  @Fork(value = 2, warmups = 2)
  @Warmup(iterations = 2)
  @Measurement(iterations = 10)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void findShortestPath(Blackhole blackhole, BenchmarkState state) {
    sst.findShortestPath(endpoints.get(state.endPointKey)[0], endpoints.get(state.endPointKey)[1]);
    blackhole.consume(graph);
    blackhole.consume(sst);
  }

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    @Param({"JHU to Druid Lake", "7-11 to Druid Lake", "Inner Harbor to JHU"})
    public String endPointKey;
  }
}



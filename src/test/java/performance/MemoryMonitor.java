package performance;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryMonitor {

  public static long getReallyUsedMemory() {
    long before;
    long current = getGcCount();
    System.gc();
    do {
      before = current;
      current = getGcCount();
    } while (current == before);
    return getCurrentlyUsedMemory();
  }

  private static long getGcCount() {
    long sum = 0;
    for (GarbageCollectorMXBean b : ManagementFactory.getGarbageCollectorMXBeans()) {
      long count = b.getCollectionCount();
      if (count != -1) {
        sum = sum + count;
      }
    }
    return sum;
  }

  public static long getCurrentlyUsedMemory() {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
    MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
    return heapMemoryUsage.getUsed() + nonHeapMemoryUsage.getUsed();
  }
}

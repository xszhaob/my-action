package java8.chapter07;

import junit.framework.Assert;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 文件描述：
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 18/6/26
 */
public class ParallelInAction {

    @Test
    public void testAll() {
        long count = 30_000_000;
        long sequentialSum = measureSumPerf(ParallelInAction::sequentialSum, count);
        System.out.println("sequentialSum fastest -> " + sequentialSum);
        long iterativeSum = measureSumPerf(ParallelInAction::iterativeSum, count);
        System.out.println("iterativeSum fastest -> " + iterativeSum);
//        long parallelSum = measureSumPerf(ParallelInAction::parallelSum, count);
//        System.out.println("parallelSum fastest -> " + parallelSum);
//        long sideEffectSum = measureSumPerf(ParallelInAction::sideEffectSum, count);
//        System.out.println("sideEffectSum fastest -> " + sideEffectSum);
        long rangeSum = measureSumPerf(ParallelInAction::rangeSum, count);
        System.out.println("rangeSum fastest -> " + rangeSum);
        long rangeParallelSum = measureSumPerf(ParallelInAction::rangeParallelSum, count);
        System.out.println("rangeParallelSum fastest -> " + rangeParallelSum);
    }

    private long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (long i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            Long apply = adder.apply(n);
            long duration = System.currentTimeMillis() - start;
            fastest = fastest > duration ? duration : fastest;
            Assert.assertTrue(apply == 450000015000000L);
        }
        return fastest;
    }

    /**
     * 这相当令人失望，求和方法的并行版本比顺序版本要慢很多。
     * 你如何解释这个意外的结果呢？这里实际上有两个问题：
     * 1)iterate生成的是装箱的对象，必须拆箱成数字才能求和；
     * 2)我们很难把iterate分成多个独立块来并行执行。
     * 第二个问题更有意思一点，因为你必须意识到某些流操作比其他操作更容易并行化。
     * 具体来说，iterate很难分割成能够独立执行的小块，
     * 因为每次应用这个函数都要依赖前一次应用的结果。
     */
    private static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(0L, Long::sum);
    }

    private static long iterativeSum(long n) {
        long result = 0;
        for (long i = 1; i <= n; i++) {
            result += i;
        }
        return result;
    }

    private static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
    }

    /**
     * 由于多个线程在同时访问累加器，执行total += value，
     * 而这一句虽然看似简单，却不是一个原子操作。
     * 问题的根源在于，forEach中调用的方法有副作用，
     * 它会改变多个线程共享的对象的可变状态。
     * 要是你想用并行Stream又不想引发类似的意外，
     * 就必须避免这种情况。共享可变状态会影响并行流以及并行计算。
     */
    private static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    private static class Accumulator {
        long total = 0;

        public void add(long value) {
            total += value;
        }
    }

    private static long rangeParallelSum(long n) {
        return LongStream.rangeClosed(1, n).parallel().reduce(0, Long::sum);
    }

    private static long rangeSum(long n) {
        return LongStream.rangeClosed(1, n).reduce(0, Long::sum);
    }
}

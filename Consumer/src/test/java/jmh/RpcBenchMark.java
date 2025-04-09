package jmh;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.better.ClientApplication;
import com.better.client.Netty.NettyClient;
import com.better.codec.SharableMessageCodec;
import com.better.controller.HelloController;
import com.better.handler.RpcResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.All})
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
//测量次数,每次测量的持续时间
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(3000)
@Fork(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class RpcBenchMark {
    private  HelloController helloController;
    private  AnnotationConfigApplicationContext context;
    private NettyClient nettyClient;

    static {
        // 初始化时设置 NettyRpcClient 和 RpcResponseHandler 的日志类级别为 OFF，及关闭日志打印
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger clientLogger = loggerContext.getLogger(NettyClient.class);
        clientLogger.setLevel(ch.qos.logback.classic.Level.OFF);
        Logger handlerLogger = loggerContext.getLogger(RpcResponseHandler.class);
        handlerLogger.setLevel(Level.OFF);
        Logger codecLogger = loggerContext.getLogger(SharableMessageCodec.class);
        codecLogger.setLevel(Level.OFF);
    }

    @Setup
    public void init() {
        context = new AnnotationConfigApplicationContext(ClientApplication.class);
        helloController = context.getBean("helloController", HelloController.class);
        nettyClient = context.getBean(NettyClient.class);
    }
    @TearDown
    public void finish(){
        nettyClient.endLoop();
        context.close();
    }


    @Benchmark
    public void testSayHello() {
        helloController.hello("zhangsan");
    }

    public static void main(String[] args) throws RunnerException {
        log.info("测试开始");
        Options opt = new OptionsBuilder()
                .include(RpcBenchMark.class.getSimpleName())
                // 可以通过注解注入
//                .warmupIterations(3)
//                .warmupTime(TimeValue.seconds(10))
                // 报告输出
                .result("result.json")
                // 报告格式
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }


}

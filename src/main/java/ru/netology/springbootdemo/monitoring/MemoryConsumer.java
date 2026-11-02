package ru.netology.springbootdemo.monitoring;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Намеренно «съедает» оперативную память: раз в interval-ms
 */
@Component
public class MemoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(MemoryConsumer.class);

    private final List<byte[]> storage = new ArrayList<>();

    private final int chunkMb;
    private final int maxMb;

    public MemoryConsumer(MeterRegistry registry,
                          @Value("${memory.consumer.chunk-mb}") int chunkMb,
                          @Value("${memory.consumer.max-mb}") int maxMb) {
        this.chunkMb = chunkMb;
        this.maxMb = maxMb;
        // Своя метрика: сколько памяти приложение занимает намеренно (app_memory_allocated_bytes в Prometheus)
        Gauge.builder("app.memory.allocated", this, MemoryConsumer::allocatedBytes)
                .baseUnit("bytes")
                .description("Сколько памяти намеренно занято приложением")
                .register(registry);
    }

    @Scheduled(fixedDelayString = "${memory.consumer.interval-ms}")
    public void consume() {
        synchronized (storage) {
            if (allocatedMb() >= maxMb) {
                return;
            }
            byte[] block = new byte[chunkMb * 1024 * 1024];
            // Заполняем, чтобы память реально была занята, а не только зарезервирована
            Arrays.fill(block, (byte) 1);
            storage.add(block);
            log.info("Занято памяти: {} МБ из {} МБ", allocatedMb(), maxMb);
        }
    }

    public double allocatedBytes() {
        synchronized (storage) {
            return (double) allocatedMb() * 1024 * 1024;
        }
    }

    private long allocatedMb() {
        return (long) storage.size() * chunkMb;
    }
}

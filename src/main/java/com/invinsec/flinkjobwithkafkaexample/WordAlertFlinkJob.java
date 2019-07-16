package com.invinsec.flinkjobwithkafkaexample;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;


public class WordAlertFlinkJob {

  final static private String EVENT_QUEUE_NAME = "events";
  final static private String CONTROL_QUEUE_NAME = "controls";

  public static void main(String[] args) throws Exception {

//    final int port;
//    try {
//      final ParameterTool params = ParameterTool.fromArgs(args);
//      port = params.getInt("port");
//    } catch (Exception e) {
//      System.err.println("No port specified. Please run 'WordAlertFlinkJob --port <port>'");
//      return;
//    }

    // get the execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    final String kafkaServer = "localhost:9092";

    // get input data by connecting to the socket
    DataStreamSource eventsSource = env.addSource(getKafkaStreamConsumer(EVENT_QUEUE_NAME, kafkaServer));
    eventsSource.name("events").uid("events-source");

    DataStreamSource controlSource = env.addSource(getKafkaStreamConsumer(CONTROL_QUEUE_NAME, kafkaServer));
    controlSource.name("controls").uid("controls-source");


//    DataStream<String> text = env.socketTextStream("localhost", port, "\n");

      /*
      this that this:23 these:2
       */

//    env.

//    // parse the data, group it, window it, and aggregate the counts
//    DataStream<WordWithCount> windowCounts = text
//        .flatMap(
//            new FlatMapFunction<String, WordWithCount>() {
//
//              @Override
//              public void flatMap(String value, Collector<WordWithCount> out) {
//                for (String word : value.split("\\s")) {
//                  out.collect(new WordWithCount(word, 1L));
//                }
//              }
//            }
//        )
//        .keyBy("word")
//        .timeWindow(Time.seconds(5), Time.seconds(1))
//        .reduce(new ReduceFunction<WordWithCount>() {
//          @Override
//          public WordWithCount reduce(WordWithCount a, WordWithCount b) {
//            return new WordWithCount(a.word, a.count + b.count);
//          }
//        });

//    // print the results with a single thread, rather than in parallel
//    windowCounts.print().setParallelism(1);

    env.execute("Socket Window WordCount");
  }

  private static FlinkKafkaConsumer getKafkaStreamConsumer(String topic, String server) {

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", server);

    FlinkKafkaConsumer kafka = new FlinkKafkaConsumer<>(
        topic,
        new SimpleStringSchema(),
        properties
    );

    return kafka;
  }

  // Data type for words with count
  public static class WordWithCount {

    public String word;
    public long count;

    public WordWithCount() {
    }

    public WordWithCount(String word, long count) {
      this.word = word;
      this.count = count;
    }

    @Override
    public String toString() {
      return word + " : " + count;
    }
  }
}




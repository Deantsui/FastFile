package cn.dreamcatchers.fastfile.config;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.netty.NettyStreamFactoryFactory;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

/**
 * Created by DeanTsui on 2018/9/5.
 */
@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    private final Environment environment;

    @Autowired
    public MongoConfig(Environment environment) {
        this.environment = environment;
    }

    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString = new ConnectionString(environment
                .getProperty("fastFile.mongo.connectionString", "localhost:27017"));
        return MongoClients.create(MongoClientSettings.builder()
                .streamFactoryFactory(NettyStreamFactoryFactory.builder()
                        .eventLoopGroup(eventLoopGroup)
                        .build())
                .clusterSettings(ClusterSettings.builder()
                        .applyConnectionString(connectionString)
                        .build())
                .build());
    }

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("fastFile.mongo.landingDatabase", "fastfile");
    }

    @Bean
    public GridFSBucket gridFsTemplate(MongoClient reactiveMongoClient) throws Exception {
        return GridFSBuckets.create(reactiveMongoClient.getDatabase(getDatabaseName()));
    }

}
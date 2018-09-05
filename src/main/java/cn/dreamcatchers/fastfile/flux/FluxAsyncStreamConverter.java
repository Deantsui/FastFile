package cn.dreamcatchers.fastfile.flux;

import com.mongodb.reactivestreams.client.gridfs.AsyncInputStream;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
/**
 * https://github.com/cyclic-reference/mongo-images/blob/master/web-service/src/main/java/io/acari/images/flux/FluxAsyncStreamConverter.java
 */
public class FluxAsyncStreamConverter {

    public static AsyncInputStream convert(Flux<DataBuffer> source){
        return new FluxAsyncInputStream(source);
    }
}
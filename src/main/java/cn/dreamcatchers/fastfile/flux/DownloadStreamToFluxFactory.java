package cn.dreamcatchers.fastfile.flux;

import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;


/**
 * https://github.com/cyclic-reference/mongo-images/blob/master/web-service/src/main/java/io/acari/images/flux/DownloadStreamToFluxFactory.java
 */
public class DownloadStreamToFluxFactory {

    public Flux<byte[]> convert(GridFSDownloadStream gridFSDownloadStream) {
        return Flux.create(synchronousSink -> readStream(gridFSDownloadStream, synchronousSink), FluxSink.OverflowStrategy.BUFFER);
    }

    private void readStream(GridFSDownloadStream gridFSDownloadStream, FluxSink<byte[]> synchronousSink) {
        ByteBuffer allocate = ByteBuffer.allocate(512000);
        Mono.from(gridFSDownloadStream.read(allocate))
                .subscribe(bytesRead -> {
                    if (finishedReading(bytesRead)) {
                        Mono.from(gridFSDownloadStream.close())
                                .subscribe(a -> {}, throwable -> {}, synchronousSink::complete);
                    } else {
                        synchronousSink.next(allocate.array());
                        readStream(gridFSDownloadStream, synchronousSink);
                    }
                }, throwable -> {
                    synchronousSink.complete();
                });
    }

    private boolean finishedReading(Integer read) {
        return read < 0;
    }
}
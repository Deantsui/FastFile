package cn.dreamcatchers.fastfile.controller;

import cn.dreamcatchers.fastfile.flux.DownloadStreamToFluxFactory;
import cn.dreamcatchers.fastfile.flux.FluxAsyncStreamConverter;
import cn.dreamcatchers.fastfile.model.File;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Created by DeanTsui on 2018/9/4.
 */
@Slf4j
@RestController
@AllArgsConstructor
public class FileController {
    //----
    private final GridFSBucket gridFSBucket;
    //----
    private final DownloadStreamToFluxFactory downloadStreamToFluxFactory = new DownloadStreamToFluxFactory();

    /**
     * upload handler method.
     */
    @PostMapping("/upload")
    public Mono<ResponseEntity> fileUpload(@RequestPart("file") FilePart filePart) throws IOException {
        log.info("receive file");
        return Mono.from(gridFSBucket.uploadFromStream(filePart.filename(), FluxAsyncStreamConverter.convert(filePart.content())))
                .map(ObjectId::toHexString)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/")
    public Flux<File> findAll() {
        return Flux.from(gridFSBucket.find()).map(gridFSFile -> {
            var id = gridFSFile.getId()
                    .asObjectId()
                    .getValue()
                    .toHexString();
            return new File(id,gridFSFile.getFilename(), LocalDateTime.ofInstant(gridFSFile.getUploadDate().toInstant(), ZoneId.systemDefault()));
        });

    }

    @GetMapping("/{id}")
    public Flux<byte[]> fetchFile(@PathVariable String id) {
        return downloadStreamToFluxFactory
                .convert(gridFSBucket.openDownloadStream(new ObjectId(id)));
    }
}

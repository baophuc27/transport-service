package com.reeco.transport.adapter.out;

import com.reeco.transport.application.port.out.BatchingFilePort;
import com.reeco.transport.utils.annotators.Adapter;
import com.reeco.transport.infrastructure.MainStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;


@Adapter
@RequiredArgsConstructor
public class BatchingFileAdapter implements BatchingFilePort {

    private final MainStorage mainStorage;

    @Value("${application.file-directory}")
    private String FILE_DIRECTORY;


    @Override
    public void batchingFile() throws IOException {
        Stream.of(new File(FILE_DIRECTORY).listFiles())
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .forEach(dir -> mainStorage.addFile(FILE_DIRECTORY+dir));
                // Each directory represent for a device id.
    }

}

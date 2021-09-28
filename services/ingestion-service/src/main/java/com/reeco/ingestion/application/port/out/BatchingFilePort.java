package com.reeco.ingestion.application.port.out;


import java.io.IOException;

public interface BatchingFilePort {

    void batchingFile() throws IOException;
}

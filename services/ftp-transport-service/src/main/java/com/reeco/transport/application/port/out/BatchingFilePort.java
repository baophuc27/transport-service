package com.reeco.transport.application.port.out;


import java.io.IOException;

public interface BatchingFilePort {

    void batchingFile() throws IOException;
}

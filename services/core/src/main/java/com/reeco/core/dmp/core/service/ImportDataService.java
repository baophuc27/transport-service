package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.ResponseMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.List;

@Service
public class ImportDataService {

    public ResponseMessage recieveDataCsv(MultipartFile file, Long orgId) throws Exception{


            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = file.getBytes();

            ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream ));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] listLine = line.split(",");
                System.out.println(listLine[0]);
                System.out.println(listLine[1]);
            }
            br.close();
            return new ResponseMessage("haha");


    }
}

package com.zkx.fsrsvocab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.zkx.fsrsvocab.modules.auth.mapper"})
public class FsrsVocabApplication {

    public static void main(String[] args) {
        SpringApplication.run(FsrsVocabApplication.class, args);
    }
}
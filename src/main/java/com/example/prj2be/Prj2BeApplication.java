package com.example.prj2be;

import com.example.prj2be.mapper.SongMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class Prj2BeApplication {

  private final SongMapper songMapper;

  public static void main(String[] args) {
    SpringApplication.run(Prj2BeApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    // song 데이터를 조회 해서 그거를 메모리에 적재
    AllSongDTO.setSongList(songMapper.getAll());

    return args -> System.out.println("song 적재 완료.");
  }
}

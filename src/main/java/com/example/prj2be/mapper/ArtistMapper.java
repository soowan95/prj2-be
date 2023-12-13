package com.example.prj2be.mapper;

import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface ArtistMapper {
  @Select("""
  SELECT id
  FROM artist
  WHERE name = #{artistName} AND `group` = #{artistGroup}
  """)
  Integer getArtistCodeByNG(String artistName, String artistGroup);



  @Insert("""
insert into artist (picture) value (#{picture})
""")
  int insertArtistPhoto(Song song, MultipartFile file);
}
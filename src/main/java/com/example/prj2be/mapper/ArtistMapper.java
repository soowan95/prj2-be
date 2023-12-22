package com.example.prj2be.mapper;

import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface ArtistMapper {
  @Select("""
  SELECT id
  FROM artist
  WHERE name = #{artistName} AND `group` = #{artistGroup}
  """)
  Integer getArtistCodeByNG(String artistName, String artistGroup);
          
  @Select("""
  SELECT picture
  FROM artist
  WHERE id = #{artistCode}
  """)
    String getPictureByCode(Integer artistCode);

  @Update("""
  UPDATE artist
  SET picture = #{file}
  WHERE id = #{artistCode}
  """)
  void updatePicture(Integer artistCode, String file);
}
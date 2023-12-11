package com.example.prj2be.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArtistMapper {
  
  @Select("""
  SELECT id
  FROM artist
  WHERE name = #{artistName} AND `group` = #{artistGroup}
  """)
  Integer getArtistCodeByNG(String artistName, String artistGroup);
}
package com.example.prj2be.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("""
INSERT INTO artist(id, picture)
VALUES (#{id}, #{picture})
""")
    int insertArtist(Integer id, String picture);


    @Select("""

SELECT picture
FROM artist
WHERE id = #{id}
""")
    List<String> selectNamesByArtistId(Integer id);
}

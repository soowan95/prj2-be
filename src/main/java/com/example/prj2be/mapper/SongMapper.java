package com.example.prj2be.mapper;

import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongMapper {

  @Select("""
  SELECT s.title, s.genre, s.mood, s.id
  FROM song s JOIN songpoint sp ON s.title = sp.title AND s.artistName = sp.artistName
  ORDER BY sp.songPoint DESC 
  LIMIT 0, 100
  """)
  List<Song> getSongLimit100();

  @Select("""
  SELECT mainMood, id
  FROM mood
  """)
  List<Map<String, Object>> getMood();

  @Select("""
  SELECT genre, id
  FROM genre
  """)
  List<Map<String, Object>> getGenre();

  @Select("""
  <script>
  SELECT s.title, s.genre, s.mood, s.id
  FROM song s JOIN songpoint sp ON s.title = sp.title AND s.artistName = sp.artistName
  <trim prefix="WHERE" prefixOverrides="AND">
  <if test='genreIncludeList.length > 0'>
  s.genre
  <foreach collection="genreIncludeList" item="elem" open=" IN ( " separator="," close=")">
  #{elem}
  </foreach>
  </if>
  <if test='moodIncludeList.length > 0'>
  AND s.mood
  <foreach collection="moodIncludeList" item="elem" open=" IN ( " separator="," close=")">
  #{elem}
  </foreach>
  </if>
  </trim>
  ORDER BY sp.songPoint DESC 
  LIMIT 0, 100
  </script>
  """)
  List<Song> getByFilter(String[] genreIncludeList, String[] moodIncludeList);

  @Select("""
  <script>
  SELECT title, genre, mood, id
  FROM song
  WHERE 
  <if test='category == "가수"'>
  artistName
  </if>
  <if test='category == "제목"'>
  title
  </if>
  <if test='category == "가사"'>
  lyric
  </if>
   LIKE #{keyword}
  </script>
  """)
  List<Song> getByCategoryAndKeyword(String category, String keyword);

  @Select("""
  SELECT title, genre, mood, id
  FROM song
  WHERE (genre=#{genre} OR mood=#{mood}) AND id != #{id}
  """)
  List<Song> getByGenreAndMood(String genre, String mood, Integer id);
}

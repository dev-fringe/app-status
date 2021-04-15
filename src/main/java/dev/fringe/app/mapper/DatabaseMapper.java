package dev.fringe.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dev.fringe.app.model.RemainReq;
import dev.fringe.app.model.Ticks;

public interface DatabaseMapper {

	@Insert(""
	+"INSERT INTO REMAIN("
	+"      LIMITDATE "
	+"	, LIMITGROUP" 
	+"	, MINUTE" 
	+"	, SEQUENCE"
	+") VALUES("
	+"     #{id.limitdate}"
	+"    , #{id.limitgroup}" 
	+"	, #{minute} "
	+"	, #{sequence}" 
	+")"
	)
	void save(RemainReq remainReq);
	
	@Select("SELECT count(1) from REMAIN WHERE limitdate = #{id.limitdate} and LIMITGROUP = #{id.limitgroup}")
	boolean select(RemainReq remainReq);
  
	@Update(""
		+"	UPDATE REMAIN SET "
		+"	      MINUTE = #{minute} "
		+"	    , SEQUENCE = #{sequence} "  
		+"	WHERE "
		+"	      limitdate = #{id.limitdate} "
		+"	  and LIMITGROUP = #{id.limitgroup} ")
	void update(RemainReq req);

	@Delete("DELETE FROM QUICKSTART")
	void delete();

	
	@Select(""
			+"	SELECT TIMESTAMP, (SELECT MAX(trade_price) FROM Ticks B WHERE A.TIMESTAMP = B.TIMESTAMP) AS tradePrice, trade_volume AS tradeVolume   FROM ( "
			+" SELECT "
			+" timestamp, cast(sum(cast(trade_volume  AS DECIMAL)) AS VARCHAR) AS trade_volume "
		+" FROM "
+" 			Ticks u " 
		+" WHERE "
+" 			market = #{market} "
		+" GROUP BY timestamp "
		+" ) A "
		+" ORDER BY A.timestamp "
		+" 	 ASC " )
	List<Ticks> selectTick(@Param("market") String market, @Param("count") String count);

	
	@Select("Select * FROM QUICKSTART")
	List<Map> select2();
}

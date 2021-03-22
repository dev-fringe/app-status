import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.PrecisionNum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTest {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
//		String test = "{\"data\":70800000.00000000}";
//        Map res = mapper.readValue(test, Map.class);
//        System.out.println(res);
//        System.out.println();
//        
//        Timestamp ts=new Timestamp(1385911682);  
//        Date date=new Date(ts.getTime());
//        System.out.println(date);
//        
        
//        System.out.println(Instant.ofEpochMilli(new Long("1617328598000")).atZone(ZoneId.systemDefault()));
        
    	//Duration.ofDays(1), ZonedDateTime.now()
   	 	BarSeries series = new BaseBarSeriesBuilder().withNumTypeOf(PrecisionNum::valueOf).build();
        series.addBar(new BaseBar(Duration.ofDays(1), ZonedDateTime.now(), 1, 1, 1, 1, 1, 1, 1, DoubleNum::valueOf));
        System.out.println(series.getSeriesPeriodDescription());
	}
}

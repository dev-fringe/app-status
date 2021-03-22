package dev.fringe.app;

import lombok.SneakyThrows;

public class AStartUp  {
	
	@SneakyThrows
	public static void main(String[] args)  {
//		Db0Shell.main(args);
//		Db1Server.main(args);
//		Db1Console.main(args);
		Db2TableCreate.main(args);
		Db3LoadMarketByWeb.main(args);
		Db4LoadCandleDaysByWeb.main(args);
////		Db5LoadCandleMinutesByWeb.main(args);
//		Db6LoadTickByWeb.main(args);
//		Db6LoadTickByWeb.main(args);
//		Db6LoadTickByWeb.main(args);
//		Db6LoadTickByWeb.main(args);
//		Db7Scheduler.main(args);
	}
}

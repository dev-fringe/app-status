package dev.fringe.app;

import org.h2.tools.Shell;

import lombok.SneakyThrows;

public class Db0Shell {
	
	/**
	 * 	> java -cp h2-*.jar org.h2.tools.Shell
		Welcome to H2 Shell
		Exit with Ctrl+C
		[Enter]   jdbc:h2:mem:2
		URL       jdbc:h2:./test
		[Enter]   org.h2.Driver
		Driver
		[Enter]   sa
		User      your_username
		Password  (hidden)
		Type the same password again to confirm database creation.
		Password  (hidden)
		Connected
		sql> quit
		Connection closed
     */	
	@SneakyThrows
	public static void main(String[] args) {
		Shell.main("-url","jdbc:h2:tcp://localhost/~/test","-user","sa","-password","sa");		//jdbc:h2:./test  create database
	}
}

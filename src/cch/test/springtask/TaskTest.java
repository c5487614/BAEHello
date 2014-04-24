package cch.test.springtask;

import java.util.Date;

public class TaskTest implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.print(new Date());
	}
}
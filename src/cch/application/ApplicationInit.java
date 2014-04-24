package cch.application;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationInit implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
//		if(event.getApplicationContext().getParent()==null){
//			if(event.getApplicationContext().containsBean("taskExecutor")){
//				event.getApplicationContext().getBean("taskExecutor");
//				System.out.println("1234567890");
//			}
//		}
	}
	
	
	public void test(){
		Logger logger = Logger.getLogger("name");
		logger.log(Level.INFO, new Date().toString());
	}

}

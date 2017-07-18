package com.gin.design.observer;

public class Test {
	public static void main(String [] args){
		ConcreteSubject eatSubject = new ConcreteSubject();
		
		ConcreteObserver personOne = new ConcreteObserver();
		ConcreteObserver personTwo = new ConcreteObserver();
		
		eatSubject.addObserver(personOne);
		eatSubject.addObserver(personTwo);
		
		eatSubject.notifyObserver("吃饭了。。");
		
	}

}

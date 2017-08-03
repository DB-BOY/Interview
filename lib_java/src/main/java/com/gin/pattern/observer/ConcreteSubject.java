package com.gin.pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class ConcreteSubject implements Subject {
	private List<Observer> list = new ArrayList<Observer>();
	@Override
	public void addObserver(Observer observer) {
		list.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		list.remove(observer);

	}

	@Override
	public void notifyObserver(String str) {
		for(Observer observer: list){
			observer.update(str);
		}
	}

}

package com.example.uno;

import android.graphics.Bitmap;

public class Card {
	
	private int id;
	private String color;
	private String number;
	private Bitmap bmp;
	
	public Card(int newId, String newColor, String newNumber) {
		id = newId;
		color = newColor;
		number = newNumber;		
		}
	
	public void setBmp(Bitmap newBmp){
		bmp = newBmp;
	}
	
	public void setNumber(String newNumber){
		number = newNumber;
	}
	
	public void setColor(String newColor){
		color = newColor;
	}
	
	public Bitmap getBmp(){
		return bmp;
	}
	
	public String getNumber(){
		return number;
	}
	
	public String getColor(){
		return color;
	}
	
	public int getId(){
		return id;		
	}

}

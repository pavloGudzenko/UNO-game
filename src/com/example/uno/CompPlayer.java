package com.example.uno;

import java.util.List;

public class CompPlayer {
	
	public int makePlay(List<Card> compCards, String color, String number) {
			int play = 0;
			int wildCard = 0;
			for (int i = 0; i < compCards.size(); i++) {
			     int tempId = compCards.get(i).getId();
			     String tempColor = compCards.get(i).getColor();
			     String tempNumber = compCards.get(i).getNumber();  
			   if (color.equals(tempColor) || number.equals(tempNumber)) {
				play = tempId;			 
			   }
			   if (tempColor.equals("plus4")){wildCard = tempId;}
			   if (tempColor.equals("all")){wildCard = tempId;}
		}
			if (play == 0 && wildCard != 0){
				play = wildCard;				
			}
			return play;
		}

	
	public String chooseColor(List<Card> compCards) {
			String color = "red";
			int red = 0;
			int blue = 0;
			int yellow = 0;
			int green = 0;
			
			for (int i = 0; i < compCards.size(); i++) {
				String tempColor = compCards.get(i).getColor();	
				if (tempColor.equals(red)){
					red++;
				} else if (tempColor.equals(blue)){
					blue++;
				} else if (tempColor.equals(yellow)){
					yellow++;
				} else if (tempColor.equals(green)){
					green++;
				}
			}
			
			if (blue>red) color = "blue";
			if (yellow>blue) color = "yellow";
			if (green>yellow) color = "green"; 
			
			return color;
			}
	
	

				
				

}

package com.example.uno;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.util.Random;





import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
	private Context myContext;
	private List<Card> deckCards = new ArrayList<Card>();
	private List<Card> discardCards = new ArrayList<Card>();
	private List<Card> userCards = new ArrayList<Card>();
	private List<Card> compCards = new ArrayList<Card>();
	private final String [] colorsArray = {"red", "yellow", "blue", "green"}; 
	private final String [] numbersArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "skip", "turn", "plus2"};
	private final String [] wildCardsColorArray = {"all", "all"};
	private final String [] wildCardsNumberArray = {"all", "plus4"};
	private int scaledCardW;
	private int scaledCardH;
	private int screenW;
	private int screenH;
	private Bitmap cardBack;
	private Bitmap nextCardButton;
	private String validNumber = "";
	private String validColor = "";
	private boolean myTurn;
	private float scale;
	private Paint whitePaint;
	private int oppScore;
	private int myScore;
	private int movingCardIdx = -1;
	private int movingX;
	private int movingY;


	
	public GameView (Context context) {
		super(context);
		myContext = context;
		scale = myContext.getResources().getDisplayMetrics().density;
		//myTurn = new Random().nextBoolean();
		myTurn = true;
		
		whitePaint = new Paint(); 
		whitePaint.setAntiAlias(true); 
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Paint.Style.STROKE);
		whitePaint.setTextAlign(Paint.Align.LEFT);
		whitePaint.setTextSize(scale*15);
		}
	
		@Override
	protected void onDraw(Canvas canvas) { 			
			
			// drawing comp's hand
			for (int i = 0; i < compCards.size(); i++) {
				if (i < 10){
				canvas.drawBitmap(cardBack,	i*(scaledCardW+6), 25, null);
				}
			}
			
			// drawing pile
			for (int i = 0; i < 10; i++) {
				if (i < 10){
			canvas.drawBitmap(cardBack,	i*(scale*3), (screenH/2-scaledCardH/2), null);
				}
			}
			
			// draw discardCards
			canvas.drawBitmap(cardBack, (screenW/2)-cardBack.getWidth()-10, (screenH/2)-(cardBack.getHeight()/2), null);
			if (!discardCards.isEmpty()) {
				canvas.drawBitmap(discardCards.get(0).getBmp(), 
								  screenW/2-scaledCardW/2, 
								  (screenH/2-scaledCardH/2), 
								  null);
			}
			
			
//			Random random = new Random(); 
//			int startX = 20;
//			int startY = 20;
//			int betweenCardGap = (int) (screenW - scaledCardW*8 - 40)/7;
//			 for (int i = 0; i < 8; i++){
//			int randomCardId = random.nextInt(deckCards.size()-1);
//			canvas.drawBitmap(deckCards.get(randomCardId).getBmp(), startX, startY, null);
//			deckCards.remove(randomCardId);
//			startX = startX + scaledCardW + betweenCardGap;
//			 }
			
			// drawing user's hand
			for (int i = 0; i < userCards.size(); i++) {
				if (i == movingCardIdx) {
					canvas.drawBitmap(userCards.get(i).getBmp(), movingX, movingY, null);
					} else {				
				canvas.drawBitmap(userCards.get(i).getBmp(),
				i*(scaledCardW+6), (screenH-scaledCardH-25), null);
			  }
	        }
			
			invalidate();
		}
		
	public boolean onTouchEvent(MotionEvent event) { 
		int eventaction = event.getAction();
		int X = (int)event.getX();
		int Y = (int)event.getY();
		switch (eventaction ) {
		       case MotionEvent.ACTION_DOWN:
		    	   myTurn = true; // don't forget to remove!!!!!!!!!!! 
		           if (myTurn) { 
		              for (int i = 0; i < userCards.size(); i++) { 
		            	if (X > i*(scaledCardW+6) && X < i*(scaledCardW+6) + scaledCardW && 
		        			Y > screenH-scaledCardH-25) {
		                       movingCardIdx = i;
		                       movingX = X-(int)(30*scale);
		                       movingY = Y-(int)(70*scale);
		                    }
		                }
		             // i*(scaledCardW+6), (screenH-scaledCardH-25)
		             }
		break;
		case MotionEvent.ACTION_MOVE:
			movingX = X-(int)(30*scale);
			movingY = Y-(int)(70*scale);
		break;
		case MotionEvent.ACTION_UP:
			if (movingCardIdx > -1 &&  X > (screenW/2)-(100*scale) &&
			X < (screenW/2)+(100*scale) & Y > (screenH/2)-(100*scale) &&
			Y < (screenH/2)+(100*scale) &&	(userCards.get(movingCardIdx).getNumber() == validNumber ||
			userCards.get(movingCardIdx).getColor() ==	validColor) ||
			userCards.get(movingCardIdx).getColor() == "all") {
			validNumber = userCards.get(movingCardIdx).getNumber();
			validColor = userCards.get(movingCardIdx).getColor();
			discardCards.add(0, userCards.get(movingCardIdx));
			userCards.remove(movingCardIdx);
			}
			movingCardIdx = -1; 
		break;
		}
		invalidate();
		return true;
		}
	
    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
		Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.card_back);
        scaledCardW = (int) (screenW/12);
        scaledCardH = (int) (scaledCardW*1.55);
		cardBack = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false); 
		nextCardButton = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_next); 
        initCards();
        dealCards();
        drawCard(discardCards);
        validColor = discardCards.get(0).getColor();
        validNumber = discardCards.get(0).getNumber();
	    myTurn = new Random().nextBoolean();
		if (!myTurn) {
		//	makeComputerPlay();			
		}
    }
	
	private void initCards() {
		int id = 1;
		for (int i = 0; i < colorsArray.length; i++) {
			for (int j = 0; j < numbersArray.length; j++) {
			    id++;
				Card tempCard = new Card(id, colorsArray[i], numbersArray[j]);
			    int resourceId = getResources().getIdentifier(colorsArray[i] +"_"+ numbersArray[j], "drawable", myContext.getPackageName());
			    Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), resourceId);
		        scaledCardW = (int) (screenW/12);
		        scaledCardH = (int) (scaledCardW*1.55);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false); 
			 	tempCard.setBmp(scaledBitmap);
			 	deckCards.add(tempCard);
			}
		}
		for (int i = 0; i < wildCardsColorArray.length; i++) {
			for (int j = 0; j < wildCardsNumberArray.length; j++) {
			    id++;
				Card tempCard = new Card(id, wildCardsColorArray[i], wildCardsNumberArray[j]);
			    int resourceId = getResources().getIdentifier(wildCardsColorArray[i] +"_"+ wildCardsNumberArray[j], "drawable", myContext.getPackageName());
			    Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), resourceId);
		        scaledCardW = (int) (screenW/12);
		        scaledCardH = (int) (scaledCardW*1.55);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false); 
			 	tempCard.setBmp(scaledBitmap);
			 	deckCards.add(tempCard);
			}
		}
	}
	
	private void drawCard(List<Card> handToDraw) { 
		handToDraw.add(0, deckCards.get(0));
		deckCards.remove(0);
		if (deckCards.isEmpty()) { 
		for (int i = discardCards.size()-1; i > 0 ; i--) {
		deckCards.add(discardCards.get(i));
		discardCards.remove(i);
		Collections.shuffle(deckCards,new Random());
		}
		}
		}
	
	private void dealCards() {
		Collections.shuffle(deckCards,new Random()); 
		for (int i = 0; i < 8; i++) { 
		drawCard(userCards);
		drawCard(compCards);
		}
		}
}

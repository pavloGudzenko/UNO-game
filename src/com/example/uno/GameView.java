package com.example.uno;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.util.Random;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
	private int movingCardIdx;
	private int movingX;
	private int movingY;
	private CompPlayer computerPlayer = new	CompPlayer();


	
	public GameView (Context context) {
		super(context);
		myContext = context;
		scale = myContext.getResources().getDisplayMetrics().density;
		movingCardIdx = -1;
		
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
			if (movingCardIdx > -1 &&  
					   X > (screenW/2)-(100*scale) &&
					   X < (screenW/2)+(100*scale) &&
					   Y > (screenH/2)-(100*scale) &&
					   Y < (screenH/2)+(100*scale) &&	
					   (userCards.get(movingCardIdx).getNumber().equals(validNumber) ||
					   userCards.get(movingCardIdx).getColor().equals(validColor) ||
					   userCards.get(movingCardIdx).getColor().equals("all"))) {
			
			validNumber = userCards.get(movingCardIdx).getNumber();
			validColor = userCards.get(movingCardIdx).getColor();
			discardCards.add(0, userCards.get(movingCardIdx));
			userCards.remove(movingCardIdx);
			if (userCards.isEmpty()) {
				//endHand();
			} else {
			if (validColor == "all") {
				showChooseColorDialog();
			  } else {
				  myTurn = false;
				  checkForPenalty();
				  if (!myTurn){
				  makeComputerPlay();
				  }
			  }
			}
		} 
			
			if (movingCardIdx == -1 && myTurn && X > 0 && X < scaledCardH + (70*scale) && 
					Y > (screenH/2-scaledCardH/2)-(70*scale) && Y < (screenH/2+scaledCardH/2)+(70*scale)) {
					if (checkForValidDraw()) {
					drawCard(userCards);
					makeComputerPlay();
					} else {
					Toast.makeText(myContext, "You have cards for playing.", Toast.LENGTH_SHORT).show();
					}
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
		  makeComputerPlay();			
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
	
	private void showChooseColorDialog() {
		final Dialog chooseColorDialog = new Dialog(myContext);
		chooseColorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		chooseColorDialog.setContentView(R.layout.choose_color_dialog);
		
		Button redButton = (Button) chooseColorDialog.findViewById(R.id.redButton);
		redButton.setOnClickListener(new View.OnClickListener(){
		   public void onClick(View view){
		     validColor = "red";
		      chooseColorDialog.dismiss();
		       Toast.makeText(myContext, "You chose RED color", Toast.LENGTH_SHORT).show();
		       myTurn = false;
		       checkForPenalty();
		       if (!myTurn){
				  makeComputerPlay();
		       }
		}
		});
		
		Button greenButton = (Button) chooseColorDialog.findViewById(R.id.greenButton);
		greenButton.setOnClickListener(new View.OnClickListener(){
		   public void onClick(View view){
		     validColor = "green";
		      chooseColorDialog.dismiss();
		       Toast.makeText(myContext, "You chose GREEN color", Toast.LENGTH_SHORT).show();
		       myTurn = false;
		       checkForPenalty();
		       if (!myTurn){
				  makeComputerPlay();
		       }
		}
		});
		
		Button blueButton = (Button) chooseColorDialog.findViewById(R.id.blueButton);
		blueButton.setOnClickListener(new View.OnClickListener(){
		   public void onClick(View view){
		     validColor = "blue";
		      chooseColorDialog.dismiss();
		       Toast.makeText(myContext, "You chose BLUE color", Toast.LENGTH_SHORT).show();
		       myTurn = false;
		       checkForPenalty();
		       if (!myTurn){
				  makeComputerPlay();
		       }
		}
		});
		
		Button yellowButton = (Button) chooseColorDialog.findViewById(R.id.yellowButton);
		yellowButton.setOnClickListener(new View.OnClickListener(){
		   public void onClick(View view){
		     validColor = "yellow";
		      chooseColorDialog.dismiss();
		       Toast.makeText(myContext, "You chose YELLOW color", Toast.LENGTH_SHORT).show();
		       myTurn = false;
		       checkForPenalty();
		       if (!myTurn){
				  makeComputerPlay();
		       }
		}
		});
		chooseColorDialog.show();
		}
	
	
	private boolean checkForValidDraw() {
		boolean canDraw = true;
		for (int i = 0; i < userCards.size(); i++) {
		int tempId = userCards.get(i).getId();
		String tempNumber = userCards.get(i).getNumber();
		String tempColor = userCards.get(i).getColor();
		if (validColor.equals(tempColor) || validNumber.equals(tempNumber)
		|| validColor.equals("all")) {
		canDraw = false;
		}
		}
		return canDraw;
		}
	
	private void makeComputerPlay() {
		int tempPlay = -1;
			tempPlay = computerPlayer.makePlay(compCards, validColor, validNumber);
			if (tempPlay == -1) {
				drawCard(compCards);
			} else {
			  if (compCards.get(tempPlay).getColor().equals("all")) {
			     validColor = computerPlayer.chooseColor(compCards);
        	Toast.makeText(myContext, "Computer chose " + validColor, Toast.LENGTH_SHORT).show();
		} else {
			validColor = compCards.get(tempPlay).getColor();
			}
			validNumber = compCards.get(tempPlay).getNumber(); 
			discardCards.add(0, compCards.get(tempPlay));
			compCards.remove(tempPlay);
		}
			if (compCards.isEmpty()) {
				//endHand();
			}
			myTurn = true;
			checkForPenalty();
		 }
	
		
   private void checkForPenalty(){
	   if (myTurn && validNumber.equals("plus2")){
		   drawCard(userCards);
		   drawCard(userCards);
		   skipTurn();
	   } else if (myTurn && validNumber.equals("plus4")){
		   drawCard(userCards);
		   drawCard(userCards);
		   drawCard(userCards);
		   drawCard(userCards);
		   skipTurn();
	   } else if (myTurn && validNumber.equals("skip")){
		   skipTurn();	   
	   }	
	   
	   if (!myTurn && validNumber.equals("plus2")){
		   drawCard(compCards);
		   drawCard(compCards);
		   skipTurn();
	   } else if (!myTurn && validNumber.equals("plus4")){
		   drawCard(compCards);
		   drawCard(compCards);
		   drawCard(compCards);
		   drawCard(compCards);
		   skipTurn();
   } else if (!myTurn && validNumber.equals("skip")){
	   skipTurn();	   
   }
   }
   
   private void skipTurn(){
	   if (myTurn){
		   myTurn = false;
		   makeComputerPlay();
	   } else {
		   myTurn = true; 
	   }	   
   }
   
   }

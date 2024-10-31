import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards
{
    class Card{
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon){
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }
        
        public String toString(){
            return cardName;
        }
    }

    String[] cardList = {
        "darkness",
        "double",
        "fairy",
        "fighting",
        "fire",
        "grass",
        "lightning",
        "metal",
        "psychic",
        "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

int boardWidth = columns * cardWidth;
int boardHeight = columns * cardHeight;

JFrame frame = new JFrame("Pokemon Match Cards");

JLabel textLabel = new JLabel();
JPanel textPanel =  new JPanel();
JPanel boardPanel = new JPanel();
JPanel restartGamePanel = new JPanel();
JButton restartButton = new JButton();

int errorCount = 0;
ArrayList<JButton> board;
Timer hideCardTimer;
boolean gameReady = false;
JButton card1Selected;
JButton card2Selected;



 MatchCards(){
   setupCards();
   shuffleCards();

   frame.setVisible(true);
   frame.setLayout(new BorderLayout());
   frame.setSize(boardWidth,boardHeight);
   frame.setLocationRelativeTo(null);
   frame.setResizable(false);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   textLabel.setFont(new Font("Arial", Font.PLAIN,20));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setText("Errors: " + Integer.toString(errorCount));

    textPanel.setPreferredSize(new Dimension(boardWidth, 30));
    textPanel.add(textLabel);
    frame.add(textPanel,BorderLayout.NORTH);

    //card game board
    board = new ArrayList<JButton>();
    boardPanel.setLayout(new GridLayout(rows,columns));
    for (int i=0; i<cardSet.size(); i++){
        JButton title = new JButton();
        title.setPreferredSize(new Dimension(cardWidth,cardHeight));
        title.setOpaque(true);
        title.setIcon(cardSet.get(i).cardImageIcon);
        title.setFocusable(false);
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                JButton title = (JButton) e.getSource();
                if(title.getIcon() == cardBackImageIcon){
                    if(card1Selected == null){
                        card1Selected = title;
                        int index = board.indexOf(card1Selected);
                        card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                    }
                    else if (card2Selected == null){
                        card2Selected = title;
                        int index = board.indexOf(card2Selected);
                        card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                        if (card1Selected.getIcon() != card2Selected.getIcon()){
                            errorCount += 1;
                            textLabel.setText("Errors: " + Integer.toString(errorCount));
                            hideCardTimer.start();
                        }
                        else{
                            card1Selected = null;
                            card2Selected =null;
                        }
                    }
                }
            }
        });
        board.add(title);
        boardPanel.add(title);
        }
        frame.add(boardPanel);

        //restart game button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                shuffleCards();

                for (int i=0; i<board.size();i++){
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                errorCount =0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                hideCardTimer.start();

            }
        });

        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);
        
    frame.pack();
    frame.setVisible(true);

    //start game
    hideCardTimer = new Timer(1500,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            hideCards();
        }

    } );

    hideCardTimer.setRepeats(false);
    hideCardTimer.start();

 }   
void setupCards(){
    cardSet = new ArrayList<Card>();
    for(String cardName : cardList){
        //load image to card
        Image cardImg = new ImageIcon(getClass().getResource("./img/"+ cardName+".jpg ")).getImage();
        ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

        //create card object
           Card card = new Card(cardName, cardImageIcon);
           cardSet.add(card);     

    }
    cardSet.addAll(cardSet);

    Image cardBackImg = new ImageIcon(getClass().getResource("./img/back.jpg")).getImage();
    cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
}

void shuffleCards() {
    System.out.println(cardSet);
    
    for (int i = 0; i < cardSet.size(); i++) {
        int j = (int) (Math.random() * cardSet.size()); 
       
        Card temp = cardSet.get(i);
        cardSet.set(i, cardSet.get(j));
        cardSet.set(j, temp);
    }
    System.out.println(cardSet);
}

void hideCards() {
    
    if (gameReady && card1Selected != null && card2Selected != null) { //only flip 2 cards
        card1Selected.setIcon(cardBackImageIcon);
        card1Selected = null;
        card2Selected.setIcon(cardBackImageIcon);
        card2Selected = null;
    }
    else { //flip all cards face down
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardBackImageIcon);
        }
        gameReady = true;
        restartButton.setEnabled(true);
    }
}
}
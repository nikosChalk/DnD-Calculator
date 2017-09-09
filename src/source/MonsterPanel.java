/*
 * Copyright 2014 Nikos Chalkiadakis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package source;
import java.awt.Color;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import source.model.Monster;

/**
 * @author Nikos Chalkiadakis
 */
public class MonsterPanel extends JPanel {
    private static final ImageIcon IMAGE_LOCK_LOCKED = new ImageIcon(MonsterPanel.class.getResource("/media/lock_locked.png"));
    private static final ImageIcon IMAGE_LOCK_UNLOCKED = new ImageIcon(MonsterPanel.class.getResource("/media/lock_unlocked.png"));
    private static final String operationsOffset = "  ";
    
    /**
     * The monster which the panel contains.
     */
    private Monster monster;
    
    /**
     * The panel's listener. Handles all events except the deleteButton.
     */
    private MyListener listener;
    
    /**
     * Numeric buttons of the calculator.
     */
    private JButton[] numericButtonsArray;
    
    /**
     * Contains the textFields associated with the monster's attributes.
     */
    private JTextField[] variableTextFields;
    
    /**
     * Whether or not this JPanel is focused and therefor has its hotkeys enabled.
     */
    private boolean isFocused;
    
    

    //HP>=0 FORMAT: --HP-OP-VALUE-OP
    //HP<0  FORMAT: -HP-OP-VALUE-OP
    /**
     * Creates a new panel for displaying the monster's attributes.
     * @param monster The monster
     */
    public MonsterPanel(Monster monster, ActionListener deleteButtonListener) {
        initComponents();   /* Initialized GUI */
        listener = new MyListener(this);
        this.monster = monster;

        /* Grouping Buttons */
        numericButtonsArray = new JButton[10];
        numericButtonsArray[0] = button0;
        numericButtonsArray[1] = button1;
        numericButtonsArray[2] = button2;
        numericButtonsArray[3] = button3;
        numericButtonsArray[4] = button4;
        numericButtonsArray[5] = button5;
        numericButtonsArray[6] = button6;
        numericButtonsArray[7] = button7;
        numericButtonsArray[8] = button8;
        numericButtonsArray[9] = button9;
        
        variableTextFields = new JTextField[] {nameTextField, fullHPTextField};
        isFocused = false;
        
        /* Initialize Listeners */
        deleteButton.addActionListener(deleteButtonListener);
        registerListeners();
        
        /* Initializing GUI with monster's attributes */
        TitledBorder border = (TitledBorder)this.getBorder();
        border.setTitle("Monster_ID: " + monster.id);
        
        notesTextArea.setText(monster.getNotes());
        nameTextField.setText(monster.getName());
        fullHPTextField.setText("" + monster.getCurHP());
        calculationsTextArea.setText(((monster.getCurHP()<0) ? "" : " ") + monster.getCurHP());
        operationsTextField.setText(operationsOffset + monster.getCurHP());
    }
 
    /**
     * Constructor used by NetBeans to generate the "Design" GUI builder
     */
    public MonsterPanel() {
        initComponents();
    }
    
    /**
     * Registers the listeners for the buttons of this JPanel.
     */
    private void registerListeners() {
        for(JButton button : numericButtonsArray)
            button.addActionListener(listener);
        
        buttonClear.addActionListener(listener);
        buttonComma.addActionListener(listener);
        buttonEquals.addActionListener(listener);
        
        buttonAdd.addActionListener(listener);
        buttonSubstract.addActionListener(listener);
        buttonMultiply.addActionListener(listener);
        buttonDivide.addActionListener(listener);
        buttonBracketOpen.addActionListener(listener);
        buttonBracketClose.addActionListener(listener);
        
        variablesLockCheckBox.addActionListener(listener);
        deleteLockCheckBox.addActionListener(listener);
        clearDisplayButton.addActionListener(listener);
        addNotesButton.addActionListener(listener);
        
        nameTextField.addFocusListener(listener);
        fullHPTextField.addFocusListener(listener);
    }
    
    /**
     * Change the focus state of this panel. If this panel gets focus, then its hotkeys are enabled.
     * @param state True if focus should be on, false otherwise.
     */
    public void setFocused(boolean state) {
        isFocused = state;
        TitledBorder mainBorder = (TitledBorder)this.getBorder();
        enableHotKeys(state);
        if(state) {
            mainBorder.setBorder(BorderFactory.createEtchedBorder(Color.MAGENTA, Color.RED));
        } else {
            mainBorder.setBorder(BorderFactory.createEtchedBorder(null, Color.LIGHT_GRAY));
            if(!(variablesLockCheckBox.isSelected())) { //locking the variables when losing focuss.
                System.out.println("Custom calling to actionEvent due to having unlocked the variables and losing foucs.");
                variablesLockCheckBox.setSelected(true);    //LOCK.
                listener.actionPerformed(new ActionEvent(variablesLockCheckBox, ActionEvent.ACTION_PERFORMED, variablesLockCheckBox.getActionCommand()));  //creating customly the setSelected event. (san na ekane click to button).
            }
        }
    }
    
    /**
     * Enables or disables the hotkeys of this JPanel.
     * @param enable True if the panel's hotkeys should be enabled. False otherwise.
     */
    private void enableHotKeys(boolean enable) {
        for(int i=0; i<numericButtonsArray.length; i++) {
            JButton button = numericButtonsArray[i];
            if(enable && variablesLockCheckBox.isSelected())
                addButtonHotKey(button, KeyEvent.VK_0 + i, 0, "key_" + i);
            else
                removeButtonHotKey(button);
        }
        
        if(enable && variablesLockCheckBox.isSelected()) {
            System.out.println("enabled keys.");
            addButtonHotKey(buttonClear, KeyEvent.VK_C, 0, "erase");
            addButtonHotKey(buttonClear, KeyEvent.VK_BACK_SPACE, 0, "erase");
            addButtonHotKey(buttonComma, KeyEvent.VK_PERIOD, 0, "comma");

            addButtonHotKey(buttonEquals, KeyEvent.VK_ENTER, 0, "equals");
            addButtonHotKey(buttonEquals, KeyEvent.VK_EQUALS, 0, "equals");
            
            addButtonHotKey(buttonAdd, KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK, "add");
            addButtonHotKey(buttonSubstract, KeyEvent.VK_MINUS, 0, "substract");
            addButtonHotKey(buttonMultiply, KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK, "multiply");
            addButtonHotKey(buttonDivide, KeyEvent.VK_SLASH, 0, "divide");
            addButtonHotKey(buttonBracketOpen, KeyEvent.VK_9, KeyEvent.SHIFT_DOWN_MASK, "bracketOpen");
            addButtonHotKey(buttonBracketClose, KeyEvent.VK_0, KeyEvent.SHIFT_DOWN_MASK, "bracketClose");
            
            addButtonHotKey(clearDisplayButton, KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK, "clear_display");
        } else {
            System.out.println("disabled keys.");
            removeButtonHotKey(buttonClear);
            removeButtonHotKey(buttonEquals);
            removeButtonHotKey(buttonComma);
            removeButtonHotKey(buttonAdd);
            removeButtonHotKey(buttonSubstract);
            removeButtonHotKey(buttonMultiply);
            removeButtonHotKey(buttonDivide);
            removeButtonHotKey(buttonBracketOpen);
            removeButtonHotKey(buttonBracketClose);
            removeButtonHotKey(clearDisplayButton);
        }
    }
    
    /**
     * Adds a hotkey for the given button.
     * @param button The button which will get the hotkey.
     * @param key Sames as {@link javax.swing.KeyStroke#getKeyStroke(int, int) KeyStroke.getKeyStroke(int, int)} method
     * @param keyModifier Sames as {@link javax.swing.KeyStroke#getKeyStroke(int, int) KeyStroke.getKeyStroke(int, int)} method
     * @param actionMapKey Same as {@link javax.swing.ActionMap#put(java.lang.Object, javax.swing.Action) ActionMap.put()} method
     */
    private void addButtonHotKey(JButton button, int key, int keyModifier, Object actionMapKey) {
        button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, keyModifier), actionMapKey);
        button.getActionMap().put(actionMapKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.actionPerformed(event);
            }
        });
    }
    
    /**
     * Removes the hotkey for the given button.
     * @param button The button
     */
    private void removeButtonHotKey(JButton button) {
        button.getInputMap(WHEN_IN_FOCUSED_WINDOW).clear();
    }
    
    /**
     * Class which handles all the events that occur within the MonsterPanel, except the deleteButton's listener.
     */
    private class MyListener implements ActionListener, FocusListener {
        private MonsterPanel panel;
        
        private MyListener(MonsterPanel panel) {
            this.panel = panel;
       }
        
        @Override
        public void focusGained(FocusEvent event) {
            /* In case the user clicks on a textfield, select the whole text */
            if(event.getSource() instanceof JTextField) {
                JTextField source = (JTextField)event.getSource();
                source.selectAll();
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() instanceof JCheckBox) {
                checkBoxEventHandler(event, (JCheckBox)event.getSource());
                
            } else if(event.getSource() instanceof JButton) {
                JButton source = (JButton)event.getSource();
                
                if(source.getActionCommand().equals("numericOperationButton") || source.getActionCommand().equals("numericButton")) {
                    String opText = operationsTextField.getText().replaceAll(" ", "");
                    char buttonChar  = source.getText().charAt(0); // == operationChar or numberChar. Dld to char pou pathse.
                    char opTextLastChar = opText.charAt(opText.length() - 1);
                    boolean isOperator = isOperator(buttonChar);
                    
                    System.out.println("opText: " + opText);
                    System.out.println("opTextLastChar: " + opTextLastChar);
                    System.out.println("pressedChar: " + buttonChar);
                    if(opText.equals("" + monster.getCurHP()) && !isOperator) {  //den pathse operation enw egrafe to curHP.
                        System.out.println("tried pressing something else when expected operation.");
                        return;
                    }
                    
                    if(isOperator) {
                        if(isOperator(opTextLastChar))    //an to teleutaio char einai operator.
                            opText = opText.substring(0, opText.length() - 1) + buttonChar; //ton kanoume replace me ton neo operator
                        else if(opTextLastChar == '.')
                            opText = opText.substring(0, opText.length() - 1) + buttonChar; //otan exei anoiksei dekadiko alla den exei valei peretairo psifio.
                        else if(Character.isDigit(opTextLastChar) || opTextLastChar == ')') //alliws an den einai kai to last char bracket
                            opText = opText.concat("" + buttonChar);   //vazoume ton operator.
                        
                    } else if(isBracket(buttonChar)) {
                        if(buttonChar == '(') {
                            if(isOperator(opTextLastChar) || opTextLastChar == '(')  //mono an to last char einai operator epitrepetai parenthesh.
                                opText = opText.concat("" + buttonChar);
                        } else if(buttonChar == ')') {
                            for(int i=0, hasOpened=0, hasClosed=0; i<opText.length(); i++) {    //elenxoume ean yparxei anoikth parenthesh pou den exei kleisei.
                                if(opText.charAt(i) == '(')
                                    hasOpened++;
                                else if(opText.charAt(i) == ')')
                                    hasClosed++;
                                if(i == opText.length() - 1 && hasOpened != hasClosed) {    //yparxei anoikth parenthesh pou den exei kleisei.
                                    if(Character.isDigit(opTextLastChar) || opTextLastChar == ')')
                                        opText = opText.concat("" + buttonChar);
                                    else if(opTextLastChar == '.')              //otan exei anoiksei dekadiko alla den exei valei peretairo psifio.
                                        opText = opText.substring(0, opText.length() - 1) + buttonChar; //bgazoume thn ypodiastolh kai kleinoume thn parenthesh.
                                    else if(opTextLastChar == '(')
                                        opText = opText.substring(0, opText.length() - 1);  //kleinei parenthesh pou molis anoikse.
                                    break;
                                }
                            }
                        }
                    } else if(buttonChar == '.') { //ean to last char einai number kai pathse ypodiastolh.
                        boolean hasFoundComma = false;
                        for(int i=opText.length() - 1; i>=0; i--) {
                            if(opText.charAt(i) == '.')     //hasFoundComma
                                hasFoundComma = true;
                            else if(isBracket(opText.charAt(i)) || isOperator(opText.charAt(i))) {   //hasFoundNonDigit character e
                                if(!hasFoundComma && Character.isDigit(opTextLastChar)) {   //an den paei na kanei poustia, dld 5.321.52 (diplo comma)
                                    opText = opText.concat("" + buttonChar);
                                }
                                break;
                            }
                        }
                    } else if(Character.isDigit(buttonChar)) {    //einai number
                        if(opTextLastChar != ')')   //se kathe allh periptwsh vazoume number.
                            opText = opText.concat("" + buttonChar);
                    }
                    parseOperationsTextField(opText);
                } else if(source.equals(buttonClear)) {
                    String opText = operationsTextField.getText().replaceAll(" ", "");
                    
                    if(opText.equals("" + monster.getCurHP())) {  //pathse C enw egrafe to curHP.
                        System.out.println("tried erasing full hp.");
                        return;
                    }
                    opText = opText.substring(0, opText.length() - 1);  //removing lastChar
                    parseOperationsTextField(opText);
                } else if(source.equals(buttonEquals)) {
                    String opText = operationsTextField.getText().replaceAll(" ", "");
                    char lastChar = opText.charAt(opText.length() - 1);

                    if(isOperator(lastChar) || lastChar == '.') //an to teleutaio char einai operator h mh oloklhrwmenos dekadikos.
                        opText = opText.substring(0, opText.length() - 1);
                    for(int i=0, hasOpened=0, hasClosed=0, initialOpTextLen=opText.length(); i<initialOpTextLen; i++) {  //kleinei anoiktes parentheseis.
                        char ch = opText.charAt(i);
                        if(ch == '(')
                            hasOpened++;
                        else if(ch == ')')
                            hasClosed++;
                        while(i==initialOpTextLen - 1 && hasOpened > hasClosed) {  //se periptwsh pou exei anoiksei '(' kai den tis exei kleisei.
                            hasClosed++;
                            opText = opText + ")";
                        }
                    }
                    System.out.println("removed uneccessary operator and fixed text: " + opText);

                    //vriskoume tous operators kai tous nums kai tous kataxwroume se ArrayLists.
                    ArrayList<Character> operators = new ArrayList<>();
                    ArrayList<Double> numbers = new ArrayList<>();
                    for(int i=0; i<opText.length(); i++) {
                        char ch = opText.charAt(i);

                        if(i==0 && monster.getCurHP() < 0) {   //den lamvanei to '-' brosta apo to HP. To thewrei ws negative num sto epomeno loop.
                            numbers.add((double)(monster.getCurHP()));
                            i = new String("" + monster.getCurHP()).length() - 1;    //pernaei thn thesh tou HP.
                            continue;   //kai phgainei sthn epomenh pou logika einai operator..
                        }
                        if(isOperator(ch) || isBracket(ch)) {
                            operators.add(ch);
                            continue;
                        }
                        //se periptwsh pou einai digit, vriskei oloklhro ton arithmo.
                        for(int j=i; j<=opText.length(); j++) { //vriskoume tous numbers - operators.
                            if(j == opText.length() || isOperator(opText.charAt(j)) || isBracket(opText.charAt(j))) { //vrikame to char to opoio pleon den einai nummber! Epishs to last char einai pada number.
                                if(j == opText.length())
                                    numbers.add(Double.parseDouble(opText.substring(i)));
                                else
                                    numbers.add(Double.parseDouble(opText.substring(i, j)));
                                i = j - 1; //giati vrikame ton teleutaio mh arithmo(ws char) kai ton apothikeusame. Den theloume na xasoume mia thesh me to i++ giauto to j-1.
                                break; //synexizei sto epomeno i loop.
                            }
                        }
                    }
/*  DEBUG        
                    System.out.println("parsing operators and numbers...");
                    for(int i=0; i<operators.size(); i++) {
                        System.out.println("ch[" + i + "]: " + operators.get(i));
                    }
                    for(int i=0; i<numbers.size(); i++) {
                        System.out.println("num[" + i + "]: " + numbers.get(i));
                    }
*/
/*
                        ypologizei ta pio eswterika brackets kai phgainei olo kai pros ta eksw mexri na mhn yparxoun brackets
                        opou tote aple ektelei tis ypoloipes prakseis.
*/
                    boolean done = false;
                    double result = -1;
                    while(!done) {
                        ArrayList<Character> toSendOperators = new ArrayList<>();
                        ArrayList<Double> toSendNumbers = new ArrayList<>();
                        int bracketsCrossNum = 0, startBracketInd = -1, endBracketInd = -1;

                        for(int i=0; i<operators.size(); i++) { //vriskoume thn pio eswterikh parenthesh.
                            if(operators.get(i) == '(') {
                                bracketsCrossNum++;
                                startBracketInd = i;
                            } else if(operators.get(i) == ')') {
                                endBracketInd = i;
                                bracketsCrossNum--; //giati metrame mesa se poses parenthesis eimaste, den metrame gia thn idia thn parenthesh pou tha ektelesoume.
                                break;
                            }
                        }

                        if(startBracketInd != -1 && endBracketInd != -1) {  //ean yparxoun parentheseis.
                            //vriskoume tous operators kai tous nums pou vriskodai mesa stis parentheseis kai tous vgazoume apo ta ArrayLists..
                            for(int j=startBracketInd+1, removalCounter=0; j<=endBracketInd-1; j++, removalCounter++) {
                                toSendOperators.add(operators.remove(j - removalCounter));     //PROSOXH!! Peftei to size!!!
//                                System.out.println("toSendOperators: " + toSendOperators.get(removalCounter));    //DEBUG
                            }            
                            for(int j=startBracketInd-bracketsCrossNum, removalCounter=0; j<=endBracketInd-bracketsCrossNum-1; j++, removalCounter++) {
                                toSendNumbers.add(numbers.remove(j - removalCounter));         //PROSOXH!! Ppeftei to size!!!
//                                System.out.println("toSendNumbers: " + toSendNumbers.get(removalCounter));    //DEBUG
                            }

                            //afairoume tis parentheseis pou molis xrhsimopoihsame.
                            operators.remove(startBracketInd);
                            operators.remove(startBracketInd);
                            numbers.add(startBracketInd-bracketsCrossNum, executeOrder(toSendOperators, toSendNumbers));    //adding to result.

                        } else {    //den yparxoun alles parenthesis.
                            result = executeOrder(operators, numbers);
                            numbers.clear();
                            operators.clear();
                            done = true;
                        }
                    }
                    
                    System.out.println("finalResult: " + result);
                    result = Math.floor(result);
                    System.out.println("roundedResult: " + result);
                    int hpDif = (int)result - monster.getCurHP();
                    char hpDifSign = (hpDif>0) ? '+' : '-';
                    int offset = Integer.toString(monster.getCurHP()).length() - Integer.toString(hpDif).length();
                    String strOffset = new String();
                    for(int i=0; i<offset; i++)
                        strOffset.concat(" ");
                    monster.setCurHP((int)result);
                    calculationsTextArea.setText(calculationsTextArea.getText() + "\n" + hpDifSign + Math.abs(hpDif) + "\n----\n" + ((monster.getCurHP()<0) ? ""  : " ") + monster.getCurHP());
                    operationsTextField.setText(operationsOffset + monster.getCurHP());
                    
                } else if(source.equals(clearDisplayButton)) {
                    calculationsTextArea.setText(((monster.getCurHP()<0) ? "" : " ") + monster.getCurHP());
                } else if(source.equals(addNotesButton)) {
                    EditTextAreaDialog dialog = new EditTextAreaDialog(notesTextArea);
                    dialog.setVisible(true);
                }
            }
        }
        
        /**
         * Handles the events which were triggered by {@link javax.swing.JCheckBox JCheckBox} objects.
         * @param event The event which occured.
         * @param checkbox The checkbox which triggered the event.
         */
        private void checkBoxEventHandler(ActionEvent event, JCheckBox checkbox) {
            if(checkbox.equals(variablesLockCheckBox)) {
                boolean isLocked = variablesLockCheckBox.isSelected();
                enableHotKeys(isLocked && isFocused);
                if(isLocked) {  /* variables have been locked. Update them */
                    variablesLockCheckBox.setIcon(IMAGE_LOCK_LOCKED);
                    nameTextField.setBackground(new Color(255, 175, 175));
                    try {
                        /* Update the Monster */
                        int newMaxHP = Integer.parseInt(fullHPTextField.getText());
                        monster.setMaxHP(newMaxHP); // May throw IllegalArgumentException

                        /* Update the GUI */
                        operationsTextField.setText(operationsOffset + monster.getCurHP());
                        calculationsTextArea.setText(((monster.getCurHP() < 0) ? "" : " ") + monster.getCurHP());
                    } catch(Exception ex) {
                        fullHPTextField.setText("" + monster.getMaxHP());   //User mis-typed in the max HP field. Restore it to its original content.
                    }
                } else {    /* Variables have been unlocked */
                    variablesLockCheckBox.setIcon(IMAGE_LOCK_UNLOCKED);
                    nameTextField.setBackground(Color.WHITE);
                }
                for(JTextField textField : variableTextFields) {
                    textField.setEditable(!isLocked);
                    textField.setFocusable(!isLocked);
                }
            } else if(checkbox.equals(deleteLockCheckBox)) {
                boolean isLocked = deleteLockCheckBox.isSelected();
                deleteLockCheckBox.setIcon(isLocked ? IMAGE_LOCK_LOCKED : IMAGE_LOCK_UNLOCKED);
                deleteButton.setEnabled(!isLocked);
            }
        }
        
        
        private void parseOperationsTextField(String passedText) {
            String visualText = new String();
            for(int i=0; i<passedText.length() - 1; i++) {
                char curChar = passedText.charAt(i);
                boolean addSpace = !(curChar == '.' || (Character.isDigit(curChar) && (Character.isDigit(passedText.charAt(i+1)) || passedText.charAt(i+1) == '.')) );

                if(addSpace)
                    visualText = visualText.concat(passedText.charAt(i) + " ");
                else
                    visualText = visualText.concat(passedText.charAt(i) + "");
            }
            visualText = visualText.concat("" + passedText.charAt(passedText.length() - 1));    //vazei to teleutaio char. logo ths for an exei xreiastei space exei hdh bei.
            
            String initialTextOffset = (monster.getCurHP() >= 0) ? "  " : " ";
            visualText = initialTextOffset + visualText;

            System.out.println("finalText: " + passedText);
            System.out.println("visualText: " + visualText);
            operationsTextField.setText(visualText);       
        }
        
        private double executeOrder(ArrayList<Character> operators, ArrayList<Double> numbers) {   //executing orders with no brackets inbetween. numbers == operators - 1
            for(int i=0; i<2; i++) {    //dyo fores running ton pinaka me mia fora gia * kai / ... kai allh mia fora gia + kai -
                for(int j=0; j<operators.size(); j++) {
                    boolean highOrder = (i==0);  //TRUE gia '*' h '/', dld i==0. FALSE gia '+' h '-', dld i!=0
                    boolean hasExecutedOp = false;
                    char op = operators.get(j);
                    double result = 0;

                    if(highOrder && (op == '*' || op == '/')) {
                        try {
                            result = (op == '*') ? (numbers.get(j) * numbers.get(j+1)) : (numbers.get(j) / numbers.get(j+1));
                            if(!Double.isFinite(result))    //dividied by zero may give +infinity, -infinity, NaN (Not a Number)
                                throw new ArithmeticException("Result was not finite number.");
                        } catch(ArithmeticException ae) {
                            System.err.println("Error while exectuing orders. " + ae.getMessage());
                            System.err.println("Result for this operation set to 0. OpperandLeft: " + numbers.get(j) + " OperandRight: " + numbers.get(j+1));
                            result = 0;
                        }
                        hasExecutedOp = true;
                    } else if(!highOrder && (op == '+' || op == '-')) {
                        result = (op == '+') ? (numbers.get(j) + numbers.get(j+1)) : (numbers.get(j) - numbers.get(j+1));
                        hasExecutedOp = true;
                    }

                    if(hasExecutedOp) {
                        operators.remove(j);
                        numbers.remove(j);
                        numbers.remove(j);
                        numbers.add(j, result);
                        j--;    //to j logo twn parapanw remove epesai SYNOLIKA kata 1 theseis.
                    }
                }
            }
            return numbers.get(0);
        }

        private boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '/' || c == '*';
        }
        private boolean isBracket(char c) {
            return c == '(' || c == ')';
        }
        
        @Override
        public void focusLost(FocusEvent event) { }
        
    }

//  <editor-fold desc="Generated Code." defaultstate="collapsed">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        calculatorPanel = new javax.swing.JPanel();
        operationsTextField = new javax.swing.JTextField();
        buttonsPanel = new javax.swing.JPanel();
        button2 = new javax.swing.JButton();
        button5 = new javax.swing.JButton();
        button6 = new javax.swing.JButton();
        button0 = new javax.swing.JButton();
        button3 = new javax.swing.JButton();
        button1 = new javax.swing.JButton();
        button4 = new javax.swing.JButton();
        button7 = new javax.swing.JButton();
        button8 = new javax.swing.JButton();
        button9 = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        buttonSubstract = new javax.swing.JButton();
        buttonMultiply = new javax.swing.JButton();
        buttonDivide = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        buttonBracketOpen = new javax.swing.JButton();
        buttonBracketClose = new javax.swing.JButton();
        buttonComma = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        calculationsTextArea = new javax.swing.JTextArea();
        fullHPTextField = new javax.swing.JTextField();
        fullHPLabel = new javax.swing.JLabel();
        topSeperator = new javax.swing.JSeparator();
        variablesLockCheckBox = new javax.swing.JCheckBox();
        clearDisplayButton = new javax.swing.JButton();
        buttonEquals = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        deleteLockCheckBox = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        addNotesButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray), "Monster_ID: "));

        nameTextField.setEditable(false);
        nameTextField.setBackground(new java.awt.Color(255, 175, 175));
        nameTextField.setColumns(13);
        nameTextField.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        nameTextField.setFocusable(false);
        nameTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));

        nameLabel.setLabelFor(nameTextField);
        nameLabel.setText("Name: ");

        operationsTextField.setEditable(false);
        operationsTextField.setBackground(new java.awt.Color(204, 255, 255));
        operationsTextField.setColumns(20);
        operationsTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        operationsTextField.setFocusable(false);

        button2.setText("2");
        button2.setActionCommand("numericButton");
        button2.setFocusable(false);

        button5.setText("5");
        button5.setActionCommand("numericButton");
        button5.setFocusable(false);

        button6.setText("6");
        button6.setActionCommand("numericButton");
        button6.setFocusable(false);

        button0.setText("0");
        button0.setActionCommand("numericButton");
        button0.setFocusable(false);

        button3.setText("3");
        button3.setActionCommand("numericButton");
        button3.setFocusable(false);

        button1.setText("1");
        button1.setActionCommand("numericButton");
        button1.setFocusable(false);

        button4.setText("4");
        button4.setActionCommand("numericButton");
        button4.setFocusable(false);

        button7.setText("7");
        button7.setActionCommand("numericButton");
        button7.setFocusable(false);

        button8.setText("8");
        button8.setActionCommand("numericButton");
        button8.setFocusable(false);

        button9.setText("9");
        button9.setActionCommand("numericButton");
        button9.setFocusable(false);

        buttonAdd.setText("+");
        buttonAdd.setActionCommand("numericOperationButton");
        buttonAdd.setFocusable(false);

        buttonSubstract.setText("-");
        buttonSubstract.setActionCommand("numericOperationButton");
        buttonSubstract.setFocusable(false);
        buttonSubstract.setPreferredSize(new java.awt.Dimension(41, 23));

        buttonMultiply.setText("*");
        buttonMultiply.setActionCommand("numericOperationButton");
        buttonMultiply.setFocusable(false);
        buttonMultiply.setPreferredSize(new java.awt.Dimension(41, 23));

        buttonDivide.setText("/");
        buttonDivide.setActionCommand("numericOperationButton");
        buttonDivide.setFocusable(false);
        buttonDivide.setPreferredSize(new java.awt.Dimension(41, 23));

        buttonClear.setText("C");
        buttonClear.setActionCommand("");
        buttonClear.setFocusable(false);

        buttonBracketOpen.setText("(");
        buttonBracketOpen.setActionCommand("numericOperationButton");
        buttonBracketOpen.setFocusable(false);
        buttonBracketOpen.setPreferredSize(new java.awt.Dimension(41, 23));

        buttonBracketClose.setText(")");
        buttonBracketClose.setActionCommand("numericOperationButton");
        buttonBracketClose.setFocusable(false);
        buttonBracketClose.setPreferredSize(new java.awt.Dimension(41, 23));

        buttonComma.setText(".");
        buttonComma.setActionCommand("numericOperationButton");
        buttonComma.setFocusable(false);
        buttonComma.setPreferredSize(new java.awt.Dimension(41, 23));

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                        .addComponent(buttonBracketOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(72, 72, 72))
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(buttonsPanelLayout.createSequentialGroup()
                                .addComponent(button0, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(buttonComma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(buttonsPanelLayout.createSequentialGroup()
                                    .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(button4)
                                        .addComponent(button1))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                                            .addComponent(button2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(button3))
                                        .addGroup(buttonsPanelLayout.createSequentialGroup()
                                            .addComponent(button5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(button6))))
                                .addGroup(buttonsPanelLayout.createSequentialGroup()
                                    .addComponent(button7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button9)))
                            .addComponent(buttonBracketClose, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonClear)
                    .addComponent(buttonDivide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonMultiply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSubstract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAdd)))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonBracketOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonBracketClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addComponent(buttonDivide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMultiply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSubstract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonAdd))
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button7)
                            .addComponent(button8)
                            .addComponent(button9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button5)
                            .addComponent(button4)
                            .addComponent(button6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button2)
                            .addComponent(button3)
                            .addComponent(button1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button0)
                            .addComponent(buttonComma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout calculatorPanelLayout = new javax.swing.GroupLayout(calculatorPanel);
        calculatorPanel.setLayout(calculatorPanelLayout);
        calculatorPanelLayout.setHorizontalGroup(
            calculatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculatorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, calculatorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(operationsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        calculatorPanelLayout.setVerticalGroup(
            calculatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculatorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(operationsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        calculationsTextArea.setEditable(false);
        calculationsTextArea.setBackground(new java.awt.Color(233, 233, 161));
        calculationsTextArea.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        calculationsTextArea.setRows(14);
        calculationsTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder("Calculations"));
        calculationsTextArea.setFocusable(false);
        jScrollPane1.setViewportView(calculationsTextArea);

        fullHPTextField.setEditable(false);
        fullHPTextField.setColumns(7);
        fullHPTextField.setToolTipText("If this value is changed, then the current HP will also be changed");
        fullHPTextField.setFocusable(false);

        fullHPLabel.setLabelFor(fullHPTextField);
        fullHPLabel.setText("Full HP: ");

        variablesLockCheckBox.setSelected(true);
        variablesLockCheckBox.setToolTipText("Locks/Unlocks Attributes. If unlocked, no hotkeys can be used.");
        variablesLockCheckBox.setBorder(null);
        variablesLockCheckBox.setBorderPainted(true);
        variablesLockCheckBox.setFocusPainted(false);
        variablesLockCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        variablesLockCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        variablesLockCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/lock_locked.png"))); // NOI18N

        clearDisplayButton.setText("Clear Calculations");
        clearDisplayButton.setToolTipText("Ctrl + C");
        clearDisplayButton.setActionCommand("");
        clearDisplayButton.setFocusable(false);

        buttonEquals.setText("Calculate (=)");
        buttonEquals.setActionCommand("");
        buttonEquals.setFocusable(false);

        deleteButton.setText("Delete");
        deleteButton.setActionCommand("deleteMonsterPanel");
        deleteButton.setEnabled(false);
        deleteButton.setFocusable(false);

        deleteLockCheckBox.setSelected(true);
        deleteLockCheckBox.setToolTipText("Locks/Unlocks Delete Button");
        deleteLockCheckBox.setBorder(null);
        deleteLockCheckBox.setBorderPainted(true);
        deleteLockCheckBox.setFocusPainted(false);
        deleteLockCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deleteLockCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteLockCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/lock_locked.png"))); // NOI18N

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        notesTextArea.setEditable(false);
        notesTextArea.setBackground(new java.awt.Color(204, 255, 204));
        notesTextArea.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        notesTextArea.setLineWrap(true);
        notesTextArea.setRows(14);
        notesTextArea.setWrapStyleWord(true);
        notesTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder("Quick Notes"));
        notesTextArea.setFocusable(false);
        jScrollPane3.setViewportView(notesTextArea);

        addNotesButton.setText("Add Notes");
        addNotesButton.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(calculatorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(buttonEquals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(clearDisplayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(addNotesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(deleteButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deleteLockCheckBox))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameLabel)
                                .addGap(10, 10, 10)
                                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fullHPLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fullHPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(variablesLockCheckBox))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(topSeperator, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(fullHPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fullHPLabel)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(variablesLockCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topSeperator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(buttonEquals)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addNotesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteLockCheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearDisplayButton))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calculatorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNotesButton;
    private javax.swing.JButton button0;
    private javax.swing.JButton button1;
    private javax.swing.JButton button2;
    private javax.swing.JButton button3;
    private javax.swing.JButton button4;
    private javax.swing.JButton button5;
    private javax.swing.JButton button6;
    private javax.swing.JButton button7;
    private javax.swing.JButton button8;
    private javax.swing.JButton button9;
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonBracketClose;
    private javax.swing.JButton buttonBracketOpen;
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonComma;
    private javax.swing.JButton buttonDivide;
    private javax.swing.JButton buttonEquals;
    private javax.swing.JButton buttonMultiply;
    private javax.swing.JButton buttonSubstract;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JTextArea calculationsTextArea;
    private javax.swing.JPanel calculatorPanel;
    private javax.swing.JButton clearDisplayButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JCheckBox deleteLockCheckBox;
    private javax.swing.JLabel fullHPLabel;
    private javax.swing.JTextField fullHPTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JTextField operationsTextField;
    private javax.swing.JSeparator topSeperator;
    private javax.swing.JCheckBox variablesLockCheckBox;
    // End of variables declaration//GEN-END:variables
//  </editor-fold>
}

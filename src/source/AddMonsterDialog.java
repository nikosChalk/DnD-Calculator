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

import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * @author Nikos Chalkiadakis
 */
public class AddMonsterDialog extends javax.swing.JDialog {
    private MyListener listener;

    public AddMonsterDialog(Frame parent, boolean modal) {
        super(parent, modal);
        this.setLocationRelativeTo(parent);
        this.setIconImage(parent.getIconImage());
        initComponents();
        prepareClasses();
    }
    
    private void prepareClasses() {
        listener = new MyListener(this);
        
        this.addWindowListener(listener);
        doneButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
        
        nameTextField.addFocusListener(listener);
        hpTextField.addFocusListener(listener);
        copiesTextField.addFocusListener(listener);
    }
    
    public MonsterPanel[] display() {
        listener.checkingThread.start();
        this.setVisible(true);
        return listener.mPanelsArray;
    }
    
    private class MyListener extends WindowAdapter implements ActionListener, FocusListener {
        private AddMonsterDialog dialog;
        private MonsterPanel mPanelsArray[];
        private Thread checkingThread;
        
        private MyListener(AddMonsterDialog dialog) {
            this.dialog = dialog;
            mPanelsArray = new MonsterPanel[0];
            checkingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!(checkingThread.isInterrupted())) {
                        doneButton.setEnabled(checkValidityAndFlavorText());
                        try {
                            Thread.sleep(400);
                        } catch(InterruptedException ie) {
                            System.err.println("Interrupted. Thread stopping.");
                            return;
                        }
                    }
                }
            });
        }
        
        private boolean checkValidityAndFlavorText() {
            try {
                Integer.parseInt(hpTextField.getText());
                setFlavourText();
                Integer.parseInt(copiesTextField.getText());
                if (nameTextField.getText().length() == 0) {
                    throw new NumberFormatException("Name has not been fulfilled.");
                }
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() instanceof JButton) {
                JButton source = (JButton)event.getSource();
                
                if(source.equals(doneButton)) {
                    mPanelsArray = new MonsterPanel[Integer.parseInt(copiesTextField.getText())];
                    for(int i=0; i<mPanelsArray.length; i++)
                        mPanelsArray[i] = new MonsterPanel(nameTextField.getText() + ((mPanelsArray.length != 1) ? "_" + (i+1) : ""), notesTextArea.getText(), Integer.parseInt(hpTextField.getText())); //i+1 gia na mhn metrame apo to mhden kai berdeuomaste.
                } else if(source.equals(cancelButton)) {
                    ;   //no action. just for the redirection. It will continue tho and execute the bellow actions.
                } 
                checkingThread.interrupt();
                dialog.dispose();
            }
        }
        
        @Override
        public void windowClosing(WindowEvent event) {  //giati otan pataei X den kanei kill to thread.
            checkingThread.interrupt();
            dialog.dispose();
        }
        
        @Override
        public void focusLost(FocusEvent event) {
            if(event.getSource() instanceof JTextField) {
                doneButton.setEnabled(checkValidityAndFlavorText());
            }
        }
        
        private void setFlavourText() throws NumberFormatException {
            int hp = Integer.parseInt(hpTextField.getText());
            int value = hp/50;
            String textOffset = "  ";
            String flavourText = "";
            
            if(hp <= 0)
                flavourText = textOffset + "Hm... I'm wondering how tough will it be...";
            else if (value == 0) //0-49
                flavourText = textOffset + "Kitty cat...";
             else if (value == 1) //50-99
                flavourText = textOffset + "Something like trivial difficulty? ;)";
             else if (value == 2) //100-149
                flavourText = textOffset + "Go pokemon! Pikachu, I choose you!";
             else if (value == 3) //150-159
                flavourText = textOffset + "Ha! Piece of cake! ;)";
             else if (value == 4) //200-249
                flavourText = textOffset + "Hmmm.... Challenging...";
             else if (value == 5) //250-299
                flavourText = "www.youtube.com/watch?v=WQO-aOdJLiw";
             else if (value == 6) //300-349
                flavourText = textOffset + "You are going to give us quite a hard time...";
             else if (value == 7) //350-399
                flavourText = textOffset + "Ermm... Are you sure that you really want to do this? D:";
             else if (value == 8) //400-449
                flavourText = textOffset + "Chould you bring us more allies? We are screwed...";
             else if (value == 9) //450-499
                flavourText = textOffset + "Look, I know that you are high, but....";
             else if (value == 10 || value == 11) //500-599
                flavourText = textOffset + "HOLY BEARD OF POSEIDON";
             else if (value == 12 || value == 13) //600-699
                flavourText = textOffset + "Ermmm.....Yeah...Right DM... D:";
             else if (value == 14 || value == 15) //700-799
                flavourText = textOffset + "*Insert evil DM laugh here*";
             else if (value == 16 || value == 17) //800-899
                flavourText = textOffset + "There was once a living peacful group... Yes, there was...";
             else if (value >= 18) //900-infinity.            
                flavourText = textOffset + "GodLike? Hahahah- Oh shit...";
            
            if(!(flavourText.equals(flavourTextArea.getText())))
                flavourTextArea.setText(flavourText);
        }

        @Override
        public void focusGained(FocusEvent event) { //GUI
            if(event.getSource() instanceof JTextField) {
                JTextField source = (JTextField)event.getSource();
                source.selectAll();
            }
        }
    }
    

//  <editor-fold desc="Generated Code." defaultstate="collapsed">    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        hpLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        hpTextField = new javax.swing.JTextField();
        copiesLabel = new javax.swing.JLabel();
        copiesTextField = new javax.swing.JTextField();
        doneButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        flavourTextAreaScrollPane = new javax.swing.JScrollPane();
        flavourTextArea = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Monster");
        setIconImage(Frame.ADD_MONSTER_ICON_IMAGE);
        setResizable(false);

        nameLabel.setLabelFor(hpTextField);
        nameLabel.setText("Name: ");

        hpLabel.setLabelFor(hpTextField);
        hpLabel.setText("Full HP: ");

        nameTextField.setColumns(15);

        hpTextField.setColumns(15);
        hpTextField.setText("0");

        copiesLabel.setLabelFor(copiesTextField);
        copiesLabel.setText("Copies: ");
        copiesLabel.setToolTipText("How many monsters should be created.");

        copiesTextField.setColumns(15);
        copiesTextField.setText("1");
        copiesTextField.setToolTipText("How many monsters should be created.");

        doneButton.setText("Rape Time!");
        doneButton.setToolTipText("All fields must have been filled. Numbers must be integers.");
        doneButton.setEnabled(false);
        doneButton.setFocusPainted(false);

        cancelButton.setText("Spare players");
        cancelButton.setFocusPainted(false);

        flavourTextArea.setEditable(false);
        flavourTextArea.setBackground(new java.awt.Color(204, 204, 255));
        flavourTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        flavourTextArea.setLineWrap(true);
        flavourTextArea.setText("  Hm... I'm wondering how tough will it be...");
        flavourTextArea.setToolTipText("Flavour Text ;)");
        flavourTextArea.setWrapStyleWord(true);
        flavourTextArea.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 2)));
        flavourTextAreaScrollPane.setViewportView(flavourTextArea);

        notesTextArea.setBackground(new java.awt.Color(204, 255, 204));
        notesTextArea.setColumns(30);
        notesTextArea.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        notesTextArea.setLineWrap(true);
        notesTextArea.setRows(8);
        notesTextArea.setWrapStyleWord(true);
        notesTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder("Quick Notes"));
        jScrollPane1.setViewportView(notesTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(doneButton)
                                .addGap(9, 9, 9)
                                .addComponent(cancelButton))
                            .addComponent(flavourTextAreaScrollPane))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(nameLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(hpLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(hpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(copiesLabel)
                                .addGap(18, 18, 18)
                                .addComponent(copiesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hpLabel)
                            .addComponent(hpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(copiesLabel)
                            .addComponent(copiesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addComponent(flavourTextAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(doneButton)
                            .addComponent(cancelButton)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel copiesLabel;
    private javax.swing.JTextField copiesTextField;
    private javax.swing.JButton doneButton;
    private javax.swing.JTextArea flavourTextArea;
    private javax.swing.JScrollPane flavourTextAreaScrollPane;
    private javax.swing.JLabel hpLabel;
    private javax.swing.JTextField hpTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextArea notesTextArea;
    // End of variables declaration//GEN-END:variables
//  </editor-fold>
    
}

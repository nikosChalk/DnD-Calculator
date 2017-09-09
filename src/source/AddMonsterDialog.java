/*
 * Copyright 2014-2017 Nikos Chalkiadakis.
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
import source.model.Monster;

/**
 * @author Nikos Chalkiadakis
 */
public class AddMonsterDialog extends javax.swing.JDialog {
    private MyListener listener;
    
    /**
     * The monster template.
     */
    private Monster templateMonster;
    /**
     * How many copies should be created.
     */
    private int copies;

    /**
     * Creates a new Dialog for creating a monster.
     * @param parent The dialog's parent. See more at {@link javax.swing.JDialog#JDialog(java.awt.Dialog, boolean) JDialog(Dialog, boolean)}
     * @param modal Same as See more at {@link javax.swing.JDialog#JDialog(java.awt.Dialog, boolean) JDialog(Dialog, boolean)}.
     */
    public AddMonsterDialog(Frame parent, boolean modal) {
        super(parent, modal);
        this.setLocationRelativeTo(parent);
        this.setIconImage(parent.getIconImage());
        initComponents();
        
        listener = new MyListener();
        copies = -1;
        templateMonster = null;
        registerListeners();
    }
    
    /**
     * Registers the listeners for this dialog's events.
     */
    private void registerListeners() {
        this.addWindowListener(listener);
        doneButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
        
        nameTextField.addFocusListener(listener);
        hpTextField.addFocusListener(listener);
        copiesTextField.addFocusListener(listener);
    }
    
    /**
     * Displays the dialog.
     * @return Returns an array containing {@link source.model.Monster Monster} instances which represent the requested monsters.
     */
    public Monster[] display() {
        listener.checkingThread.start();
        this.setVisible(true);
        
        Monster[] monsterArray = new Monster[copies];
        for(int i=0; i<monsterArray.length; i++) {
            Monster curMonster = new Monster(templateMonster);
            if(copies > 1)
                curMonster.setName(curMonster.getName() + " " + (i+1));
            monsterArray[i] = curMonster;
        }
        return monsterArray;
    }
    
    /**
     * Listener for the dialog's events.
     */
    private class MyListener extends WindowAdapter implements ActionListener, FocusListener {
        /**
         * This thread constantly checks (every 400ms) whether or not the user's current input is okay.
         */
        private Thread checkingThread;
        
        private MyListener() {
            checkingThread = new Thread(() -> {
                boolean isValidInput;
                while(!(checkingThread.isInterrupted())) {
                    isValidInput = checkValidity();
                    if(isValidInput)
                        setFlavourText();
                    doneButton.setEnabled(isValidInput);
                    try {
                        Thread.sleep(400);
                    } catch(InterruptedException ie) {
                        System.err.println("Interrupted. Thread stopping.");
                        return;
                    }
                }
            });
        }
        
        /**
         * Checks whether or not the user's inputs are correct.
         * @return True if inputs are correct, false otherwise.
         */
        private boolean checkValidity() {
            try {
                int hp = Integer.parseInt(hpTextField.getText());
                if(Integer.parseInt(copiesTextField.getText()) <= 0)
                    throw new Exception("Copies must be greater than 0");
                new Monster(nameTextField.getText(), notesTextArea.getText(), hp);    //Attempting to create a sample monster for Exception purposes.
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        
        /**
         * Sets a flavor text based on the given monster's maxHP.
         * @throws NumberFormatException If the hpTextField cannot be parsed to an int.
         */
        private void setFlavourText() throws NumberFormatException {
            int maxHP = Integer.parseInt(hpTextField.getText());
            flavourTextArea.setText("  ".concat(Monster.getFlavourText(maxHP)));
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() instanceof JButton) {
                JButton source = (JButton)event.getSource();
                
                if(source.equals(doneButton) && checkValidity()) { // Check validity once more, just in case.
                    templateMonster = new Monster(nameTextField.getText(), notesTextArea.getText(), Integer.parseInt(hpTextField.getText()));
                    copies = Integer.parseInt(copiesTextField.getText());
                    releaseResources();
                } else if(source.equals(cancelButton)) {
                    releaseResources();
                }
            }
        }
        
        @Override
        public void windowClosing(WindowEvent event) {  //giati otan pataei X gia na kleisei to window.
            releaseResources();
        }

        /**
         * Releases the resources that this window is using and closes it.
         */
        private void releaseResources() {
            checkingThread.interrupt();
            dispose();
        }
        
        @Override
        public void focusGained(FocusEvent event) {
            if(event.getSource() instanceof JTextField) {
                JTextField source = (JTextField)event.getSource();
                /* When the user clicks on a JTextField, select the whole text that exists there. */
                source.selectAll();
            }
        }
        @Override
        public void focusLost(FocusEvent event) { }
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

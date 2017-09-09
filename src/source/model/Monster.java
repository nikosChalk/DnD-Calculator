/*
 * Copyright 2017 Nikolaos Chalkiadakis.
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
package source.model;

/**
 * @author Nikolaos Chalkiadakis
 */
public class Monster {
    private static int MONSTER_COUNTER = 0;
    
    public final int id;
    private String name, notes;
    private int curHP, maxHP;
    
    /**
     * Creates a new monster with the given attributes.
     * @param name The monster's name.
     * @param notes The monster's quick notes.
     * @param maxHP The monster's maxHP.
     * @throws IllegalArgumentException if name or notes are null. Also if maxHP{@literal <}0.
     */
    public Monster(String name, String notes, int maxHP) throws IllegalArgumentException {

        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        if(maxHP<0)
            throw new IllegalArgumentException("Max HP cannot be negative");
        setName(name);
        setNotes(notes);
        if(maxHP<0)
            throw new IllegalArgumentException("Max HP cannot be negative");
        this.maxHP = maxHP;
        this.curHP = maxHP;
        id = MONSTER_COUNTER++;
    }
    
    /**
     * Creates a copy of the given monster.
     * @param monster The monster to copy.
     * @throws NullPointerException If monster is null.
     */
    public Monster(Monster monster) throws NullPointerException {
        this(monster.getName(), monster.getNotes(), monster.getMaxHP());
    }
    

    //  <editor-fold desc="Getters" defaultstate="collapsed">
    public String getName() {
        return name;
    }
    public String getNotes() {
        return notes;
    }
    public int getCurHP() {
        return curHP;
    }
    public int getMaxHP() {
        return maxHP;
    }
    //  </editor-fold>    
    
    //  <editor-fold desc="Setters" defaultstate="collapsed">
    /**
     * Sets the name.
     * @param name The new name.
     * @throws IllegalArgumentException If name is null or empty.
     */
    public void setName(String name) {
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        this.name = name;
    }
    /**
     * Sets the notes.
     * @param notes The notes.
     * @throws NullPointerException if notes are null
     */
    public void setNotes(String notes) {
        if(notes == null)
            throw new IllegalArgumentException("Notes cannot be null");
        this.notes = notes;
    }
    public void setCurHP(int curHP) {
        this.curHP = curHP;
    }
    /**
     * Sets the max HP. Any change (increase or decrease) to the max HP will also affect the monster's curHP proportionally.
     * @param maxHP The max HP.
     * @throws IllegalArgumentException If maxHP{@literal <}0
     */
    public void setMaxHP(int maxHP) {
        if(maxHP<0)
            throw new IllegalArgumentException("Max HP cannot be negative");
        curHP = curHP + (maxHP - this.maxHP);
        this.maxHP = maxHP;
    }
    //</editor-fold>
    
    
    /**
     * Returns a flavour text based on a creature's maxHP.
     * @param maxHP The creature's maxHP whose flavor text you want.
     * @return The flavor text
     * @throws IllegalArgumentException If maxHP{@literal <}0
     */
    public static String getFlavourText(int maxHP) throws IllegalArgumentException {
        if(maxHP<0)
            throw new IllegalArgumentException("Max HP cannot be negative");
        int value = maxHP / 50;
        String flavourText = "";

        if(maxHP == 0) {
            flavourText = "Hm... I'm wondering how tough will it be...";
        } else if(value == 0) { //0-49
            flavourText = "Kitty cat...";
        } else if(value == 1) { //50-99
            flavourText = "Something like trivial difficulty? ;)";
        } else if(value == 2) { //100-149
            flavourText = "Go pokemon! Pikachu, I choose you!";
        } else if(value == 3) { //150-159
            flavourText = "Ha! Piece of cake! ;)";
        } else if(value == 4) { //200-249
            flavourText = "Hmmm.... Challenging...";
        } else if(value == 5) { //250-299
            flavourText = "www.youtube.com/watch?v=WQO-aOdJLiw";
        } else if(value == 6) { //300-349
            flavourText = "You are going to give us quite a hard time...";
        } else if(value == 7) { //350-399
            flavourText = "Ermm... Are you sure that you really want to do this? D:";
        } else if(value == 8) { //400-449
            flavourText = "Chould you bring us more allies? We are screwed...";
        } else if(value == 9) { //450-499
            flavourText = "Look, I know that you are high, but....";
        } else if(value == 10 || value == 11) { //500-599
            flavourText = "HOLY BEARD OF POSEIDON";
        } else if(value == 12 || value == 13) { //600-699
            flavourText = "Ermmm.....Yeah...Right DM... D:";
        } else if(value == 14 || value == 15) { //700-799
            flavourText = "*Insert evil DM laugh here*";
        } else if(value == 16 || value == 17) { //800-899
            flavourText = "There was once a living peacful group... Yes, there was...";
        } else if(value >= 18) {    //900-infinity.
            flavourText = "GodLike? Hahahah- Oh shit...";
        }
        return flavourText;
    }
}

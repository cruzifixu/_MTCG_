package game;

import game.card.Cards_impl;

public class monsterCard extends Cards_impl {
    protected int defensive; // block attacks
    protected boolean boost; // boost monster powers with right spell card
    protected boolean weakens; // weakens monster (power) if spell card got used
}

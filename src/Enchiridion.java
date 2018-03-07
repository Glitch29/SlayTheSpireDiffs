package com.megacrit.cardcrawl.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class Enchiridion
        extends AbstractRelic
{
    public static final String ID = "Enchiridion";
    public static final float RARE_CHANCE = 0.5F;

    public Enchiridion()
    {
        super("Enchiridion", "enchiridion.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart()
    {
        flash();

        AbstractCard c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.5F), AbstractCard.CardType.POWER, false).makeCopy();
        if (c.cost != -1) {
            c.updateCost(-99);
        }
        UnlockTracker.markCardAsSeen(c.cardID);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
    }

    public AbstractRelic makeCopy()
    {
        return new Enchiridion();
    }
}
    
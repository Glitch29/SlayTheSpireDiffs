/**
 * Created by Aaron Fisher on 3/6/2018.
 */
public class Cleric
        extends AbstractImageEvent
{
    public static final String ID = "The Cleric";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("The Cleric");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final int HEAL_COST = 35;
    private static final int PURIFY_COST = 50;
    private static final int A_2_PURIFY_COST = 75;
    private int purifyCost = 0;
    private static final float HEAL_AMT = 0.25F;
    private static final String DIALOG_1 = DESCRIPTIONS[0];
    private static final String DIALOG_2 = DESCRIPTIONS[1];
    private static final String DIALOG_3 = DESCRIPTIONS[2];
    private static final String DIALOG_4 = DESCRIPTIONS[3];
    private int healAmt;

    public Cleric()
    {
        super(NAME, DIALOG_1, "images/events/cleric.jpg");
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.purifyCost = 75;
        } else {
            this.purifyCost = 50;
        }
        int gold = AbstractDungeon.player.gold;
        if (gold >= 35)
        {
            this.healAmt = ((int)(AbstractDungeon.player.maxHealth * 0.25F));
            GenericEventDialog.setDialogOption(OPTIONS[0] + this.healAmt + OPTIONS[8], gold < 35);
        }
        else
        {
            GenericEventDialog.setDialogOption(OPTIONS[1] + 35 + OPTIONS[2], gold < 35);
        }
        if (gold >= 50) {
            GenericEventDialog.setDialogOption(OPTIONS[3] + this.purifyCost + OPTIONS[4], gold < 50);
        } else {
            GenericEventDialog.setDialogOption(OPTIONS[5], gold < 50);
        }
        GenericEventDialog.setDialogOption(OPTIONS[6]);
    }

    public void update()
    {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2, Settings.HEIGHT / 2));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                switch (buttonPressed)
                {
                    case 0:
                        AbstractDungeon.player.loseGold(35);
                        AbstractDungeon.player.heal(this.healAmt);
                        showProceedScreen(DIALOG_2);
                        logMetric("Healed");
                        break;
                    case 1:
                        AbstractDungeon.player.loseGold(50);
                        AbstractDungeon.gridSelectScreen.open(
                                CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                        .getPurgeableCards()), 1, OPTIONS[7], false, false, false, true);

                        showProceedScreen(DIALOG_3);
                        logMetric("Card Removal");
                        break;
                    default:
                        showProceedScreen(DIALOG_4);
                        logMetric("Leave");
                }
                break;
            default:
                openMap();
        }
    }

    public void logMetric(String actionTaken)
    {
        AbstractEvent.logMetric("The Cleric", actionTaken);
    }
}

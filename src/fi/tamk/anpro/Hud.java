package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;

/**
 * Hallitsee pelin käyttöliittymää eli HUDia. Ei kuitenkaan tunnista kosketustapahtumia
 * eikä sisällä toteutusta aseiden uudelleensijoittamiseen.
 */
public class Hud
{
    /* Painikkeiden tunnukset */
    public static final int BUTTON_1  = 0;
    public static final int BUTTON_2  = 1;
    public static final int BUTTON_3  = 2;
    public static final int SPECIAL_1 = 3;
    public static final int SPECIAL_2 = 4;

    /* Painikkeisiin sijoitettujen aseiden tunnukset (viittaa WeaponManagerin
       asetaulukoiden soluihin */
    public int[] weapons;
    
    /* Käyttöliittymän objektit */
    public        ArrayList<Button>          buttons                 = null; // TODO: Ei kai tarvitsisi olla public?
    public		  ArrayList<CooldownCounter> cooldowncounter         = null;
    public 		  ArrayList<Icon>	         icons	                 = null;
    public static ArrayList<Counter>         counters                = null;
    public static Joystick                   joystick                = null;
    public static Bar		                 healthBar               = null;
    public static Bar				         armorBar                = null;
    public        GuideArrow                 guideArrowToCollectable = null;
    public        GuideArrow                 guideArrowToMothership  = null;
    public static Radar						 radar_top			     = null;
    public static Radar						 radar_left			     = null;
    public static Radar						 radar_right			 = null;
    public static Radar						 radar_down			     = null;
    
     
    /* Osoittimet tarvittaviin luokkiin */
    private final WeaponManager weaponManager;

    /**
     * Alustaa luokan muuttujat ja lukee Hudin ulkoasun XmlReaderin avulla.
     * 
     * @param _context       Ohjelman konteksti
     * @param _weaponManager Osoitin WeaponManageriin
     */
    public Hud(Context _context, WeaponManager _weaponManager)
    {
        weaponManager = _weaponManager;
        
        weapons = new int[5];
        for (byte i = 0; i < 5; ++i) {
        	weapons[i] = -1;
        }
        
        weapons[0] = 0; // TODO: DEBUG!!!!
        
        buttons          = new ArrayList<Button>();
        cooldowncounter	 = new ArrayList<CooldownCounter>();
        counters         = new ArrayList<Counter>();
        icons            = new ArrayList<Icon>();

        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
        
        // Luodaan opastusnuolet
        guideArrowToCollectable = new GuideArrow(0, 0, GuideArrow.TARGET_COLLECTABLE);
        guideArrowToMothership  = new GuideArrow(0, 0, GuideArrow.TARGET_MOTHERSHIP);
    }

    /**
     * Päivittää cooldownit (HUD:ssa näkyvät, ei oikeita cooldowneja).
     */
    public final void updateCooldowns()
    {
    	 for (int i = buttons.size()-1; i >= 0; --i) {
            if (weapons[i] > -1) {
	    		if (weaponManager.cooldownLeft[weapons[i]] >= 0) {
	            	cooldowncounter.get(i).update(weaponManager.cooldownLeft[i]);
	            }
            }
        }
    }
    
    /**
     * Päivittää HUD:ssa näkyvän pistelaskurin.
     * 
     * @param _score Pisteet
     */
    public final static void updateScoreCounter(long _score)
    {
    	for (int i = counters.size()-1; i >= 0; --i) {
    		counters.get(i).parseScore(_score);
    	}
    }

    /**
     * Käsittelee napin painalluksen ja asettaa uuden aseen käyttöön WeaponManageriin.
     * 
     * @param _buttonId Painetun napin tunnus
     */
    public final void triggerClick(int _buttonId)
    {
        // Tarkistetaan, onko aseessa cooldownia jäljellä vai ei
    	if (weapons[_buttonId] > -1) {
	        if (weaponManager.cooldownLeft[weapons[_buttonId]] <= 0) {
	            
	            // Otetaan muut aseet pois käytöstä
	            for (Button object : buttons) {
	                object.setSelected(false); // TODO: Poista pelkästään valittuna ollut ase käytöstä
	            }
	            
	        	// Otetaan uusi ase käyttöön
	            weaponManager.setCurrentWeapon(weapons[_buttonId]);
	            icons.get(_buttonId).setState(true);
	            buttons.get(_buttonId).setSelected(true);
	        }
    	}
    }
    
    /**
     * Palauttaa osoittimen healthBarista.
     * 
     * @return Bar Osoitin healthBariin
     */
    public final static Bar getHealthBar()
    {
        return healthBar;
    }
    
    /**
     * Palauttaa osoittimen armorBarista.
     * 
     * @return Bar Osoitin armorBariin
     */
    public final static Bar getArmorBar()
    {
    	return armorBar;
    }
}

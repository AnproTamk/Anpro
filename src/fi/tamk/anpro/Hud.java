package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

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
    public        ArrayList<Button> buttons   = null;
    public		  ArrayList<Icon>	icons	  = null;
    public static Joystick          joystick  = null;
    public static Bar		        healthBar = null;

    /* Osoittimet WeaponManageriin ja Hudiin itseensä */
    private final  WeaponManager weaponManager;

    /**
     * Alustaa luokan muuttujat ja lukee Hudin ulkoasun XmlReaderin avulla.
     * 
     * @param Context       Ohjelman konteksti
     * @param WeaponManager Osoitin WeaponManageriin
     */
    public Hud(Context _context, WeaponManager _weaponManager)
    {
        weaponManager = _weaponManager;
        weapons = new int[5];
        
        buttons = new ArrayList<Button>();
        icons	= new ArrayList<Icon>();

        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
        
        //joystick = new Joystick(800, 480);
    }

    /**
     * Päivittää cooldownit (HUD:ssa näkyvät, ei oikeita cooldowneja).
     */
    public final void updateCooldowns()
    {
    	 for (int i = buttons.size()-1; i >= 0; --i) {
            if (weaponManager.cooldownLeft[weapons[i]] >= 0) {
            	icons.get(i).updateCooldownIcon(weaponManager.cooldownLeft[i]);
            }
        }
    }

    /**
     * Käsittelee napin painalluksen ja asettaa uuden aseen käyttöön WeaponManageriin.
     * 
     * @param int Painetun napin tunnus
     */
    public final void triggerClick(int _buttonId)
    {
        // Tarkistetaan, onko aseessa cooldownia jäljellä vai ei
        if (weaponManager.cooldownLeft[weapons[_buttonId]] <= 0) {
            
            // Otetaan muut aseet pois käytöstä
            for (Button object : buttons) {
                object.setSelected(false); // TODO: Poista pelkästään valittuna ollut ase käytöstä
            }
            
        	// Otetaan uusi ase käyttöön
            weaponManager.currentWeapon = weapons[_buttonId];
            buttons.get(_buttonId).setSelected(true);
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
}

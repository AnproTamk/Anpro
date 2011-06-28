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
    public ArrayList<Button> buttons   = null;
    public Joystick          joystick  = null;
    public static Bar		 healthBar = null;

    /* Osoitin WeaponManageriin (HUDin tehtävänä on muuttaa käytössä olevaa
       asetta WeaponManagerista) */
    private final WeaponManager weaponManager;
    
    private static Hud pointerToSelf;

    /**
     * Alustaa luokan muuttujat ja kutsuu XmlReaderia.
     */
    public Hud(Context _context)
    {
        weaponManager = WeaponManager.getConnection();
        weapons = new int[5];
        
        buttons = new ArrayList<Button>();

        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
        
        joystick = new Joystick(800, 480);
    }

    /**
     * Päivittää cooldownit (HUD:ssa näkyvät, ei "oikeita" cooldowneja).
     */
    public final void updateCooldowns()
    {
        for (int i = buttons.size()-1; i >= 0; --i) {
            if (weaponManager.cooldownLeft[weapons[i]] > 0 ) {
                // TODO:
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
        if (weaponManager.cooldownLeft[weapons[_buttonId]] <=0 ) {
            weaponManager.currentWeapon = weapons[_buttonId];

            for (Button object : buttons) {
                object.setSelected(false);
            }
            
            buttons.get(_buttonId).setSelected(true);
        }
    }
    
    /**
     * Palauttaa osoittimen tästä luokasta.
     * 
     * @return Hud Osoitin tähän luokkaan.
     */
    public final static Hud getConnection()
    {
        return pointerToSelf;
    }
    
    /**
     * Palauttaa osoittimen healthBarista.
     * 
     * @return Bar Osoitin healthBarista.
     */
    public final static Bar getHealthBar()
    {
        return healthBar;
    }
}

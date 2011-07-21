package fi.tamk.anpro;

/**
 * Toteutus lineaarisen reitinhaun teko‰lylle. Teko‰ly hakeutuu kohteenseensa
 * mahdollisimman suoraa reitti‰ pitkin ja reagoi ainoastaan kohteen liikkumiseen.
 * 
 * K‰ytet‰‰n ainoastaan ammuksille.
 */
public class LinearProjectileAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public LinearProjectileAi(AiObject _parentObject, int _userType) 
	{
		super(_parentObject, _userType);
	}

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
	@Override
    public final void setActive(float _x, float _y)
    {
		parentObject.direction = setDirection(_x, _y);
		
		active = true;
    }

    /**
     * Asettaa teko‰lyn ep‰aktiiviseksi.
     */
	@Override
    public final void setUnactive()
    {
		active = false;
    }
}

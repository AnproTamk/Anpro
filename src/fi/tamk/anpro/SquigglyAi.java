package fi.tamk.anpro;

/**
 * Toteutus "väistelevälle" tekoälylle. Tekoäly seuraa lähes siniaallon
 * muotoista reittiä kohti pelaajaa.
 * 
 * Käytetään ainoastaan vihollisille.
 */
public class SquigglyAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public SquigglyAi(int _id, int _type) 
	{
		super(_id, _type);
	}
    
    /**
     * Käsittelee tekoälyn.
     */
	@Override
	public void handleAi() {
		// TODO: Toteutus puuttuu
        
        /* Tarkistetaan törmäykset pelaajan kanssa */
        checkCollisionWithPlayer();
	}
}

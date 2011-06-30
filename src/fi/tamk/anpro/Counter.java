package fi.tamk.anpro;

/**
 * Sisältää pelaajan pisteiden laskemiseen tarvittavat toimenpiteet.
 * 
 * @extends GuiObject
 */
public class Counter extends GuiObject
{
	// Luodaan taulukko numeroarvoille
	//private int scoreArray[] = null;
	
	private int value;
	private int tempScore;

	/**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     * @param int Laskettavat arvot
     */
	public Counter(int _x, int _y, int _value) {
		super(_x, _y);
		
		value = _value;
		
		//scoreArray = new int[4];
		
		usedTexture = GLRenderer.TEXTURE_COUNTER;
		wrapper.guiObjectStates.set(listId, 1);
	}
	
	/**
	 * Palastellaan pisteet yksittäisiksi numeroiksi, joiden avulla
	 * piirretään yksittäiset numerot ruudulle pisteiden muodostamiseksi.
	 * 
	 * @param _score
	 */
	public void parseScore(long _score)
	{
		if (_score >= value) {
				int index;
				wrapper.guiObjectStates.set(listId, 1);
			
			if (value == 1000) {
				index = (int) (_score / value);
				usedTexture = GLRenderer.TEXTURE_COUNTER + index;
				//tempScore = (int)(_score - value * index);
				int a = 0;
			}
			else if (value == 100) {
				index = (int) (_score / value);
				usedTexture = GLRenderer.TEXTURE_COUNTER + index;
				//tempScore -= value * index;
				int a = 0;
			}
			else if (value == 10) {
				index = (int) (_score / value);
				usedTexture = GLRenderer.TEXTURE_COUNTER + index;
				//tempScore -= value * index;
				int a = 0;
			}
			else {
				index = (int) (_score / value);
				usedTexture = GLRenderer.TEXTURE_COUNTER + index;
				//tempScore -= value * index;
				int a = 0;
			}
		}
		else {
			wrapper.guiObjectStates.set(listId, 0);
		}
	}

}

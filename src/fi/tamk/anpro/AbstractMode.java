package fi.tamk.anpro;

abstract public class AbstractMode
{
    /* Kameran koordinaatit */
    protected int cameraX = CameraManager.camX;
    protected int cameraY = CameraManager.camY;
    
    abstract public void startWave();
    
    abstract protected void updateSpawnPoints();
}

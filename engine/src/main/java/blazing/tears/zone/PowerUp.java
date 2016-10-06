package blazing.tears.zone;

abstract class PowerUp {

    private boolean mPowerUpEnabled;

    public PowerUp() {
        mPowerUpEnabled = true;
    }

    public boolean isPowerUpEnabled() {
        return mPowerUpEnabled;
    }

    public void setPowerUpEnabled(boolean powerUpEnabled) {
        mPowerUpEnabled = powerUpEnabled;
    }

    public void usePowerUp() {
        if (mPowerUpEnabled) {
            powerUp();
        }
    }

    protected abstract void powerUp();
}

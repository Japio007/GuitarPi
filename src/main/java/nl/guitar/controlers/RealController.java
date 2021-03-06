package nl.guitar.controlers;

import com.pi4j.io.i2c.I2CFactory;
import i2c.servo.pwm.PCA9685;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealController extends RealTimeController  {
    private static final Logger logger = LoggerFactory.getLogger(RealController.class);
    private static final int updateFrequencyHz = 100; // max 200

    private static PCA9685[] servoBoards;

    static {
        try {
            logger.info("Starting REAL controller (works only on the PI)");
            servoBoards = new PCA9685[] {
                    new PCA9685(0x40),
                    new PCA9685(0x41),
                    new PCA9685(0x42)
                    //new PCA9685(0x48)
            };
            servoBoards[0].setPWMFreq(updateFrequencyHz);
            servoBoards[1].setPWMFreq(updateFrequencyHz);
            servoBoards[2].setPWMFreq(updateFrequencyHz);
            //servoBoards[3].setPWMFreq(updateFrequencyHz);
        } catch (I2CFactory.UnsupportedBusNumberException | UnsatisfiedLinkError e) {
            logger.error("Failed to load real guitar player", e);
            throw new RuntimeException(e);
        }
    }

    public void setServoPulse(int boardNumber, short port, float v) {
        if (port > -1 && boardNumber > -1) {
            servoBoards[boardNumber].setServoPulse(port, v);
        } else {
            logger.warn("Tried to control board {} with {} to value {}", boardNumber, port, v);
        }
    }
}

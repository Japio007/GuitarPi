package nl.guitar.player.tuning;

import nl.guitar.data.ConfigRepository;

public class DropDTuning extends GuitarTuning {
    public DropDTuning(ConfigRepository configRepository) {
        super(new int[] { -2, 0, 0, 0, 0, 0}, configRepository);
    }
}

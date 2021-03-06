package nl.guitar.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.guitar.domain.FredConfig;
import nl.guitar.domain.PlectrumConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConfigRepository {
    private static final Logger logger = LoggerFactory.getLogger(ConfigRepository.class);

    @ConfigProperty(name = "config.folder")
    String CONFIG_FOLDER = "./";

    @ConfigProperty(name = "controller")
    String controller;

    private static final String PLECTRUM_CONF = "plectrum.conf";
    private static final String FRED_CONF = "fred.conf";
    private static final short HOW_MANY_FRED_ON_STRING = 16;
    private static final int[] stringStartNote = new int[] { 28, 33, 38, 43, 47, 52};
    private static final ObjectMapper om = new ObjectMapper();

    static {
        om.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public List<PlectrumConfig> loadPlectrumConfig() {
        try {
            File configFile = new File(CONFIG_FOLDER + PLECTRUM_CONF);
            if (!configFile.exists()) {
                createDefaultPlectrumConfig(configFile);
            }
            return om.readValue(configFile, new TypeReference<List<PlectrumConfig>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDefaultPlectrumConfig(File configFile) {
        if ("RealController".equals(controller)) {
            throw new IllegalStateException("Won't create new config if real controller is enabled");
        }
        logger.warn("Unable to find {} so creating a new one", configFile);
        List<PlectrumConfig> config = new ArrayList<>();
        addNoteToPlectrumConfig(config, "E");
        addNoteToPlectrumConfig(config, "B");
        addNoteToPlectrumConfig(config, "G");
        addNoteToPlectrumConfig(config, "D");
        addNoteToPlectrumConfig(config, "A");
        addNoteToPlectrumConfig(config, "e");
        savePlectrumConfig(config);
    }

    private void addNoteToPlectrumConfig(List<PlectrumConfig> config, String note) {
        PlectrumConfig plectrumConfig = new PlectrumConfig();
        plectrumConfig.note = note;
        config.add(plectrumConfig);
    }

    public void savePlectrumConfig(List<PlectrumConfig> config) {
        try {
            File configFile = new File(CONFIG_FOLDER + PLECTRUM_CONF);
            om.writeValue(configFile, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<FredConfig>> loadFredConfig() {
        try {
            File configFile = new File(CONFIG_FOLDER + FRED_CONF);
            if (!configFile.exists()) {
                if ("RealController".equals(controller)) {
                    throw new IllegalStateException("Won't create new config if real controller is enabled");
                }
                logger.warn("Unable to find {} so creating a new one", configFile);
                List<List<FredConfig>> config = new ArrayList<>();
                for (int startNote : stringStartNote) {
                    List<FredConfig> stringConfig = new ArrayList<>();
                    config.add(stringConfig);
                    for (int i = 0; i < HOW_MANY_FRED_ON_STRING; i++) {
                        addNoteToFredConfig(stringConfig, startNote + i);
                    }
                }
                saveFredConfig(config);
            }
            return om.readValue(configFile, new TypeReference<List<List<FredConfig>>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFredConfig(List<List<FredConfig>> config) {
        try {
            File configFile = new File(CONFIG_FOLDER + FRED_CONF);
            om.writeValue(configFile, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNoteToFredConfig(List<FredConfig> config, int note) {
        FredConfig fredConfig = new FredConfig();
        fredConfig.note = note;
        fredConfig.push = 1.5f;
        fredConfig.address = 1;
        fredConfig.port = -1;
        if (note % 2 == 0) {
            fredConfig.free = 1.5f;
        }
        config.add(fredConfig);
    }
}

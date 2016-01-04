package cn.tsign.www.openapi.constant;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class ConfigProperties {
    private PropertiesConfiguration propertiesConfig;

    public ConfigProperties(String fileName) {
        try {
            propertiesConfig = new PropertiesConfiguration(fileName);
            propertiesConfig
                    .setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return propertiesConfig.getString(key);
    }

    @SuppressWarnings("rawtypes")
    public List getList(String key) {
        return propertiesConfig.getList(key);
    }

    public boolean getBoolean(String key) {
        return propertiesConfig.getBoolean(key);
    }

    public int getInt(String key) {
        return propertiesConfig.getInt(key);
    }
    
    public double getDouble(String key){
    	return propertiesConfig.getDouble(key);
    }

    public void setProperty(String key, Object value)
            throws ConfigurationException {
        if (propertiesConfig.containsKey(key)) {
            propertiesConfig.setProperty(key, value);
        } else {
            propertiesConfig.addProperty(key, value);
        }
        propertiesConfig.save();
    }

    @SuppressWarnings("rawtypes")
    public String getListString(String key) {
        List list = getList(key);
        String result = "";
        for (Object obj : list) {
            result += (String) obj;
            result += ",";
        }
        int last = result.lastIndexOf(",");
        if (last >= 0) {
            result = result.substring(0, last);
        }
        return result.trim();
    }

}

package nyaysabha;

import java.util.*;

public enum CaseCategory {
    CRIMINAL, CYBERCRIME, FORENSIC, FAMILY, CIVIL, CONSTITUTIONAL, ENVIRONMENTAL, CORPORATE, MOTOR_VEHICLE, CUSTOM;

    public static CaseCategory parse(String value) {
        try {
            return valueOf(value == null ? "CUSTOM" : value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Please choose a valid case category.");
        }
    }

    public static List<String> names() {
        List<String> n = new ArrayList<>();
        for (CaseCategory c : values()) {
            n.add(c.name());
        
        }return n;
    }
}
